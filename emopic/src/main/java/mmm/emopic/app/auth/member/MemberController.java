package mmm.emopic.app.auth.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.auth.member.Dto.MemberResponse;
import mmm.emopic.app.auth.member.Dto.RegisterRequest;
import mmm.emopic.app.auth.member.service.MemberService;
import mmm.emopic.app.auth.member.support.CurrentUser;
import mmm.emopic.app.base.Dto.BaseResponse;
import mmm.emopic.app.base.Dto.SavedId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

//Swagger
@Tag(name =  "회원 API")

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "회원가입에 필요한 데이터로회원가입을 한다.", responses = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SavedId.class)))
    })
    public ResponseEntity<BaseResponse> register(@Validated @RequestBody RegisterRequest registerRequest){
        SavedId savedId = memberService.register(registerRequest);
        return ResponseEntity.created(URI.create("/api/v1/member")).body(new BaseResponse(HttpStatus.CREATED.value(), "회원가입 성공",savedId));
    }

    @GetMapping("/member")
    @Operation(summary = "회원 조회", description = "Access Token 으로 회원을 조회한다.", responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = MemberResponse.class)))
    })
    public ResponseEntity findById(@CurrentUser Member member){
        MemberResponse response = MemberResponse.of(member);
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), "유저 정보 요청 성공", response));
    }
}
