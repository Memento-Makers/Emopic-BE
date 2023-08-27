package mmm.emopic.app.domain.category;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mmm.emopic.app.base.BaseEntity;
import mmm.emopic.app.domain.photo.Photo;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String thumbnail;

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;
    */
    @Builder
    public Category(String name, String thumbnail){
        this.name = name;
        this.thumbnail = thumbnail;
    }
}
