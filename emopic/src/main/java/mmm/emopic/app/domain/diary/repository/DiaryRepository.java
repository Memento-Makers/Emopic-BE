package mmm.emopic.app.domain.diary.repository;

import mmm.emopic.app.domain.diary.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary,Long> {
    Optional<Diary> findByPhotoId(Long photoId);
}

