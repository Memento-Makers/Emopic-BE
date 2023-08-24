package mmm.emopic.app.exception.auth;

import mmm.emopic.app.exception.base.UnAuthorizedException;

public class TokenNotValidException extends UnAuthorizedException {

    private static final String SERVER_MESSAGE = "유효하지 않은 토큰입니다.";
    private static final String ERROR_CODE = "TOKEN_IS_NOT_VALID";
    private static final String CLIENT_MESSAGE = "유효하지 않은 토큰입니다.";

    public TokenNotValidException() {
        super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
    }
    public TokenNotValidException(String Message) {
        super(Message, Message, ERROR_CODE);
    }
}
