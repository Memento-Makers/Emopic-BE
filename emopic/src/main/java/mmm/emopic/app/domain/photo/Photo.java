package mmm.emopic.app.domain.photo;

import lombok.*;
import mmm.emopic.app.auth.member.Member;
import mmm.emopic.app.base.BaseEntity;
import mmm.emopic.app.domain.location.Location;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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

    private Boolean location_YN = false; // 기본적으로는 위치정보 없는 상태

    @Lob
    private String signedUrl;

    private LocalDateTime signedUrlExpireTime;

    @Lob
    private String tbSignedUrl;

    private LocalDateTime tbSignedUrlExpireTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Builder
    public Photo(Long id, String name, LocalDateTime snapped_at, String caption, Boolean location_YN, String signedUrl, LocalDateTime signedUrlExpireTime, String tbSignedUrl, LocalDateTime tbSignedUrlExpireTime, Member member, Location location) {
        this.id = id;
        this.name = name;
        this.snapped_at = snapped_at;
        this.caption = caption;
        this.location_YN = location_YN;
        this.signedUrl = signedUrl;
        this.signedUrlExpireTime = signedUrlExpireTime;
        this.tbSignedUrl = tbSignedUrl;
        this.tbSignedUrlExpireTime = tbSignedUrlExpireTime;
        this.member = member;
        this.location = location;
    }

    public void createSnappedAt(Date date){
        this.snapped_at = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public void createLocation(Location location){
        this.location_YN = true;
        this.location = location;
    }
}
