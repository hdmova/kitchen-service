package com.verimi.kitchenservice.common.error.exceptions;

import com.verimi.kitchenservice.common.error.codes.ApiErrorCode;

public class StaffShortageException extends KitchenServiceException {
    public StaffShortageException(String message) {
        super(message);
    }

    @Override
    public ApiErrorCode getApiErrorCode() {
        return ApiErrorCode.STAFF_SHORTAGE;
    }
}
