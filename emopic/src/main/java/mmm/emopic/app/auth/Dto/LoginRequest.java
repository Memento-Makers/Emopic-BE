package mmm.emopic.app.auth.Dto;

import lombok.Getter;

@Getter
public class LoginRequest {

    String userId;
    String password;

    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
