package com.rkm.projectmanagement.exception;

public class VendorNotFoundException extends RuntimeException {

    public VendorNotFoundException(Integer id) {
        super("Could not find vendor with id of " + id);
    }

}
