package mmm.emopic.app.domain.photo.dto.response;

import lombok.Getter;

@Getter
public class PhotoCaptionResponse {
    private String caption;

    public PhotoCaptionResponse(String caption){
        this.caption = caption;
    }
}
