package com.verimi.kitchenservice.common.error.exceptions;

import com.verimi.kitchenservice.common.error.codes.ApiErrorCode;

public class InvalidWeekRangeException extends KitchenServiceException {

    public InvalidWeekRangeException(String message) {
        super(message);
    }

    @Override
    public ApiErrorCode getApiErrorCode() {
        return ApiErrorCode.INVALID_WEEK_RANGE;
    }
}
