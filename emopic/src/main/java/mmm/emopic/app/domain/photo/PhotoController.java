package mmm.emopic.app.domain.photo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.base.Dto.BaseResponse;
import mmm.emopic.app.domain.photo.dto.response.PhotoInCategoryResponse;
import mmm.emopic.app.domain.photo.dto.request.PhotoCaptionRequest;
import mmm.emopic.app.domain.photo.dto.response.PhotoCaptionResponse;
import mmm.emopic.app.domain.photo.dto.request.PhotoUploadRequest;
import mmm.emopic.app.domain.photo.dto.response.PhotoInformationResponse;
import mmm.emopic.app.domain.photo.dto.response.PhotoUploadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/photos")
    @Operation(summary = "이미지 업로드", description = "이미지 업로드용 signed_url 생성요청", responses = {
            @ApiResponse(responseCode = "201", description = "signed_url 생성 성공", content = @Content(schema = @Schema(implementation = PhotoUploadResponse.class)))
    })
    public ResponseEntity<BaseResponse> createPhoto(@Validated @RequestBody PhotoUploadRequest photoUploadRequest){
        PhotoUploadResponse response = photoService.createPhoto(photoUploadRequest);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "이미지 업로드 signed_url 생성 성공",response));
    }

    @PostMapping("/photos/caption")
    @Operation(summary = "이미지 캡셔닝", responses = {
            @ApiResponse(responseCode = "200", description = "캡셔닝 생성 성공", content = @Content(schema = @Schema(implementation = PhotoCaptionResponse.class)))
    })
    public ResponseEntity<BaseResponse> getPhotoCaption(@Validated @RequestBody PhotoCaptionRequest photoCaptionRequest) throws Exception {
        PhotoCaptionResponse response = photoService.getPhotoCaption(photoCaptionRequest.getPhotoId());
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "캡셔닝 생성 성공", response));
    }

    @GetMapping("/photos/{photoId}")
    @Operation(summary = "개별 사진 조회", responses = {
            @ApiResponse(responseCode = "200", description = "개별 사진 조회 성공", content = @Content(schema = @Schema(implementation = PhotoInformationResponse.class)))
    })
    public ResponseEntity<BaseResponse> getPhotoInformation(@PathVariable(name="photoId") Long photoId) throws IOException {
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
}
