package mmm.emopic.app.auth.member.Dto;

import lombok.Getter;
import mmm.emopic.app.auth.member.Member;
import mmm.emopic.app.auth.member.support.Provider;
import mmm.emopic.app.auth.member.support.Role;

@Getter
public class RegisterRequest {

    private String userId;
    private String name;
    private String password;

    public RegisterRequest(String userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public Member toEntity(String encodedPassword){
        return Member.builder()
                .userId(userId)
                .name(name)
                .password(encodedPassword)
                .provider(Provider.EMOPIC)
                .role(Role.USER)
                .build();
    }
}
