package com.dornu.hsd.processor.models;

import com.dornu.hsd.processor.models.xommon.ErrorDetails;
import com.dornu.hsd.processor.models.xommon.Status;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "HsdProcessorAccountResponseBuilder", toBuilder = true)
@JsonDeserialize(builder = HsdProcessorAccountResponse.HsdProcessorAccountResponseBuilder.class)
@Getter
public class HsdProcessorAccountResponse {

    private String message;
    private Status status;
    private ErrorDetails error;

    @JsonPOJOBuilder(withPrefix = "")
    public static class HsdProcessorAccountResponseBuilder {}
}
