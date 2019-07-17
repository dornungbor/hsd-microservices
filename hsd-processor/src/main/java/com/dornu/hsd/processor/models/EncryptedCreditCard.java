package com.dornu.hsd.processor.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

@Builder(builderClassName = "CreditCardBuilder", toBuilder = true)
@JsonDeserialize(builder = EncryptedCreditCard.CreditCardBuilder.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EncryptedCreditCard {

    private String id;
    private String encryptedCardDetails;
    private String salt;
    private String createdOn;
    private String lastModifiedOn;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CreditCardBuilder {}
}
