package com.dornu.hsd.server.domain.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder(builderClassName = "HsdProcessorAccountRequestBuilder", toBuilder = true)
@JsonDeserialize(builder = HsdProcessorAccountRequest.HsdProcessorAccountRequestBuilder.class)
@Getter
public class HsdProcessorAccountRequest extends AbstractRemoteOperationRequest {

    private String id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private String phone;
    private String address;
    private String createdOn;
    private String lastModifiedOn;
    private List<EncryptedCreditCard> creditCards;

    @JsonPOJOBuilder(withPrefix = "")
    public static class HsdProcessorAccountRequestBuilder {}
}
