package com.rkm.projectmanagement.exception;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(String id) {
        super("Could not find project with id of " + id);
    }

}
