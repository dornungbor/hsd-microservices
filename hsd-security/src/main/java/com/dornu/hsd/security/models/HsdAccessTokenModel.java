package com.dornu.hsd.security.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "MakeShiftAccessTokenModelBuilder", toBuilder = true)
@JsonDeserialize(builder = HsdAccessTokenModel.MakeShiftAccessTokenModelBuilder.class)
@Getter
public class HsdAccessTokenModel {
    private String access_token;
    private String token_type;
    private String issued_at;
    private String expires_at;
    private String expires_in;
    private Status status;
    private ErrorDetails error;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MakeShiftAccessTokenModelBuilder {}
}
