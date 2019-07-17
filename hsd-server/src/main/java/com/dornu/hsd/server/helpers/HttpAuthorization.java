package com.dornu.hsd.server.helpers;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Builder
@Getter
public class HttpAuthorization {

    @Getter(AccessLevel.NONE)
    private AuthorizationType type;
    private String username;
    private String password;
    private String accessToken;

    public AuthorizationType getType() {
        if (type == null) {
            if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
                type = AuthorizationType.BASIC;
            } else {
                type = AuthorizationType.BEARER;
            }
        }
        return type;
    }

}
