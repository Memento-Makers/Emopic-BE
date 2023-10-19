package mmm.emopic.app.domain.photo.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.location.dto.response.LocationPointResponse;
import mmm.emopic.app.domain.photo.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import static mmm.emopic.app.domain.category.QPhotoCategory.photoCategory;
import static mmm.emopic.app.domain.photo.QPhoto.photo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                .orderBy(makeOrder(pageable.getSort()))
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
                .orderBy(makeOrder(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPQLQuery<Photo> count = queryFactory
                .select(photo)
                .from(photo);

        return PageableExecutionUtils.getPage(queryResults,pageable,() -> count.fetchCount());
    }
    public List<Photo> findAllByExpiredTime() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        List<Photo> queryResults = queryFactory
                .selectFrom(photo)
                .where(photo.signedUrlExpireTime.before(tomorrow))
                .fetch();
        return queryResults;
    }

    @Override
    public List<Photo> findAllByLocationYN() {
        List<Photo> queryResults = queryFactory
                .selectFrom(photo)
                .where(eqExistsLocation().and(eqNotDeleted()))
                .fetch();
        return queryResults;
    }

    @Override
    public Optional<LocationPointResponse> findRecentPhotoAndCountByCity(String city) {
        Photo recentPhoto = queryFactory
                .selectFrom(photo)
                .where(
                        eqExistsLocation().and(eqLocationCity(city)).and(eqNotDeleted())
                )
                .orderBy(makeOrder(makeSort("createDate")))
                .fetchFirst();
        JPQLQuery<Photo> result = queryFactory
                .selectFrom(photo)
                .where(
                        eqExistsLocation().and(eqLocationCity(city)).and(eqNotDeleted())
                );

        Long count = result.fetchCount();

        if(count.equals(0L)){
            return Optional.empty();
        }
        return Optional.of(new LocationPointResponse(recentPhoto,count));
    }

    @Override
    public Optional<Photo> findRecentPhoto() {
        Photo recentPhoto = queryFactory
                .selectFrom(photo)
                .where(
                        eqExistsLocation().and(eqNotDeleted())
                )
                .orderBy(makeOrder(makeSort("createDate")))
                .fetchFirst();
        if(recentPhoto.equals(null)){
            return Optional.empty();
        }
        return Optional.of(recentPhoto);
    }

    private OrderSpecifier[] makeOrder(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        for(Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            PathBuilder conditions = new PathBuilder(Photo.class, "photo");
            orders.add(new OrderSpecifier(direction, conditions.get(property)));
        }

        return orders.stream().toArray(OrderSpecifier[]::new);
    }

    private BooleanExpression eqNotDeleted(){
        return photo.deleted.isFalse();
    }

    private BooleanExpression eqExistsLocation() {return photo.location_YN.isTrue();}

    private BooleanExpression eqLocationCity(String city){
        return photo.location.address_1depth.eq(city);
    }

    private Sort makeSort(String properties){
        return Sort.by(Sort.Direction.DESC,properties);
    }

}
