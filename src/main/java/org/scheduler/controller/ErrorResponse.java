package org.scheduler.controller;

public class ErrorResponse {

    private String errorMessage;

    public static ErrorResponse of(String errorMessage) {
        return new ErrorResponse(errorMessage);
    }

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

}
