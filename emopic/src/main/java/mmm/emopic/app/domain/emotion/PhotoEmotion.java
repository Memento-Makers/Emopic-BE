package mmm.emopic.app.domain.emotion;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mmm.emopic.app.base.BaseEntity;
import mmm.emopic.app.domain.photo.Photo;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoEmotion extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_id")
    private Emotion emotion;

    @Builder
    public PhotoEmotion(Long id, Photo photo, Emotion emotion) {
        this.id = id;
        this.photo = photo;
        this.emotion = emotion;
    }

}
