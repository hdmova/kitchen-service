package com.verimi.kitchenservice.common.error.exceptions;

import com.verimi.kitchenservice.common.error.codes.ApiErrorCode;

public class StaffMemberNotFoundException extends KitchenServiceException {

    public StaffMemberNotFoundException(Long id) {
        super("Could not find staff member with ID " + id);
    }

    @Override
    public ApiErrorCode getApiErrorCode() {
        return ApiErrorCode.STAFF_MEMBER_NOT_FOUND;
    }
}
