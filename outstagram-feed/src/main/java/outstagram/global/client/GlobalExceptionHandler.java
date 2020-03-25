package outstagram.global.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import outstagram.global.domain.ApiErrorResponse;
import outstagram.global.exception.ApiErrorException;
import outstagram.global.exception.NoDataException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiErrorException.class)
    public final ResponseEntity<ApiErrorResponse> ExceptionHandle(final ApiErrorException exception) {
        ApiErrorResponse response = new ApiErrorResponse(exception.getErrorCode(),
                exception.getErrorMessage(),
                exception.getDetails());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
