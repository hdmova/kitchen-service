package com.verimi.kitchenservice.common.error.codes;

import com.verimi.kitchenservice.common.error.ApiError;
import org.springframework.http.HttpStatus;


public interface IApiErrorCode {

    HttpStatus getHttpStatus();

    ApiError toResponseEntity();

    ApiError toResponseEntity(String detailedMessage);

}
