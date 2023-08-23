package mmm.emopic.app.domain.emotion.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class EmotionUploadResponse {
    private final List<Long> photoEmotionIds;

    public EmotionUploadResponse(List<Long> photoEmotionIds) {
        this.photoEmotionIds = photoEmotionIds;
    }
}
