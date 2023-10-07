package mmm.emopic.app.domain.category;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.domain.category.dto.response.CategoryDetailResponse;
import mmm.emopic.app.domain.category.dto.response.CategoryResponse;
import mmm.emopic.app.domain.category.repository.CategoryRepository;
import mmm.emopic.app.domain.category.repository.CategoryRepositoryCustom;
import mmm.emopic.app.domain.category.repository.PhotoCategoryRepository;
import mmm.emopic.app.domain.photo.Photo;
import mmm.emopic.app.domain.photo.repository.PhotoRepository;
import mmm.emopic.app.domain.photo.support.CategoryInferenceResponse;
import mmm.emopic.app.domain.photo.support.Translators;
import mmm.emopic.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mmm.emopic.app.domain.photo.support.PhotoInferenceWithAI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static mmm.emopic.app.domain.category.QPhotoCategory.photoCategory;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryRepositoryCustom categoryRepositoryCustom;

    @Transactional
    public CategoryDetailResponse getCategoriesAsMuchAsSize(Long size) {
        CategoryDetailResponse result = new CategoryDetailResponse();
        List<Tuple> list = categoryRepositoryCustom.getMostCategory(size);
        for (Tuple tuple : list) {
            Long categoryId = tuple.get(photoCategory.category.id);
            Long count = tuple.get(photoCategory.category.id.count());
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category", categoryId));
            result.AddCategoryDetail(category,count);
        }
        return result;
    }
}
