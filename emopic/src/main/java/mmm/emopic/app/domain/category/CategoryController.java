package mmm.emopic.app.domain.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.base.Dto.BaseResponse;
import mmm.emopic.app.domain.category.dto.response.CategoryDetailResponse;
import mmm.emopic.app.domain.category.dto.request.CategoryRequest;
import mmm.emopic.app.domain.category.dto.response.CategoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//swagger
@Tag(name="카테고리 API")

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class CategoryController {

    private final CategoryService categoryService;
    @PostMapping("/photos/categories")
    @Operation(summary = "분류 결과 조회", responses = {
            @ApiResponse(responseCode = "201", description = "분류 결과 조회 성공", content = @Content(schema = @Schema(implementation = CategoryResponse.class)))
    })
    public ResponseEntity<BaseResponse> requestCategories(@Validated @RequestBody CategoryRequest categoryGetAllRequest) throws Exception {

        CategoryResponse response = categoryService.requestCategories(categoryGetAllRequest.getPhotoId());
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "분류 결과 조회 성공", response));
    }

    @GetMapping("/photos/categories")
    @Operation(summary = "분류 결과 전체 조회", responses = {
            @ApiResponse(responseCode = "201", description = "분류 결과 전체 조회 성공", content = @Content(schema = @Schema(implementation = CategoryDetailResponse.class)))
    })
    public ResponseEntity<BaseResponse>  getCategoriesAsMuchAsSize(@RequestParam Long size){
        CategoryDetailResponse categories = categoryService.getCategoriesAsMuchAsSize(size);

        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "분류 결과 전체 조회 성공", categories));
    }


}
