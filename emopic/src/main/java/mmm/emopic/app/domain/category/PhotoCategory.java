package mmm.emopic.app.domain.category;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mmm.emopic.app.base.BaseEntity;
import mmm.emopic.app.domain.emotion.Emotion;
import mmm.emopic.app.domain.photo.Photo;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoCategory extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public PhotoCategory(Long id, Photo photo, Category category){
        this.id = id;
        this.photo = photo;
        this.category = category;
    }
}
