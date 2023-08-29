package mmm.emopic.app.domain.diary.repository;

import mmm.emopic.app.domain.diary.Diary;

import java.util.List;

public interface DiaryRepositoryCustom {
    List<Diary> getAllDiariesPhotoId(Long id);
}
