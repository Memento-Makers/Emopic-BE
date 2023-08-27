package mmm.emopic.app.domain.diary.repository;

import mmm.emopic.app.domain.diary.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary,Long> {
    Diary findByPhoto_Id(Long photoId);
}

