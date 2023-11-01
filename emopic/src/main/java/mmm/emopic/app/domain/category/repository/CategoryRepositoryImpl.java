package mmm.emopic.app.domain.category.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.category.repository.CategoryRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;

import static mmm.emopic.app.domain.category.QCategory.category;
import static mmm.emopic.app.domain.category.QPhotoCategory.photoCategory;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    @Override
    public List<Tuple> getMostCategory(Long size) {
        Long defaultId = queryFactory.select(category.id).from(category)
                .where(eqDefault())
                .fetchFirst();


        List<Tuple> queryResults = queryFactory
                .select(photoCategory.category.id, photoCategory.category.id.count())
                .from(photoCategory)
                .groupBy(photoCategory.category.id)
                .having(photoCategory.category.id.ne(defaultId))
                .orderBy(photoCategory.category.id.count().desc())
                .limit(size)
                .fetch();
        return queryResults;
    }

    private BooleanExpression eqDefault(){
        return category.name.eq("default");
    }

}
