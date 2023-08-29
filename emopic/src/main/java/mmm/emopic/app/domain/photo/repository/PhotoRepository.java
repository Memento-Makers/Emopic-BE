package mmm.emopic.app.domain.photo.repository;

import mmm.emopic.app.domain.photo.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo,Long> {
}
