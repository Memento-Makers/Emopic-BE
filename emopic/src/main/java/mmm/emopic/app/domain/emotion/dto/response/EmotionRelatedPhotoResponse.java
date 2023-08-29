package mmm.emopic.app.domain.emotion.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class EmotionRelatedPhotoResponse {
    private final List<Long> photoEmotionIds;

    public EmotionRelatedPhotoResponse(List<Long> photoEmotionIds) {
        this.photoEmotionIds = photoEmotionIds;
    }
}
