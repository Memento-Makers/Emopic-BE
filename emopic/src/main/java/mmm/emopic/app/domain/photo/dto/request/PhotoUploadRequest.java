package mmm.emopic.app.domain.photo.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mmm.emopic.app.domain.photo.Photo;

import javax.validation.constraints.NotBlank;
@Getter
@NoArgsConstructor
public class PhotoUploadRequest {

    @NotBlank
    private String fileName;

    public Photo toEntity() {
        return Photo.builder()
                .name(fileName)
                .build();
    }
}
