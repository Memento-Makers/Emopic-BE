package mmm.emopic.app.domain.diary.dto.response;

import lombok.Getter;
import mmm.emopic.app.domain.diary.Diary;

@Getter
public class DiarySaveResponse {
    private Long diaryId;

    public DiarySaveResponse(Diary diary){
        this.diaryId = diary.getId();
    }
}
