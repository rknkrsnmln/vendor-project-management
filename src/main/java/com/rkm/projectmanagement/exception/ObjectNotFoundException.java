package com.rkm.projectmanagement.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectName, String id) {
        super("Could not find " + objectName + " with id of " + id);
    }

    public ObjectNotFoundException(String objectName, Integer id) {
        super("Could not find " + objectName + " with id of " + id);
    }
}
