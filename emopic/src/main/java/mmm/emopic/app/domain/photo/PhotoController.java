package mmm.emopic.app.domain.photo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.base.Dto.BaseResponse;
import mmm.emopic.app.domain.photo.dto.response.*;
import mmm.emopic.app.domain.photo.dto.request.PhotoUploadRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import java.net.URISyntaxException;
import java.util.List;

//Swagger
@Tag(name="사진 API")


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping(value = "/photos" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 업로드", description = "이미지 업로드 요청", responses = {
            @ApiResponse(responseCode = "201", description = "이미지 업로드 성공", content = @Content(schema = @Schema(implementation = PhotoUploadResponse.class)))
    })
    public ResponseEntity<BaseResponse> createPhoto(@Validated PhotoUploadRequest photoUploadRequest) {
        PhotoUploadResponse response = photoService.createPhoto(photoUploadRequest);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "이미지 업로드 성공",response));
    }

    @GetMapping("/photos/{photoId}")
    @Operation(summary = "개별 사진 조회", responses = {
            @ApiResponse(responseCode = "200", description = "개별 사진 조회 성공", content = @Content(schema = @Schema(implementation = PhotoInformationResponse.class)))
    })
    public ResponseEntity<BaseResponse> getPhotoInformation(@PathVariable(name="photoId") Long photoId){
        PhotoInformationResponse response = photoService.getPhotoInformation(photoId);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "개별 사진 조회 성공", response));
    }

    @GetMapping("/photos/categories/{categoryId}")

    @Operation(summary = "분류 결과 세부 조회", responses = {
            @ApiResponse(responseCode = "200", description = "분류 결과 세부 조회 성공", content = @Content(schema = @Schema(implementation = PhotoInformationResponse.class)))
    })
    public ResponseEntity<BaseResponse> getPhotoInCategory(@PathVariable(name = "categoryId") Long categoryId,@PageableDefault(page = 0, size = 10, sort="snapped_at",direction = Sort.Direction.DESC) Pageable pageable){
        Page<PhotoInCategoryResponse> response = photoService.getPhotoInCategory(categoryId, pageable);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "분류 결과 세부 조회 성공", response));
    }

    @GetMapping("/photos")
    @Operation(summary = "전체 사진 조회", responses = {
            @ApiResponse(responseCode = "200", description = "전체 사진 조회 성공", content = @Content(schema = @Schema(implementation = PhotoInformationResponse.class)))
    })
    public ResponseEntity<BaseResponse> getPhotosInformation(@PageableDefault(page = 0, size = 20, sort="snapped_at",direction = Sort.Direction.DESC) Pageable pageable){
        PageResponse response = photoService.getPhotosInformation(pageable);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "전체 사진 조회 성공", response));
    }
}
