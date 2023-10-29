package mmm.emopic.app.domain.location;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.category.Category;
import mmm.emopic.app.domain.category.PhotoCategory;
import mmm.emopic.app.domain.category.repository.CategoryRepository;
import mmm.emopic.app.domain.category.repository.PhotoCategoryRepository;
import mmm.emopic.app.domain.emotion.Emotion;
import mmm.emopic.app.domain.emotion.PhotoEmotion;
import mmm.emopic.app.domain.emotion.repository.EmotionRepository;
import mmm.emopic.app.domain.emotion.repository.PhotoEmotionRepository;
import mmm.emopic.app.domain.location.dto.response.CityResponse;
import mmm.emopic.app.domain.location.dto.response.LocationPhotoResponse;
import mmm.emopic.app.domain.location.dto.response.LocationPointResponse;
import mmm.emopic.app.domain.location.dto.response.LocationRecentResponse;
import mmm.emopic.app.domain.photo.Photo;
import mmm.emopic.app.domain.photo.dto.response.KakaoCoord2regionResponse;
import mmm.emopic.app.domain.photo.dto.response.PageResponse;
import mmm.emopic.app.domain.photo.dto.response.PhotosInformationResponse;
import mmm.emopic.app.domain.photo.repository.PhotoRepositoryCustom;
import mmm.emopic.app.domain.photo.support.KakaoMapAPI;
import mmm.emopic.exception.ResourceNotFoundException;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {
    private final CategoryRepository categoryRepository;
    private final EmotionRepository emotionRepository;
    private final PhotoEmotionRepository photoEmotionRepository;
    private final PhotoCategoryRepository photoCategoryRepository;
    private final PhotoRepositoryCustom photoRepositoryCustom;

    private final KakaoMapAPI kakaoMapAPI;
    public List<LocationPhotoResponse> getAllPhotos() {
        List<Photo> result = photoRepositoryCustom.findAllByLocationYN();
        return result.stream().map(photo -> new LocationPhotoResponse(photo)).collect(Collectors.toList());
    }

    public LocationPointResponse getCityAndCount(double latitude, double longitude) {

        KakaoCoord2regionResponse kakaoCoord2regionResponse = kakaoMapAPI.getLocationInfo(latitude,longitude).orElseThrow(() -> new RuntimeException("Kakao Map api 사용도중 에러가 발생했습니다"));

        LocationPointResponse response = photoRepositoryCustom.findRecentPhotoAndCountByCity(kakaoCoord2regionResponse.getRegion_1depth_name()).orElseThrow(() -> new RuntimeException("해당 지역에 사진이 없습니다"));

        return response;
    }

    public LocationRecentResponse getCityAndPhoto() {

        Photo photo = photoRepositoryCustom.findRecentPhoto().orElseThrow(() -> new RuntimeException("저장된 사진이 없습니다"));

        return new LocationRecentResponse(photo);
    }

    public List<CityResponse> getPhotoByCity() {

        List<Photo> result = photoRepositoryCustom.findPhotosGroupByCity();
        return result.stream().map(photo -> new CityResponse(photo)).collect(Collectors.toList());
    }

    public PageResponse getAllPhotosByCity(String city, Pageable pageable) {

        Page<Photo> result = photoRepositoryCustom.findAllPhotosByCity(city,pageable);
        List<PhotosInformationResponse> photosInformationResponseList = new ArrayList<>();
        for(Photo photo: result) {
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
        return new PageResponse(new PageImpl<>(photosInformationResponseList,result.getPageable(),result.getTotalElements()));

    }
}
