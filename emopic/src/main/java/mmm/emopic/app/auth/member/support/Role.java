package mmm.emopic.app.auth.member.support;

import lombok.Getter;

@Getter
public enum Role {
    USER("유저"),ADMIN("관리자");
    private String name;

    Role(String name) {
        this.name = name;
    }
}
