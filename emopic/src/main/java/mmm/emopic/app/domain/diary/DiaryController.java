package mmm.emopic.app.domain.diary;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.base.Dto.BaseResponse;
import mmm.emopic.app.domain.diary.dto.request.DiaryRequest;
import mmm.emopic.app.domain.diary.dto.response.DiaryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/photos/{photoId}/diaries")
    public ResponseEntity<BaseResponse> saveDiary(@Validated @RequestBody DiaryRequest diaryRequest, @PathVariable(name ="photoId") Long photoId){
        DiaryResponse response =  diaryService.saveDiary(diaryRequest.getContent(), photoId);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "일기장 생성 완료", response));
    }

}
