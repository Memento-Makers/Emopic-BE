package mmm.emopic.app.domain.diary;

import lombok.*;
import mmm.emopic.app.base.BaseEntity;
import mmm.emopic.app.domain.emotion.Emotion;
import mmm.emopic.app.domain.photo.Photo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;


    @Builder
    public Diary(Photo photo, String content) {
        this.photo = photo;
        this.content = content;
    }
}
