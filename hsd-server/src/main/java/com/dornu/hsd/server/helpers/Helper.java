package com.dornu.hsd.server.helpers;

import com.dornu.hsd.server.domain.models.AbstractCoomandResponse;
import com.dornu.hsd.server.domain.models.HsdProcessorAccessTokenResponse;
import com.dornu.hsd.server.domain.models.Status;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

public class Helper {

    public static String buildUrl(String baseUrl, MultiValueMap<String, String> queryParams, String... paths) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path((paths != null && paths.length > 0) ? String.join("/", paths) : "")
                .queryParams(queryParams)
                .toUriString();
    }

    public static HttpEntity<MultiValueMap<String, String>> createHeaderOnlyRequestEntity(MultiValueMap<String, String> httpHeaders) {
        return new HttpEntity<>(httpHeaders);
    }

    public static <T> HttpEntity<T> createHeaderAndPayloadRequestEntity(T payload, MultiValueMap<String, String> httpHeaders) {
        return new HttpEntity<>(payload, httpHeaders);
    }

    public static HttpEntity<MultiValueMap<String, String>> createRequestEntityWithHeaderAndFormData(MultiValueMap<String, String> formData, HttpHeaders httpHeaders) {
        return new HttpEntity<>(formData, httpHeaders);
    }

    public static String extractAccessToken(HsdProcessorAccessTokenResponse accessTokenResponseModel) {
        return accessTokenResponseModel.getAccess_token();
    }

    public static boolean isApprovedAccessToken(HsdProcessorAccessTokenResponse response) {
        return !StringUtils.isEmpty(response.getStatus()) && response.getStatus() == Status.SUCCESS;
    }

    public static int convertSecondsToMinutesOrDefault(int expirySeconds, int defaultMinutes) {
        if (expirySeconds < 1) return defaultMinutes;
        return expirySeconds / 60;
    }

    public static int getDesiredValueOrDefault(int desiredValue, int defaultValue) {
        return (desiredValue >= 120) ? desiredValue : defaultValue;
    }

    public static HttpHeaders createHttpHeaders(HttpAuthorization authorization, MediaType contentType, List<MediaType> acceptableMediaTypes) {
        HttpHeaders headers = new HttpHeaders();
        if (!StringUtils.isEmpty(contentType)) headers.setContentType(contentType);
        if (acceptableMediaTypes != null && !acceptableMediaTypes.isEmpty()) headers.setAccept(acceptableMediaTypes);
        if (authorization != null) {
            if (authorization.getType() == AuthorizationType.BASIC) {
                String username = authorization.getUsername();
                String password = authorization.getPassword();
                if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password))
                    headers.setBasicAuth(username, password);
            } else {
                String accessToken = authorization.getAccessToken();
                if (!StringUtils.isEmpty(accessToken)) headers.setBearerAuth(accessToken);
            }
        }
        return headers;
    }

    public static HttpHeaders getRequestHeaders(String accessToken) {
        HttpAuthorization authorization = HttpAuthorization
                .builder()
                .type(AuthorizationType.BEARER)
                .accessToken(accessToken)
                .build();
        return createHttpHeaders(authorization, null, Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    public static HttpStatus tryGetHttpStatus(AbstractCoomandResponse response) {
        if (response != null && response.getStatus() == Status.SUCCESS) return HttpStatus.OK;
        if (response == null || response.getError() == null || response.getError().getCode() == null) return HttpStatus.SERVICE_UNAVAILABLE;
        return response.getError().getCode().getHttpStatus();
    }
}
