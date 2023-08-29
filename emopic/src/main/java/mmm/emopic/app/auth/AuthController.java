package mmm.emopic.app.auth;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.auth.Dto.LoginRequest;
import mmm.emopic.app.auth.Dto.TokenResponse;
import mmm.emopic.app.base.Dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Validated @RequestBody LoginRequest loginRequest){
        TokenResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), "로그인 성공", response));
    }
}
