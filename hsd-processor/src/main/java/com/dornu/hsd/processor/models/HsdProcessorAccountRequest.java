package com.dornu.hsd.processor.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Builder(builderClassName = "HsdProcessorAccountRequestBuilder", toBuilder = true)
@JsonDeserialize(builder = HsdProcessorAccountRequest.HsdProcessorAccountRequestBuilder.class)
@Getter
public class HsdProcessorAccountRequest {

    private String id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String phone;
    private String address;
    private String createdOn;
    private String lastModifiedOn;
    private List<EncryptedCreditCard> creditCards;

    @JsonPOJOBuilder(withPrefix = "")
    public static class HsdProcessorAccountRequestBuilder {}
}
