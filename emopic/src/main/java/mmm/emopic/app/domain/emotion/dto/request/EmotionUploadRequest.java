package mmm.emopic.app.domain.emotion.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mmm.emopic.app.domain.emotion.Emotion;
import mmm.emopic.app.domain.emotion.PhotoEmotion;
import mmm.emopic.app.domain.photo.Photo;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class EmotionUploadRequest {


    @NotNull
    private Long emotionId;

    private List<Long> childEmotions;

    public PhotoEmotion toEntity(Photo photo, Emotion emotion) {
        return PhotoEmotion.builder()
                .photo(photo)
                .emotion(emotion)
                .build();
    }
}
