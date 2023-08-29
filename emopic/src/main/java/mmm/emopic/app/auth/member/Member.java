package mmm.emopic.app.auth.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mmm.emopic.app.auth.member.support.Provider;
import mmm.emopic.app.auth.member.support.Role;
import mmm.emopic.app.base.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String password;

    private String name;

    @Lob
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Builder
    public Member(String userId, String password, String name, String profileImgUrl, Role role, Provider provider) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.profileImgUrl = profileImgUrl;
        this.role = role;
        this.provider = provider;
    }
}
