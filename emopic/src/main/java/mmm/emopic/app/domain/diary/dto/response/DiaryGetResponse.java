package mmm.emopic.app.domain.diary.dto.response;

import lombok.Builder;
import lombok.Getter;
import mmm.emopic.app.domain.diary.Diary;

@Getter
public class DiaryGetResponse {
    private String diary;
    private String caption;

    @Builder
    public DiaryGetResponse(String diary, String caption) {
        this.diary = diary;
        this.caption = caption;
    }
}
