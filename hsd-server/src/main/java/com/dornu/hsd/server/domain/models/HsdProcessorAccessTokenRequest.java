package com.dornu.hsd.server.domain.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class HsdProcessorAccessTokenRequest extends AbstractRemoteOperationRequest {
    private String username;
    private String password;
}
