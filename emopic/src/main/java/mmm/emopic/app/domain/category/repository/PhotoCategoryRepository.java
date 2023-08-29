package mmm.emopic.app.domain.category.repository;

import mmm.emopic.app.domain.category.PhotoCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoCategoryRepository extends JpaRepository<PhotoCategory, Long> {
    List<PhotoCategory> findByPhotoId(Long photoId);
}
