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
    public List<LocationPhotoResponse> getAllPhotos() {
        List<Photo> response = photoRepositoryCustom.findAllByLocationYN();
        return response.stream().map(photo -> new LocationPhotoResponse(photo)).collect(Collectors.toList());
    }
    public LocationRecentResponse getCityAndPhoto() {

        Photo photo = photoRepositoryCustom.findRecentPhoto().orElseThrow(() -> new RuntimeException("저장된 사진이 없습니다"));

        return new LocationRecentResponse(photo);
    }
}
