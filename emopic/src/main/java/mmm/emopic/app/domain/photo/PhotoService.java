package mmm.emopic.app.domain.photo;


import com.fasterxml.jackson.core.JsonProcessingException;
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
import mmm.emopic.app.domain.photo.dto.response.PhotoCaptionResponse;
import mmm.emopic.app.domain.photo.dto.request.PhotoUploadRequest;
import mmm.emopic.app.domain.photo.dto.response.PhotoInCategoryResponse;
import mmm.emopic.app.domain.photo.dto.response.PhotoInformationResponse;
import mmm.emopic.app.domain.photo.dto.response.PhotoUploadResponse;
import mmm.emopic.app.domain.photo.repository.PhotoRepository;
import mmm.emopic.app.domain.photo.repository.PhotoRepositoryCustom;
import mmm.emopic.app.domain.photo.support.PhotoInferenceWithAI;
import mmm.emopic.app.domain.photo.support.SignedURLGenerator;
import mmm.emopic.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
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

    @Transactional
    public PhotoUploadResponse createPhoto(PhotoUploadRequest photoUploadRequest) {
        Photo photo = photoUploadRequest.toEntity();
        Photo savedPhoto = photoRepository.save(photo);
        String upLoadSignedUrl;
        String downLoadSignedUrl;
        try {
            upLoadSignedUrl = signedURLGenerator.generateV4PutObjectSignedUrl(savedPhoto.getName());
            downLoadSignedUrl = signedURLGenerator.generateV4GetObjectSignedUrl(savedPhoto.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        photo.setSignedUrl(downLoadSignedUrl);
        return new PhotoUploadResponse(savedPhoto.getId(),upLoadSignedUrl);
    }

    @Transactional
    public PhotoCaptionResponse getPhotoCaption(Long photoId) throws URISyntaxException, JsonProcessingException {
        // 캡셔닝 내용을 AI inference서버에서 받아와야 사용 가능
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new ResourceNotFoundException("photo", photoId));
        String result = photoInferenceWithAI.getCaptionByPhoto(photo.getSignedUrl());
        photo.setCaption(result);
        return new PhotoCaptionResponse(photo.getCaption());

    }

    @Transactional
    public PhotoInformationResponse getPhotoInformation(Long photoId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new ResourceNotFoundException("photo", photoId));
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
}
