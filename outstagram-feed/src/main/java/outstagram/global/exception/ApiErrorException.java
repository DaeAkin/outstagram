package outstagram.global.exception;

import lombok.Getter;

@Getter
public class ApiErrorException extends RuntimeException{
    private Long errorCode;
    private String errorMessage;
    private String details;

    public ApiErrorException(Long errorCode, String errorMessage, String details) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.details = details;
    }

}
