package com.mobiquityinc.exception;

/**
 * Class responsible for custom exception
 */
public class APIException extends RuntimeException {

    /**
     *
     * @param message
     */
    public APIException(String message) {
        super(message);
    }
}
