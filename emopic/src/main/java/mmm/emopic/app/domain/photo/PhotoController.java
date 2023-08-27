package mmm.emopic.app.domain.photo;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.base.BaseEntity;
import mmm.emopic.app.base.Dto.BaseResponse;
import mmm.emopic.app.domain.photo.dto.request.PhotoCaptionRequest;
import mmm.emopic.app.domain.photo.dto.response.PhotoCaptionResponse;
import mmm.emopic.app.domain.photo.dto.request.PhotoUploadRequest;
import mmm.emopic.app.domain.photo.dto.response.PhotoUploadResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity getPhotoCaption(@Validated @RequestBody PhotoCaptionRequest photoCaptionRequest){
        PhotoCaptionResponse response = photoService.getPhotoCaption(photoCaptionRequest.getPhotoId());
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "캡셔닝 생성 완료", response));
    }

}
