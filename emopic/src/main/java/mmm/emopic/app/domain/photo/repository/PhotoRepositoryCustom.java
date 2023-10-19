package mmm.emopic.app.domain.photo.repository;

import mmm.emopic.app.domain.photo.Photo;
import mmm.emopic.app.domain.photo.dto.response.PhotoInCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PhotoRepositoryCustom {
    Page<Photo> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Photo> findAllPhotos(Pageable pageable);
    List<Photo> findAllByExpiredTime();
    List<Photo> findAllByLocationYN();
}
