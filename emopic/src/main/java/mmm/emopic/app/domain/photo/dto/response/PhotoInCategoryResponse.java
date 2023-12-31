package mmm.emopic.app.domain.photo.dto.response;

import lombok.Getter;
import mmm.emopic.app.domain.category.Category;
import mmm.emopic.app.domain.diary.Diary;
import mmm.emopic.app.domain.emotion.Emotion;
import mmm.emopic.app.domain.emotion.dto.response.EmotionMainSubResponse;
import mmm.emopic.app.domain.emotion.dto.response.EmotionResponse;
import mmm.emopic.app.domain.photo.Photo;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PhotoInCategoryResponse {
    private Long photoId;

    private String signedUrl;

    private EmotionMainSubResponse emotions;


    public PhotoInCategoryResponse(Photo photo, List<Emotion> emotionList){
        this.photoId = photo.getId();
        this.signedUrl = photo.getSignedUrl();
        List<EmotionResponse> eRList = emotionList.stream().map(EmotionResponse::new).collect(Collectors.toList());
        this.emotions = new EmotionMainSubResponse(eRList);
    }

}
