package com.verimi.kitchenservice.common.error.codes;

import com.verimi.kitchenservice.common.error.ApiError;
import lombok.Getter;
import org.springframework.http.HttpStatus;


public enum ApiErrorCode implements IApiErrorCode {

    INTERNAL_SERVICE_ERROR(1, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error"),

    STAFF_MEMBER_NOT_FOUND(2, HttpStatus.NOT_FOUND, "staff member not found"),

    MISSING_QUERY_PARAM(4, HttpStatus.BAD_REQUEST, "Missing query-string parameter"),

    MISSING_BODY_FIELD(5, HttpStatus.BAD_REQUEST, "Invalid body field"),

    INVALID_PARAM_VALUE(6, HttpStatus.BAD_REQUEST, "Invalid parameter value"),

    INVALID_WEEK_RANGE(7, HttpStatus.BAD_REQUEST, "Invalid week date"),

    STAFF_SHORTAGE(8, HttpStatus.PRECONDITION_FAILED, "Not enough staff available"),

    STAFF_ON_VACATION(9, HttpStatus.PRECONDITION_FAILED, "Not enough staff available");

    @Getter
    private final int code;

    @Getter
    private final HttpStatus httpStatus;

    @Getter
    private final String message;

    ApiErrorCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public ApiError toResponseEntity() {
        return new ApiError(code, name(), message);
    }

    @Override
    public ApiError toResponseEntity(String detailedMessage) {
        return new ApiError(code, name(), message + ": " + detailedMessage);
    }
}
