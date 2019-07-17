package com.dornu.hsd.security.models;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_EMAIL("The email address is invalid"),
    INVALID_CVV("The credit card verification value is invalid"),
    INVALID_PARAMETERS("One or more request parameters are invalid; see 'error.violations' for details."),
    INVALID_CREDIT_CARD_ID("The Credit Card ID is invalid"),
    INVALID_CUSTOMER_ID("The Customer ID is invalid"),
    INVALID_CLIENT_CREDENTIALS("The username or password is incorrect.");

    private String defaultMessage;

    private ErrorCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
}
