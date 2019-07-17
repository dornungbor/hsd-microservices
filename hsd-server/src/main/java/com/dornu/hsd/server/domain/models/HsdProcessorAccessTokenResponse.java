package com.dornu.hsd.server.domain.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "HsdProcessorAccessTokenResponseBuilder", toBuilder = true)
@JsonDeserialize(builder = HsdProcessorAccessTokenResponse.HsdProcessorAccessTokenResponseBuilder.class)
@Getter
public class HsdProcessorAccessTokenResponse extends AbstractRemoteOperationResponse {
    private String access_token;
    private String token_type;
    private String issued_at;
    private String expires_at;
    private String expires_in;
    private Status status;
    private ErrorDetails error;

    @JsonPOJOBuilder(withPrefix = "")
    public static class HsdProcessorAccessTokenResponseBuilder {}
}
