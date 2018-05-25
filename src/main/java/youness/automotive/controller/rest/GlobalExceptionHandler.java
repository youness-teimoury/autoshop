package youness.automotive.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * This exception handler helps us to intercept any outgoing exception/error message through rest services
 * All exceptions should be matched through @ExceptionHandler (with the exception used as the argument of the method)
 * For more info visit http://www.baeldung.com/exception-handling-for-rest-with-spring
 */

@ControllerAdvice
@Component
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // TODO log exceptions + user for security threat
    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "An internal error happened. Please contact the Admin.";
        logger.error(bodyOfResponse, ex);
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * Handles validation failures for Body Params (as the result of @Valid annotation)
     * For more info visit https://sdqali.in/blog/2015/12/04/validating-requestparams-and-pathvariables-in-spring-mvc/
     *
     * @param exception
     * @return
     */
//    @ExceptionHandler
//    @ResponseBody
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
// TODO uncomment above
    public Map handle(MethodArgumentNotValidException exception) {
        return error(exception.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList()));
    }


    /**
     * Handles validation failures for Request Params (as the result of @Validated annotation)
     * For more info visit https://sdqali.in/blog/2015/12/04/validating-requestparams-and-pathvariables-in-spring-mvc/
     *
     * @param exception
     * @return
     */
//    @ExceptionHandler
//    @ResponseBody
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
// TODO uncomment above
    public Map handle(ConstraintViolationException exception) {
        return error(exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()));
    }

    private Map error(Object message) {
        return Collections.singletonMap("error", message);
    }
}
