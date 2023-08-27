package mmm.emopic.app.domain.diary.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class DiaryRequest {

    @NotBlank
    private String content;

}
