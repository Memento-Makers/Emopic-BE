package mmm.emopic.app.domain.photo.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mmm.emopic.app.domain.photo.Photo;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
@Getter
public class PhotoUploadRequest {

    private MultipartFile image;

    public PhotoUploadRequest(MultipartFile image) {
        this.image = image;
    }
}
