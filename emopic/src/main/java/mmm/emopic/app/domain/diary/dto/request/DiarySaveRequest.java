package mmm.emopic.app.domain.diary.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class DiarySaveRequest {

    @NotBlank
    private String content;

}
