package mmm.emopic.app.domain.photo.dto.response;

import lombok.Getter;
import mmm.emopic.app.domain.category.Category;
import mmm.emopic.app.domain.diary.Diary;
import mmm.emopic.app.domain.emotion.Emotion;
import mmm.emopic.app.domain.emotion.dto.response.EmotionMainSubResponse;
import mmm.emopic.app.domain.emotion.dto.response.EmotionResponse;
import mmm.emopic.app.domain.photo.Photo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class PhotosInformationResponse {
    private Long photoId;

    private String thumbnailUrl;

    private String uploadDateTime;

    private List<String> categories;

    private EmotionMainSubResponse emotions;

    public PhotosInformationResponse(Photo photo, List<Category> categoryList, List<Emotion> emotionList){
        this.photoId = photo.getId();
        this.thumbnailUrl = photo.getTbSignedUrl();
        this.categories = categoryList.stream().map(Category::getName).collect(Collectors.toList());
        List<EmotionResponse> eRList = emotionList.stream().map(EmotionResponse::new).collect(Collectors.toList());
        this.emotions = new EmotionMainSubResponse(eRList);
        Optional<LocalDateTime> dateTime = Optional.ofNullable(photo.getSnapped_at());
        dateTime.ifPresent(localDateTime -> this.uploadDateTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
