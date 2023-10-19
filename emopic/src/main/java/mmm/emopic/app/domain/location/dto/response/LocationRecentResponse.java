package mmm.emopic.app.domain.location.dto.response;

import lombok.Getter;
import mmm.emopic.app.domain.photo.Photo;

@Getter
public class LocationRecentResponse extends LocationPhotoResponse{
    private String city;

    public LocationRecentResponse(Photo photo) {
        super(photo);
        this.city = photo.getLocation().getAddress_1depth();
    }
}
