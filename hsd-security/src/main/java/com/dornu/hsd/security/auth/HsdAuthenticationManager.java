package com.dornu.hsd.security.auth;

import com.dornu.hsd.security.configurations.HsdAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Base64;

@Service
public class HsdAuthenticationManager implements AuthenticationManager {

    @Autowired
    private HsdTokenStore hsdTokenStore;

    @Autowired
    private HsdAppConfiguration hsdAppConfiguration;

    private static String sha256ifiedCredentials;

    @PostConstruct
    public void init() {
        String usernamePassword = hsdAppConfiguration.getCredentials().getUsername() + ":" + hsdAppConfiguration.getCredentials().getPassword();
        HsdAuthenticationManager.sha256ifiedCredentials = Base64.getEncoder().encodeToString(usernamePassword.getBytes());
    }

    public boolean isValidCredentials(String sha256ifiedCredentials) {
        return HsdAuthenticationManager.sha256ifiedCredentials.equalsIgnoreCase(sha256ifiedCredentials);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String authorization = (String) authentication.getPrincipal();
        if (isBearerAuthorization(authorization)) {
            if (hsdTokenStore.isValid(extractAccessToken(authorization))) {
                authentication.setAuthenticated(true);
                return authentication;
            }
        }
        throw new BadCredentialsException("Invalid Access Token");
    }


    private static boolean isBearerAuthorization(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase() + " ");
    }

    public static String extractAccessToken(String authorization) {
        if (!StringUtils.isEmpty(authorization) && authorization.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase())) {
            return authorization.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
        }
        return "";
    }

    public static String extractBasicAuth(String authorization) {
        if (!StringUtils.isEmpty(authorization) && authorization.toLowerCase().startsWith("Basic".toLowerCase())) {
            return authorization.substring("Basic".length()).trim();
        }
        return "";
    }
}
