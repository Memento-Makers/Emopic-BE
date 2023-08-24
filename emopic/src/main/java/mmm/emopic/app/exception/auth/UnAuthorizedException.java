package mmm.emopic.app.exception.auth;

public class UnAuthorizedException extends RuntimeException {
    private String errorCode = "UNAUTHORIZED";
    private String clientMessage = "인증에 실패했습니다!";

    public UnAuthorizedException(final String serverMessage, final String clientMessage, final String errorCode) {
        super(serverMessage);
        this.clientMessage = clientMessage;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getClientMessage() {
        return clientMessage;
    }

}
