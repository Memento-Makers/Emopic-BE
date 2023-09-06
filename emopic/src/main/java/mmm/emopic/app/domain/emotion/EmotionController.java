package mmm.emopic.app.domain.emotion;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.base.Dto.BaseResponse;
import mmm.emopic.app.domain.emotion.dto.request.EmotionUploadRequest;
import mmm.emopic.app.domain.emotion.dto.response.EmotionMainSubResponse;
import mmm.emopic.app.domain.emotion.dto.response.EmotionResponse;
import mmm.emopic.app.domain.emotion.dto.response.EmotionRelatedPhotoResponse;
import mmm.emopic.app.domain.photo.dto.response.PhotoInformationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//swagger
@Tag(name="감정 API")


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class EmotionController {
    private final EmotionService emotionService;
    @Operation(summary = "전체 감정 조회", responses = {
            @ApiResponse(responseCode = "200", description = "전체 감정 조회 성공", content = @Content(schema = @Schema(implementation = EmotionMainSubResponse.class)))
    })
    @GetMapping("/emotions")
    public ResponseEntity<BaseResponse> getEmotions(){
        EmotionMainSubResponse response = emotionService.findEmotions();
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), "전체 감정 조회 성공",response));
    }

    @PostMapping("/photos/{photoId}/emotions")
    @Operation(summary = "감정 저장", responses = {
            @ApiResponse(responseCode = "200", description = "감정 저장 성공", content = @Content(schema = @Schema(implementation = EmotionRelatedPhotoResponse.class)))
    })
    public ResponseEntity<BaseResponse> saveEmotionsInPhoto(@Validated @RequestBody EmotionUploadRequest emotionUploadRequest, @PathVariable(name ="photoId") Long photoId){
        EmotionRelatedPhotoResponse response = emotionService.saveEmotionsInPhoto(emotionUploadRequest,photoId);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "감정 저장 성공",response));
    }
}
