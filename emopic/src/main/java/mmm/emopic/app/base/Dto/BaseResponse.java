package mmm.emopic.app.base.Dto;

import lombok.Getter;

@Getter
public class BaseResponse {
    private int status;
    private String message;
    private Object data;

    // 데이터 없는 생성자
    public BaseResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
    // 데이터 있는 생성자
    public BaseResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
