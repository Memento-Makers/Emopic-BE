package mmm.emopic.app.domain.diary.dto.response;

import lombok.Getter;
import mmm.emopic.app.domain.diary.Diary;

@Getter
public class DiaryResponse {
    private Long diaryId;

    public DiaryResponse(Diary diary){
        this.diaryId = diary.getId();
    }
}
