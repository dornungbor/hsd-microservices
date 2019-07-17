package com.dornu.hsd.server.domain.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "HsdProcessorAccountResponseBuilder", toBuilder = true)
@JsonDeserialize(builder = HsdProcessorAccountResponse.HsdProcessorAccountResponseBuilder.class)
@Getter
public class HsdProcessorAccountResponse extends AbstractRemoteOperationResponse {

    private String message;
    private Status status;
    private ErrorDetails error;

    @JsonPOJOBuilder(withPrefix = "")
    public static class HsdProcessorAccountResponseBuilder {}
}
