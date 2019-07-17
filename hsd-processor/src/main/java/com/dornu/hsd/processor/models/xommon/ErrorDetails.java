package com.dornu.hsd.processor.models.xommon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "ErrorDetailsBuilder", toBuilder = true)
@JsonDeserialize(builder = ErrorDetails.ErrorDetailsBuilder.class)
@Getter
public class ErrorDetails {
    private ErrorCode code;
    private String message;
    private String[] violations;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ErrorDetailsBuilder {}
}
