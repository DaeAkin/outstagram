package outstagram.global.domain;

import lombok.Getter;

@Getter
public class ApiErrorResponse {
    private Long errorCode;
    private String errorMessage;
    private String details;

    public ApiErrorResponse(Long errorCode, String errorMessage, String details) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.details = details;
    }
}
