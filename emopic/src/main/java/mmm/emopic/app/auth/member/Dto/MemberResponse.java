package mmm.emopic.app.auth.member.Dto;

import lombok.Builder;
import lombok.Getter;
import mmm.emopic.app.auth.member.Member;

@Getter
public class MemberResponse {

    private String userId;
    private String name;
    private String profile;

    @Builder
    public MemberResponse(String userId, String name, String profile) {
        this.userId = userId;
        this.name = name;
        this.profile = profile;
    }


    public static MemberResponse of(Member member){
        return MemberResponse.builder()
                .name(member.getName())
                .userId(member.getUserId())
                .profile(member.getProfileImgUrl())
                .build();
    }
}
