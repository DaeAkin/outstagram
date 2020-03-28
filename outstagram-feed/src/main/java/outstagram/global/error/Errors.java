package outstagram.global.error;

import lombok.Getter;

@Getter
public enum Errors {

    ThereIsNoData(4004L,"There is no Data","해당 데이터를 찾을 수 없습니다.");

    private Long errorCode;
    private String errorMessage;
    private String details;

    Errors(Long errorCode, String errorMessage, String details) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.details = details;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
