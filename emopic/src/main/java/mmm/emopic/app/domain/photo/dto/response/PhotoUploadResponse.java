package mmm.emopic.app.domain.photo.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PhotoUploadResponse {

    private Long photoId;
    private String signedUrl;
    private String tbSignedUrl;

    private List<String> categories;

    public PhotoUploadResponse(Long photoId, String signedUrl, String tbSignedUrl, List<String> categories) {
        this.photoId = photoId;
        this.signedUrl = signedUrl;
        this.tbSignedUrl = tbSignedUrl;
        this.categories = categories;
    }
}
