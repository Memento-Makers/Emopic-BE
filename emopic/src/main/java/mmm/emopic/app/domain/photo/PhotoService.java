package mmm.emopic.app.domain.photo;


import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.category.Category;
import mmm.emopic.app.domain.category.repository.CategoryRepository;
import mmm.emopic.app.domain.category.PhotoCategory;
import mmm.emopic.app.domain.category.repository.PhotoCategoryRepository;
import mmm.emopic.app.domain.diary.Diary;
import mmm.emopic.app.domain.diary.repository.DiaryRepository;
import mmm.emopic.app.domain.emotion.Emotion;
import mmm.emopic.app.domain.emotion.repository.EmotionRepository;
import mmm.emopic.app.domain.emotion.PhotoEmotion;
import mmm.emopic.app.domain.emotion.repository.PhotoEmotionRepository;
import mmm.emopic.app.domain.photo.dto.response.*;
import mmm.emopic.app.domain.photo.dto.request.PhotoUploadRequest;
import mmm.emopic.app.domain.photo.repository.PhotoRepository;
import mmm.emopic.app.domain.photo.repository.PhotoRepositoryCustom;
import mmm.emopic.app.domain.photo.support.Translators;
import mmm.emopic.app.domain.photo.support.PhotoInferenceWithAI;
import mmm.emopic.app.domain.photo.support.SignedURLGenerator;
import mmm.emopic.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final SignedURLGenerator signedURLGenerator;
    private final DiaryRepository diaryRepository;
    private final PhotoCategoryRepository photoCategoryRepository;
    private final PhotoEmotionRepository photoEmotionRepository;
    private final CategoryRepository categoryRepository;
    private final EmotionRepository emotionRepository;
    private final PhotoRepositoryCustom photoRepositoryCustom;
    private final PhotoInferenceWithAI photoInferenceWithAI;
    private final Translators translators;

    @Value("${DURATION}")
    private long duration;
    @Transactional
    public PhotoUploadResponse createPhoto(PhotoUploadRequest photoUploadRequest) {

        Photo photo = photoUploadRequest.toEntity();
        Photo savedPhoto = photoRepository.save(photo);
        String upLoadSignedUrl;
        String downLoadSignedUrl;
        String thumbnailSignedUrl;
        try {
            upLoadSignedUrl = signedURLGenerator.generateV4PutObjectSignedUrl(savedPhoto.getName());
            downLoadSignedUrl = signedURLGenerator.generateV4GetObjectSignedUrl(savedPhoto.getName());
            thumbnailSignedUrl = signedURLGenerator.generateV4GetObjectSignedUrl("thumbnail/"+savedPhoto.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        photo.setSignedUrl(downLoadSignedUrl);
        photo.setTb_signedUrl(thumbnailSignedUrl);
        LocalDateTime signedUrlExpiredTime = LocalDateTime.now().plusMinutes(duration);
        LocalDateTime thumbnailSignedUrlExpiredTime = LocalDateTime.now().plusMinutes(duration);
        photo.setSignedUrlExpireTime(signedUrlExpiredTime);
        photo.setTb_signedUrlExpireTime(thumbnailSignedUrlExpiredTime);
        return new PhotoUploadResponse(savedPhoto.getId(),upLoadSignedUrl);
    }

    @Transactional
    public PhotoCaptionResponse getPhotoCaption(Long photoId) throws Exception {
        // 캡셔닝 내용을 AI inference서버에서 받아와야 사용 가능
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new ResourceNotFoundException("photo", photoId));
        String result = photoInferenceWithAI.getCaptionByPhoto(photo.getSignedUrl());
        result = translators.deeplTranslate(result);
        photo.setCaption(result);
        Optional<Diary> optionalDiary = diaryRepository.findByPhotoId(photoId);
        Diary diary;
        if(optionalDiary.isEmpty()){
            diary = Diary.builder().photo(photo).build();
            diary = diaryRepository.save(diary);
            diary.setContent(result);
        }

        return new PhotoCaptionResponse(photo.getCaption());

    }

    @Transactional
    public PhotoInformationResponse getPhotoInformation(Long photoId) throws IOException {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new ResourceNotFoundException("photo", photoId));
        if(signedURLGenerator.isExpired(photo)){
            photo.setSignedUrl(signedURLGenerator.generateV4GetObjectSignedUrl(photo.getName()));
            photo.setSignedUrlExpireTime(LocalDateTime.now().plusMinutes(duration));
        }
        Optional<Diary> optionalDiary = diaryRepository.findByPhotoId(photoId);
        Diary diary;
        if(optionalDiary.isEmpty()){
            diary = Diary.builder().photo(photo).build();
            diary = diaryRepository.save(diary);
        }
        else{
            diary = optionalDiary.get();
        }
        List<PhotoCategory> photoCategoryList= photoCategoryRepository.findByPhotoId(photoId);
        List<Category> categories = new ArrayList<>();
        for(PhotoCategory photoCategory : photoCategoryList){
            Long cid = photoCategory.getCategory().getId();
            categories.add(categoryRepository.findById(cid).orElseThrow(() -> new ResourceNotFoundException("category",cid )));
        }
        List<PhotoEmotion> photoEmotionList = photoEmotionRepository.findByPhotoId(photoId);
        List<Emotion> emotions = new ArrayList<>();
        for(PhotoEmotion photoEmotion : photoEmotionList){
            Long eid = photoEmotion.getEmotion().getId();
            emotions.add(emotionRepository.findById(eid).orElseThrow(() -> new ResourceNotFoundException("emotion",eid )));
        }
        return new PhotoInformationResponse(photo, diary, categories, emotions);
    }

    @Transactional
    public Page<PhotoInCategoryResponse> getPhotoInCategory(Long categoryId, Pageable pageable){
        Page<Photo> photoList = photoRepositoryCustom.findByCategoryId(categoryId, pageable);
        List<PhotoInCategoryResponse> results = new ArrayList<>();
        for(Photo photo : photoList){
            List<PhotoEmotion> photoEmotionList = photoEmotionRepository.findByPhotoId(photo.getId());
            List<Emotion> emotions = new ArrayList<>();
            for(PhotoEmotion photoEmotion : photoEmotionList){
                Long eid = photoEmotion.getEmotion().getId();
                emotions.add(emotionRepository.findById(eid).orElseThrow(() -> new ResourceNotFoundException("emotion",eid )));
            }
            results.add(new PhotoInCategoryResponse(photo, emotions));
        }

        return new PageImpl<>(results,pageable, photoList.getTotalElements());
    }

    public Page<PhotosInformationResponse> getPhotosInformation(Pageable pageable) throws IOException {
        //List<Photo> photoList = photoRepository.findAll();
        Page<Photo> photoList = photoRepositoryCustom.findAllPhotos(pageable);
        List<PhotosInformationResponse> photosInformationResponseList = new ArrayList<>();
        for(Photo photo: photoList) {
            if (signedURLGenerator.isExpired(photo)) {
                photo.setSignedUrl(signedURLGenerator.generateV4GetObjectSignedUrl(photo.getName()));
                photo.setSignedUrlExpireTime(LocalDateTime.now().plusMinutes(duration));
            }
            Optional<Diary> optionalDiary = diaryRepository.findByPhotoId(photo.getId());
            Diary diary;
            if (optionalDiary.isEmpty()) {
                diary = Diary.builder().photo(photo).build();
                diary = diaryRepository.save(diary);
            } else {
                diary = optionalDiary.get();
            }
            List<PhotoCategory> photoCategoryList = photoCategoryRepository.findByPhotoId(photo.getId());
            List<Category> categories = new ArrayList<>();
            for (PhotoCategory photoCategory : photoCategoryList) {
                Long cid = photoCategory.getCategory().getId();
                categories.add(categoryRepository.findById(cid).orElseThrow(() -> new ResourceNotFoundException("category", cid)));
            }
            List<PhotoEmotion> photoEmotionList = photoEmotionRepository.findByPhotoId(photo.getId());
            List<Emotion> emotions = new ArrayList<>();
            for (PhotoEmotion photoEmotion : photoEmotionList) {
                Long eid = photoEmotion.getEmotion().getId();
                emotions.add(emotionRepository.findById(eid).orElseThrow(() -> new ResourceNotFoundException("emotion", eid)));
            }
            photosInformationResponseList.add(new PhotosInformationResponse(photo,diary,categories,emotions));
        }
        //return photosInformationResponseList;
        return new PageImpl<>(photosInformationResponseList,pageable, photoList.getTotalElements());
    }
}
