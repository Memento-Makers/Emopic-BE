package mmm.emopic.app.domain.emotion;

import lombok.*;
import mmm.emopic.app.base.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import mmm.emopic.app.domain.photo.Photo;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Emotion{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /*
    @OneToMany(mappedBy = "emotion", cascade = CascadeType.PERSIST)
    private List<UserEmotion> userEmotions = new ArrayList<>();
    */
    @Builder
    Emotion(Long id, String name){
        this.id = id;
        this.name = name;
    }
}
