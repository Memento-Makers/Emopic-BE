package mmm.emopic.app.domain.category.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CategoryRequest {
    @NotNull
    private Long photoId;

}
