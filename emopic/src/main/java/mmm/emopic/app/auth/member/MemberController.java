package mmm.emopic.app.auth.member;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@Validated @RequestBody RegisterRequest registerRequest){
        SavedId savedId = memberService.register(registerRequest);
        return ResponseEntity.created(URI.create("/api/v1/member")).body(new BaseResponse(HttpStatus.CREATED.value(), "회원가입 성공",savedId));
    }

    @GetMapping("/member")
    public ResponseEntity findById(@CurrentUser Member member){
        MemberResponse response = MemberResponse.of(member);
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), "유저 정보 요청 성공", response));
    }
}
