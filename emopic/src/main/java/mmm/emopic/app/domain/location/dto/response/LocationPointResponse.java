package mmm.emopic.app.domain.location.dto.response;

import lombok.Getter;
import mmm.emopic.app.domain.photo.Photo;

@Getter
public class LocationPointResponse {
    private Long photoId;
    private String thumbnailUrl;
    private Long count;
    private String city;

    public LocationPointResponse(Photo photo, Long count) {
        this.photoId = photo.getId();
        this.thumbnailUrl = photo.getTbSignedUrl();
        this.count = count;
        this.city = photo.getLocation().getAddress_1depth();
    }
}
