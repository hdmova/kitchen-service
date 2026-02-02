package com.verimi.kitchenservice.common.error.exceptions;


import com.verimi.kitchenservice.common.error.codes.ApiErrorCode;

public abstract class KitchenServiceException extends RuntimeException {

    KitchenServiceException(String message) {
        super(message);
    }

    KitchenServiceException() {
        super();
    }

    public abstract ApiErrorCode getApiErrorCode();
}
