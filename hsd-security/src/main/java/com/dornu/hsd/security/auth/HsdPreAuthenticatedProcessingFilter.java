package com.dornu.hsd.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class HsdPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

    private final String authorizationHeader = "Authorization";
    private static final TokenExtractor tokenExtractor = new BearerTokenExtractor();

    @Autowired
    public HsdPreAuthenticatedProcessingFilter(HsdAuthenticationManager hsdAuthenticationManager) {
        this.setAuthenticationManager(hsdAuthenticationManager);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }
}