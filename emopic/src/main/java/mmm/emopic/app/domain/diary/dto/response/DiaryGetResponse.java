package mmm.emopic.app.domain.diary.dto.response;

import lombok.Getter;
import mmm.emopic.app.domain.diary.Diary;

@Getter
public class DiaryGetResponse {
    private String diary;
    public DiaryGetResponse(Diary diary){
        this.diary = diary.getContent();
    }
}
