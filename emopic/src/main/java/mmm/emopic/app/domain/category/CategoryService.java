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
    private final PhotoRepository photoRepository;
    private final PhotoCategoryRepository photoCategoryRepository;
    private final PhotoInferenceWithAI photoInferenceWithAI;
    private final Translators translators;
    @Transactional
    public Category createCategory(String name){
        Category category = Category.builder().name(name).build();
        return categoryRepository.save(category);
    }

    @Transactional
    public void saveCategoriesInPhoto(Long photoId, List<Long> categoryIdList){
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new ResourceNotFoundException("photo", photoId));
        for(Long cid : categoryIdList){
            Category category = categoryRepository.findById(cid).orElseThrow(() -> new ResourceNotFoundException("category", cid));
            PhotoCategory photoCategory = PhotoCategory.builder().photo(photo).category(category).build();
            PhotoCategory savedPhotoCategory = photoCategoryRepository.save(photoCategory);
        }
    }
    @Transactional
    public CategoryResponse requestCategories(Long photoId) throws Exception {


        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new ResourceNotFoundException("photo", photoId));

        CategoryInferenceResponse categoryInferenceResponse = photoInferenceWithAI.getClassificationsByPhoto(photo.getSignedUrl());

        String dateTime = categoryInferenceResponse.getDateTime();
        if(!dateTime.isEmpty()) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
            LocalDateTime strToLocalDateTime = LocalDateTime.parse(dateTime, format);
            photo.setSnapped_at(strToLocalDateTime);
        }
        else{
            LocalDateTime localDateTime = photo.getCreateDate();
            photo.setSnapped_at(localDateTime);
        }

        List<String> requiredTranslateResult = categoryInferenceResponse.getCategories();
        List<String> result = new ArrayList<>();
        for(String translateText :requiredTranslateResult){
            result.add(translators.papagoTranslate(translateText));
        }
        for(String categoryName : result){
            Category category = categoryRepository.findByName(categoryName).orElseGet(() -> createCategory(categoryName)
            );
            PhotoCategory photoCategory = PhotoCategory.builder().photo(photo).category(category).build();
            PhotoCategory savedPhotoCategory = photoCategoryRepository.save(photoCategory);
        }

        return new CategoryResponse(result);
    }

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
