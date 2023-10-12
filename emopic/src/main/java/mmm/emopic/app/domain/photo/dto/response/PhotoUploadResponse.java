package mmm.emopic.app.domain.photo.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PhotoUploadResponse {

    private Long photoId;
    private String tbSignedUrl;

    public PhotoUploadResponse(Long photoId,String tbSignedUrl) {
        this.photoId = photoId;
        this.tbSignedUrl = tbSignedUrl;
    }
}
