package mmm.emopic.app.domain.diary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.base.Dto.BaseResponse;
import mmm.emopic.app.domain.diary.dto.request.DiarySaveRequest;
import mmm.emopic.app.domain.diary.dto.response.DiaryGetResponse;
import mmm.emopic.app.domain.diary.dto.response.DiarySaveResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
//swagger
@Tag(name="일기장 API")

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/photos/{photoId}/diaries")
    @Operation(summary = "일기장 생성", responses = {
            @ApiResponse(responseCode = "201", description = "일기장 생성 성공", content = @Content(schema = @Schema(implementation = DiarySaveResponse.class)))
    })
    public ResponseEntity<BaseResponse> saveDiary(@Validated @RequestBody DiarySaveRequest diarySaveRequest, @PathVariable(name ="photoId") Long photoId){
        DiarySaveResponse response =  diaryService.saveDiary(diarySaveRequest.getContent(), photoId);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "일기장 생성 성공", response));
    }

    @GetMapping("/photos/{photoId}/diaries")
    @Operation(summary = "일기장 조회", responses = {
            @ApiResponse(responseCode = "200", description = "일기장 조회 성공", content = @Content(schema = @Schema(implementation = DiaryGetResponse.class)))
    })
    public ResponseEntity<BaseResponse> getDiary(@PathVariable(name ="photoId") Long photoId){
        DiaryGetResponse response = diaryService.getDiary(photoId);
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), "일기장 조회 완료", response));
    }
}
