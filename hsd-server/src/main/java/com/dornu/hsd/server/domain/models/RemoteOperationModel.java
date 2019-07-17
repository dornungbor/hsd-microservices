package com.dornu.hsd.server.domain.models;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Builder
@Getter
public class RemoteOperationModel<T extends AbstractRemoteOperationRequest, R extends AbstractRemoteOperationResponse> {
    private String requestUrl;
    private HttpMethod httpMethod;
    private HttpStatus httpStatus;
    private Status status;
    private ErrorDetails error;
    private T request;
    private R response;
}
