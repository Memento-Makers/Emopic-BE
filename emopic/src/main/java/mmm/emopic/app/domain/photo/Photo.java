package mmm.emopic.app.domain.photo;

import lombok.*;
import mmm.emopic.app.auth.member.Member;
import mmm.emopic.app.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime snapped_at;

    @Lob // MYSQL -> LONGTEXT
    private String caption;

    private Boolean location_YM = false; // 기본적으로는 위치정보 없는 상태

    @Lob
    private String signedUrl;

    private LocalDateTime signedUrlExpireTime;

    @Lob
    private String tbSignedUrl;

    private LocalDateTime tbSignedUrlExpireTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @Builder
    public Photo(Long id, String name, LocalDateTime snapped_at, String caption, Boolean location_YM, String signedUrl, LocalDateTime signedUrlExpireTime, String tbSignedUrl, LocalDateTime tbSignedUrlExpireTime, Member member) {
        this.id = id;
        this.name = name;
        this.snapped_at = snapped_at;
        this.caption = caption;
        this.location_YM = location_YM;
        this.signedUrl = signedUrl;
        this.signedUrlExpireTime = signedUrlExpireTime;
        this.tbSignedUrl = tbSignedUrl;
        this.tbSignedUrlExpireTime = tbSignedUrlExpireTime;
        this.member = member;
    }

}
