package mmm.emopic.app.domain.photo;

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
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1") //
public class PhotoController {
    private final PhotoService photoService;

    //이미지 업로드
    @PostMapping("/photos")
    public ResponseEntity<BaseResponse> createPhoto(@Validated @RequestBody PhotoUploadRequest photoUploadRequest){
        PhotoUploadResponse response = photoService.createPhoto(photoUploadRequest);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "이미지 업로드 signed_url 생성 성공",response));
    }

    @PostMapping("/photos/caption")
    public ResponseEntity<BaseResponse> getPhotoCaption(@Validated @RequestBody PhotoCaptionRequest photoCaptionRequest){
        PhotoCaptionResponse response = photoService.getPhotoCaption(photoCaptionRequest.getPhotoId());
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "캡셔닝 생성 완료", response));
    }

    @GetMapping("/photos/{photoId}")
    public ResponseEntity<BaseResponse> getPhotoInformation(@PathVariable(name="photoId") Long photoId){
        PhotoInformationResponse response = photoService.getPhotoInformation(photoId);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "개별 사진 조회 완료", response));
    }

    @GetMapping("/photos/categories/{categoryId}")
    public ResponseEntity<BaseResponse> getPhotoInCategory(@PathVariable(name = "categoryId") Long categoryId,@PageableDefault(size = 1) Pageable pageable){
        Page<PhotoInCategoryResponse> response = photoService.getPhotoInCategory(categoryId, pageable);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "분류 결과 세부 조회 완료", response));
    }
}
