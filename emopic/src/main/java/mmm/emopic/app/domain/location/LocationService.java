package mmm.emopic.app.domain.location;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.location.dto.response.LocationPhotoResponse;
import mmm.emopic.app.domain.location.dto.response.LocationPointResponse;
import mmm.emopic.app.domain.location.dto.response.LocationRecentResponse;
import mmm.emopic.app.domain.photo.Photo;
import mmm.emopic.app.domain.photo.dto.response.KakaoCoord2regionResponse;
import mmm.emopic.app.domain.photo.repository.PhotoRepositoryCustom;
import mmm.emopic.app.domain.photo.support.KakaoMapAPI;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {

    private final PhotoRepositoryCustom photoRepositoryCustom;

    private final KakaoMapAPI kakaoMapAPI;
    public List<LocationPhotoResponse> getAllPhotos() {
        List<Photo> response = photoRepositoryCustom.findAllByLocationYN();
        return response.stream().map(photo -> new LocationPhotoResponse(photo)).collect(Collectors.toList());
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
}
