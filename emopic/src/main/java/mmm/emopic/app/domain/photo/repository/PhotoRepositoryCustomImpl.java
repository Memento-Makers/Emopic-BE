package mmm.emopic.app.domain.photo.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.photo.Photo;
import mmm.emopic.app.domain.photo.repository.PhotoRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import static mmm.emopic.app.domain.category.QPhotoCategory.photoCategory;
import static mmm.emopic.app.domain.photo.QPhoto.photo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PhotoRepositoryCustomImpl implements PhotoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Photo> findByCategoryId(Long categoryId, Pageable pageable){
        List<Photo> queryResults = queryFactory
                .selectFrom(photo)
                .where(photo.id.in(
                        JPAExpressions
                                .select(photoCategory.photo.id)
                                .from(photoCategory)
                                .where(photoCategory.category.id.eq(categoryId))
                ))
                .orderBy(makeSort(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Page<Photo> pagedResults = new PageImpl<>(queryResults, pageable, queryResults.size());
        return pagedResults;

    }

    @Override
    public Page<Photo> findAllPhotos(Pageable pageable){
        List<Photo> queryResults = queryFactory
                .selectFrom(photo)
                .orderBy(makeSort(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Page<Photo> pagedResults = new PageImpl<>(queryResults, pageable, queryResults.size());
        return pagedResults;
    }
    public List<Photo> findAllByExpiredTime() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        List<Photo> queryResults = queryFactory
                .selectFrom(photo)
                .where(photo.signedUrlExpireTime.before(tomorrow))
                .orderBy(photo.createDate.desc())
                .fetch();
        return queryResults;
    }

    private OrderSpecifier[] makeSort(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        for(Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            PathBuilder conditions = new PathBuilder(Photo.class, "photo");
            orders.add(new OrderSpecifier(direction, conditions.get(property)));
        }

        return orders.stream().toArray(OrderSpecifier[]::new);
    }
}
