package mmm.emopic.app.domain.category;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.base.Dto.BaseResponse;
import mmm.emopic.app.domain.category.dto.response.CategoryDetailResponse;
import mmm.emopic.app.domain.category.dto.request.CategoryRequest;
import mmm.emopic.app.domain.category.dto.response.CategoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class CategoryController {

    private final CategoryService categoryService;
    @PostMapping("/photos/categories")
    public ResponseEntity<BaseResponse> requestCategories(@Validated @RequestBody CategoryRequest categoryGetAllRequest){

        CategoryResponse response = categoryService.requestCategories(categoryGetAllRequest.getPhotoId());
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "분류 결과 조회 완료", response));
    }

    @GetMapping("/photos/categories")
    public ResponseEntity<BaseResponse>  getCategoriesAsMuchAsSize(@RequestParam Long size){
        CategoryDetailResponse categories = categoryService.getCategoriesAsMuchAsSize(size);

        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "분류 결과 전체 조회 완료", categories));
    }


}
