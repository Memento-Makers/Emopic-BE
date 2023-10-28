package mmm.emopic.app.domain.location.dto.response;

import lombok.Getter;
import mmm.emopic.app.domain.photo.Photo;

@Getter
public class LocationPointResponse extends CityResponse{
    private Long photoId;
    private Long count;

    public LocationPointResponse(Photo photo, Long count) {
        super(photo);
        this.photoId = photo.getId();
        this.count = count;
    }
}
