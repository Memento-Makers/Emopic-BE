package mmm.emopic.app.domain.photo.repository;

import mmm.emopic.app.domain.location.dto.response.LocationPointResponse;
import mmm.emopic.app.domain.photo.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PhotoRepositoryCustom {
    Page<Photo> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Photo> findAllPhotos(Pageable pageable);
    List<Photo> findAllByExpiredTime();
    List<Photo> findAllByLocationYN();

    Optional<LocationPointResponse> findRecentPhotoAndCountByCity(String city);

    Optional<Photo> findRecentPhoto();

    List<Photo> findPhotosGroupByCity();
}
