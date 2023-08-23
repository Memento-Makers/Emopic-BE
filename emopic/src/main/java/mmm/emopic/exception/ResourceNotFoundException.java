package mmm.emopic.exception;

public class ResourceNotFoundException extends BadRequestException {
    private static final String ERROR_CODE = "BAD_REQUEST";
    private static final String SERVER_MESSAGE = "존재하지 않는 객체 조회";

    public ResourceNotFoundException(String domain, final Long id) {
        super(String.format("%s %s id : %d", domain, SERVER_MESSAGE, id), String.format("%s %s id : %d", domain, SERVER_MESSAGE, id), ERROR_CODE);
    }

    public ResourceNotFoundException(String domain, final Long id1, final Long id2, final Long id3) {
        super(String.format("%s %s id : %d, %d, %d", domain, SERVER_MESSAGE, id1, id2, id3), String.format("%s %s id : %d %d %d", domain, SERVER_MESSAGE, id1, id2, id3), ERROR_CODE);
    }

    public ResourceNotFoundException(String domain, final String code) {
        super(String.format("%s %s code : %s", domain, SERVER_MESSAGE, code), String.format("%s %s url : %s", domain, SERVER_MESSAGE, code), ERROR_CODE);
    }

    public ResourceNotFoundException(String reason) {
        super(SERVER_MESSAGE, reason, ERROR_CODE);
    }
}
