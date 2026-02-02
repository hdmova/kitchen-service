package com.verimi.kitchenservice.common.error;


import com.verimi.kitchenservice.common.error.codes.ApiErrorCode;
import com.verimi.kitchenservice.common.error.exceptions.KitchenServiceException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HTTPErrorHandler {

    @ExceptionHandler(KitchenServiceException.class)
    public final ResponseEntity<ApiError> handleServiceException(KitchenServiceException exception) {
        ApiErrorCode errorCode = exception.getApiErrorCode();
        String detailedMessage = exception.getMessage();
        return new ResponseEntity<>(errorCode.toResponseEntity(detailedMessage), errorCode.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getField)
                .collect(Collectors.toList());

        body.put("code", ApiErrorCode.MISSING_BODY_FIELD.getCode());
        body.put("message", ApiErrorCode.MISSING_BODY_FIELD.name());
        body.put("description", ApiErrorCode.MISSING_BODY_FIELD.getMessage() + ": Required: " + errors);
        return new ResponseEntity<>(body, ApiErrorCode.MISSING_BODY_FIELD.getHttpStatus());
    }
}
