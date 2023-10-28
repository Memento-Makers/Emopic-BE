package mmm.emopic.app.domain.location.dto.response;

import lombok.Builder;
import lombok.Getter;
import mmm.emopic.app.domain.photo.Photo;

@Getter
public class CityResponse {

    private String city;
    private String thumbnailUrl;


    @Builder
    public CityResponse(Photo photo) {
        this.city = photo.getLocation().getAddress_1depth();
        this.thumbnailUrl = photo.getTbSignedUrl();
    }
}
