package mmm.emopic.app.domain.emotion;


import lombok.RequiredArgsConstructor;
import mmm.emopic.app.base.Dto.BaseResponse;
import mmm.emopic.app.domain.emotion.dto.request.EmotionUploadRequest;
import mmm.emopic.app.domain.emotion.dto.response.EmotionResponse;
import mmm.emopic.app.domain.emotion.dto.response.EmotionRelatedPhotoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class EmotionController {
    private final EmotionService emotionService;

    @GetMapping("/emotions")
    public ResponseEntity<BaseResponse> getEmotions(){
        List<EmotionResponse> response = emotionService.findEmotions();
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), "전체 감정 조회 성공",response));
    }

    @PostMapping("/photos/{photoId}/emotions")
    public ResponseEntity<BaseResponse> saveEmotionsInPhoto(@Validated @RequestBody EmotionUploadRequest emotionUploadRequest, @PathVariable(name ="photoId") Long photoId){
        EmotionRelatedPhotoResponse response = emotionService.saveEmotionsInPhoto(emotionUploadRequest,photoId);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "감정 저장 성공",response));
    }
}
