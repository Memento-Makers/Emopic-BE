package mmm.emopic.app.domain.photo;

import lombok.*;
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

    @Builder
    public Photo(String name, LocalDateTime snapped_at, String caption, Boolean location_YM) {
        this.name = name;
        this.snapped_at = snapped_at;
        this.caption = caption;
        this.location_YM = location_YM;
    }
}
