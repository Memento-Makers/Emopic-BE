package mmm.emopic.app.domain.diary.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class DiaryGetRequest {

    @NotNull
    private Long photoId;
}
