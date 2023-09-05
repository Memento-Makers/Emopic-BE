package mmm.emopic.app.domain.diary;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.base.Dto.BaseResponse;
import mmm.emopic.app.domain.diary.dto.request.DiarySaveRequest;
import mmm.emopic.app.domain.diary.dto.response.DiaryGetResponse;
import mmm.emopic.app.domain.diary.dto.response.DiarySaveResponse;
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
    public ResponseEntity<BaseResponse> saveDiary(@Validated @RequestBody DiarySaveRequest diarySaveRequest, @PathVariable(name ="photoId") Long photoId){
        DiarySaveResponse response =  diaryService.saveDiary(diarySaveRequest.getContent(), photoId);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "일기장 생성 완료", response));
    }

    @GetMapping("/photos/{photoId}/diaries")
    public ResponseEntity<BaseResponse> getDiary(@PathVariable(name ="photoId") Long photoId){
        DiaryGetResponse response = diaryService.getDiary(photoId);
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), "일기장 조회 완료", response));
    }
}
