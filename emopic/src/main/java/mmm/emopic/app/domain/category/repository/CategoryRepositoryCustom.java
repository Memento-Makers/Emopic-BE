package mmm.emopic.app.domain.category.repository;

import com.querydsl.core.Tuple;

import java.util.List;

public interface CategoryRepositoryCustom {

    List<Tuple> getMostCategory(Long size);
}
