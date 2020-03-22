package outstagram.global.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import outstagram.global.exception.NoDataException;

@RestControllerAdvice
public class GlobalExceptionHandler {
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(NoDataException.class)
//    public Exception ExceptionHandle(NoDataException exception) {
//       return exception;
//    }
}
