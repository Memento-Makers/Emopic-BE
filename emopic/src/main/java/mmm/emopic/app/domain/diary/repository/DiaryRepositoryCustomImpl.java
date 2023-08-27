package mmm.emopic.app.domain.diary.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.diary.Diary;
import mmm.emopic.app.domain.diary.repository.DiaryRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;

import static mmm.emopic.app.domain.diary.QDiary.diary;

@Repository
@RequiredArgsConstructor
public class DiaryRepositoryCustomImpl implements DiaryRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Diary> getAllDiariesPhotoId(Long id) {

        List<Diary> queryResults = queryFactory
                .selectFrom(diary)
                .where(diary.photo.id.eq(id))
                .fetch();

        return queryResults;
    }
}
