package mmm.emopic.app.domain.photo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.photo.Photo;
import mmm.emopic.app.domain.photo.repository.PhotoRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static mmm.emopic.app.domain.category.QPhotoCategory.photoCategory;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PhotoRepositoryCustomImpl implements PhotoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Photo> findByCategoryId(Long categoryId, Pageable pageable){
        List<Photo> queryResults = queryFactory
                .select(photoCategory.photo)
                .from(photoCategory)
                .where(photoCategory.category.id.eq(categoryId))
                .fetch();

        Page<Photo> pagedResults = new PageImpl<>(queryResults, pageable, queryResults.size());
        return pagedResults;

    }
}
