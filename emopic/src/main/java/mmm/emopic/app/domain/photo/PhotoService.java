package mmm.emopic.app.domain.photo;


import com.drew.metadata.Metadata;
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
import mmm.emopic.app.domain.location.Location;
import mmm.emopic.app.domain.location.LocationRepository;
import mmm.emopic.app.domain.photo.dto.response.*;
import mmm.emopic.app.domain.photo.dto.request.PhotoUploadRequest;
import mmm.emopic.app.domain.photo.repository.PhotoRepository;
import mmm.emopic.app.domain.photo.repository.PhotoRepositoryCustom;
import mmm.emopic.app.domain.photo.support.*;
import mmm.emopic.exception.ResourceNotFoundException;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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
    private final ImageUploader imageUploader;

    private final MetadataExtractor metadataExtractor;
    private final LocationRepository locationRepository;
    private final KakaoMapAPI kakaoMapAPI;

    @Value("${DURATION}")
    private long duration;

    @Transactional
    public PhotoUploadResponse createPhoto(PhotoUploadRequest photoUploadRequest) {
        if(!photoUploadRequest.getImage().getContentType().startsWith("image/")){
            throw new RuntimeException("이미지 파일이 아닙니다");
        }
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));		//한국시간
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

        String fileName =  now.format(format) + userId.toString();


        ExecutorService executorService = Executors.newCachedThreadPool();

        // 이미지 업로드 -> 실패하면 사진 객체가 저장되면 안되므로 비동기 처리 하면 안됨.
        imageUploader.imageUpload(fileName, photoUploadRequest.getImage());


        // 다운로드용 signed_url 생성
        String signedUrl = getSignedUrl(fileName).orElseThrow(() -> new RuntimeException("create signed url error"));
        String thumbnailSignedUrl = getSignedUrl("thumbnail/"+fileName).orElseThrow(() -> new RuntimeException("create signed url error"));;
        // signed_url 만료시간 설정
        LocalDateTime signedUrlExpiredTime = LocalDateTime.now().plusMinutes(duration);
        LocalDateTime thumbnailSignedUrlExpiredTime = LocalDateTime.now().plusMinutes(duration);

        // photo 객체 만들기
        Photo photo = Photo.builder()
                .name(fileName)
                .signedUrl(signedUrl)
                .signedUrlExpireTime(signedUrlExpiredTime)
                .tbSignedUrl(thumbnailSignedUrl)
                .tbSignedUrlExpireTime(thumbnailSignedUrlExpiredTime)
                .build();

        // 저장하기
        Photo savedPhoto = photoRepository.save(photo);

        // 비동기 caption 요청하기
        executorService.execute(() -> {
            requestCaption(savedPhoto);
        });
        // 비동기 category 요청하기
        executorService.execute(() -> {
            requestCategories(savedPhoto);
        });
        // 메타데이터 추출 하기
        ExtractMetadata(savedPhoto,photoUploadRequest);

        // 빈 다이어리 생성
        Diary diary = Diary.builder().photo(savedPhoto).content("captioning 진행중 입니다.").build();
        diaryRepository.save(diary);

        return new PhotoUploadResponse(savedPhoto.getId(),thumbnailSignedUrl);
    }

    @Transactional
    public void ExtractMetadata(Photo photo, PhotoUploadRequest photoUploadRequest){

        Optional<Metadata> metadata = metadataExtractor.readMetadata(photoUploadRequest.getImage());

        boolean exists_gps_info = false;

        Optional<Point> point = null;

        KakaoCoord2regionResponse info = null;
        Optional<LocalDateTime> snappedDate = null;

        if(metadata.isPresent()){
            point = metadataExtractor.getLocationPoint(metadata.get());
            if(point.isPresent()){ // GPS 정보 있으면 추출
                Optional<KakaoCoord2regionResponse> LocationInfo = kakaoMapAPI.getLocationInfo(point.get().getX(),point.get().getY());
                if(LocationInfo.isPresent()){
                    info = LocationInfo.orElseThrow(() -> new RuntimeException("Kakao Map api 사용 도중 에러가 발생했습니다"));
                    exists_gps_info = true;
                }
            }
            snappedDate = metadataExtractor.getSnappedDate(metadata.get());
            if(snappedDate.isPresent()){ // 날짜 정보가 있으면 추출
                photo.createSnappedAt(snappedDate.get());
            }
            else{
                photo.createSnappedAt(LocalDateTime.now());
            }
        }

        if(exists_gps_info){//위치 정보가 있으면 저장
            Location location = Location.builder()
                    .full_address(info.getAddress_name())
                    .address_1depth(info.getRegion_1depth_name())
                    .address_2depth(info.getRegion_2depth_name())
                    .address_3depth(info.getRegion_3depth_name())
                    .address_4depth(info.getRegion_4depth_name())
                    .latitude(point.get().getX())
                    .longitude(point.get().getY())
                    .photoId(photo.getId())
                    .build();
            Location saved = locationRepository.save(location);
            photo.createLocation(saved);
        }
    }

    public void requestCategories(Photo photo){

        photoInferenceWithAI.getClassificationsByPhoto(photo.getId(),photo.getSignedUrl());

    }
    // 캡셔닝 내용 요청
    public void requestCaption(Photo photo) {
        photoInferenceWithAI.getCaptionByPhoto(photo.getId(),photo.getSignedUrl());
    }

    // Optional 사용방법 https://www.daleseo.com/java8-optional-effective/
    public Optional<String> getSignedUrl(String fileName){
        try {
            return Optional.of(signedURLGenerator.generateV4GetObjectSignedUrl(fileName));
        } catch (IOException e) {
            return Optional.empty();
        }
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

    public PageResponse getPhotosInformation(Pageable pageable){
        Page<Photo> photoList = photoRepositoryCustom.findAllPhotos(pageable);
        List<PhotosInformationResponse> photosInformationResponseList = new ArrayList<>();
        for(Photo photo: photoList) {
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
            photosInformationResponseList.add(new PhotosInformationResponse(photo,categories,emotions));
        }
        return new PageResponse(new PageImpl<>(photosInformationResponseList,photoList.getPageable(),photoList.getTotalElements()));
    }

}
