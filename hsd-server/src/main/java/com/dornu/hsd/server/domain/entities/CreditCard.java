package com.dornu.hsd.server.domain.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Builder(builderClassName = "CreditCardBuilder", toBuilder = true)
@JsonDeserialize(builder = CreditCard.CreditCardBuilder.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CreditCard {

    @Id
    private String id;
    @Column(name="ENCRYPTED_CARD_DETAILS",columnDefinition="LONGTEXT")
    private String encryptedCardDetails;
    private String salt;
    private String createdOn;
    private String lastModifiedOn;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CreditCardBuilder {}
}
