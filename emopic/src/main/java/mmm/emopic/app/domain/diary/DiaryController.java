package mmm.emopic.app.domain.diary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.base.Dto.BaseResponse;
import mmm.emopic.app.domain.diary.dto.request.DiaryRequest;
import mmm.emopic.app.domain.diary.dto.response.DiaryResponse;
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
            @ApiResponse(responseCode = "201", description = "일기장 생성 성공", content = @Content(schema = @Schema(implementation = DiaryResponse.class)))
    })
    public ResponseEntity<BaseResponse> saveDiary(@Validated @RequestBody DiaryRequest diaryRequest, @PathVariable(name ="photoId") Long photoId){
        DiaryResponse response =  diaryService.saveDiary(diaryRequest.getContent(), photoId);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "일기장 생성 성공", response));
    }

}
