package mmm.emopic.app.domain.emotion.dto.response;

import lombok.Getter;
import mmm.emopic.app.domain.emotion.Emotion;

@Getter
public class EmotionResponse {
    private Long id;
    private String name;

    public EmotionResponse(Emotion emotion){
        this.id = emotion.getId();
        this.name = emotion.getName();
    }
}
