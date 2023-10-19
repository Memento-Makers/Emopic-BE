package mmm.emopic.app.domain.location.dto.response;

import lombok.Getter;
import mmm.emopic.app.domain.photo.Photo;

@Getter
public class LocationPhotoResponse {
    private Long photoId;
    private String thumbnailUrl;
    private Double longitude;
    private Double latitude;


    public LocationPhotoResponse(Photo photo) {
        this.photoId = photo.getId();
        this.thumbnailUrl = photo.getTbSignedUrl();
        this.longitude = photo.getLocation().getLongitude();
        this.latitude = photo.getLocation().getLatitude();
    }


}
