package com.verimi.kitchenservice;

class StaffMemberNotFoundException extends RuntimeException {

    StaffMemberNotFoundException(Long id) {
        super("Could not find staff member with ID " + id);
    }
}
