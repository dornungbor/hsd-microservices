package com.dornu.hsd.server.domain.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Builder(builderClassName = "CreditCardBuilder", toBuilder = true)
@JsonDeserialize(builder = CreditCardModel.CreditCardBuilder.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreditCardModel {

    @NotEmpty
    private String cardNumber;
    @NotEmpty
    private String cardVerificationValue;
    @NotEmpty
    private String cardExpiryMonth;
    @NotEmpty
    private String cardExpiryYear;
    @NotEmpty
    private String cardHolderName;
    @NotEmpty
    @Email
    private String cardHolderEmail;
    @NotEmpty
    private String cardHolderPhone;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CreditCardBuilder {}
}
