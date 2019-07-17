package com.dornu.hsd.server.domain.models;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_EMAIL("The email address is invalid", HttpStatus.BAD_REQUEST),
    INVALID_CVV("The credit card verification value is invalid", HttpStatus.BAD_REQUEST),
    INVALID_PARAMETERS("One or more request parameters are invalid; see 'error.violations' for details.", HttpStatus.BAD_REQUEST),
    INVALID_CREDIT_CARD_ID("The Credit Card ID is invalid", HttpStatus.BAD_REQUEST),
    INVALID_CUSTOMER_ID("The Customer ID is invalid", HttpStatus.BAD_REQUEST),
    SOMETHING_WENT_WRONG("Something went wrong :(", HttpStatus.SERVICE_UNAVAILABLE),
    ACCESS_DENIED("Access Denied", HttpStatus.UNAUTHORIZED),
    SERVICE_UNAVAILABLE("The service is temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE);

    private final String defaultMessage;
    private final HttpStatus httpStatus;

    private ErrorCode(String defaultMessage, HttpStatus httpStatus) {
        this.defaultMessage = defaultMessage;
        this.httpStatus = httpStatus;
    }
}
