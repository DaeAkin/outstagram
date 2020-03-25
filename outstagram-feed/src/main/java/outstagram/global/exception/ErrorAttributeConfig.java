package outstagram.global.exception;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Configuration
public class ErrorAttributeConfig {
//    @Bean
//    public ErrorAttributes errorAttributes() {
//        return new DefaultErrorAttributes() {
//            @Override
//            public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
//                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest,false);
//                Throwable error = getError(webRequest);
//                if (error instanceof NoDataException) {
//                    errorAttributes.put("errors", ((NoDataException)error).getErrors());
//                    errorAttributes.put("message", ((NoDataException)error).getErrorMessage());
//                    errorAttributes.put("errorCode", ((NoDataException)error).getErrorCode());
//                }
//                return errorAttributes;
//            }
//
//        };
//    }
}
