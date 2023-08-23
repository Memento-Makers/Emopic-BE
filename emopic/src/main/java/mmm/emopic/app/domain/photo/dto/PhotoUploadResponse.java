package mmm.emopic.app.domain.photo.dto;

import lombok.Getter;

@Getter
public class PhotoUploadResponse {

    private Long photoId;
    private String signedUrl;

    public PhotoUploadResponse(Long photoId, String signedUrl) {
        this.photoId = photoId;
        this.signedUrl = signedUrl;
    }
}
