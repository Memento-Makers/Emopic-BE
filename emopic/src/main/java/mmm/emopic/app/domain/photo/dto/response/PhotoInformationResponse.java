package mmm.emopic.app.domain.photo.dto.response;

import lombok.Getter;
import mmm.emopic.app.domain.category.Category;
import mmm.emopic.app.domain.category.PhotoCategory;
import mmm.emopic.app.domain.diary.Diary;
import mmm.emopic.app.domain.emotion.Emotion;
import mmm.emopic.app.domain.emotion.PhotoEmotion;
import mmm.emopic.app.domain.emotion.dto.response.EmotionResponse;
import mmm.emopic.app.domain.photo.Photo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PhotoInformationResponse {

    private Long photoId;

    private String signedUrl;

    private Long diaryId;

    private String diaryContent;

    private List<String> categories;

    private List<EmotionResponse> emotions;

    public PhotoInformationResponse(Photo photo, Diary diary, List<Category> categoryList, List<Emotion> emotionList){
        this.photoId = photo.getId();
        this.signedUrl = photo.getSignedUrl();
        this.diaryId = diary.getId();
        this.diaryContent = diary.getContent();
        this.categories = categoryList.stream().map(Category::getName).collect(Collectors.toList());
        this.emotions = emotionList.stream().map(EmotionResponse::new).collect(Collectors.toList());

    }
}
