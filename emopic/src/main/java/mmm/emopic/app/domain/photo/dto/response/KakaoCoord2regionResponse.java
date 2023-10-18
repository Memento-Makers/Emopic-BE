package mmm.emopic.app.domain.photo.dto.response;

import lombok.Getter;

@Getter
public class KakaoCoord2regionResponse {

    private String region_type;
    private String code;
    private String address_name;
    private String region_1depth_name;
    private String region_2depth_name;
    private String region_3depth_name;
    private String region_4depth_name;
    private String x;
    private String y;
}
