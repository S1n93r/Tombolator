package com.example.tombolator.commons;

public class SwitchCaseNotDefinedException extends RuntimeException {

    public SwitchCaseNotDefinedException() {
    }

    public SwitchCaseNotDefinedException(String message) {
        super(message);
    }

    public SwitchCaseNotDefinedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SwitchCaseNotDefinedException(Throwable cause) {
        super(cause);
    }

    public SwitchCaseNotDefinedException(String message, Throwable cause, boolean enableSuppression,
                                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
