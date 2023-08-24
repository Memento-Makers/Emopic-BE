package mmm.emopic.app.auth;

import lombok.RequiredArgsConstructor;
import mmm.emopic.app.auth.Dto.LoginRequest;
import mmm.emopic.app.auth.Dto.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    public TokenResponse login(LoginRequest loginRequest) {
        return new TokenResponse();
    }
}
