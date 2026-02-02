package com.verimi.kitchenservice.common.error.exceptions;

import com.verimi.kitchenservice.common.error.codes.ApiErrorCode;

public class VacationException extends KitchenServiceException {
    public VacationException(String message) {
        super(message);
    }

    @Override
    public ApiErrorCode getApiErrorCode() {
        return ApiErrorCode.STAFF_ON_VACATION;
    }
}
