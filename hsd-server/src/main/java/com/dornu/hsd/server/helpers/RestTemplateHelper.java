package com.dornu.hsd.server.helpers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestTemplateHelper {

    public static <T, R> ResponseEntity<R> executeHttpRequest(RestTemplateParameters<T,R> restTemplateParameters) {
        return executeByExchange(
                restTemplateParameters.getRestTemplate(),
                restTemplateParameters.getRequestUrl(),
                restTemplateParameters.<T>getHttpEntity(),
                restTemplateParameters.<R>getResponseType(),
                restTemplateParameters.getHttpMethod()
        );
    }

    private static <T, R> ResponseEntity<R> executeByExchange(RestTemplate restTemplate, String requestUrl, HttpEntity<T> httpEntity, Class<R> responseType, HttpMethod httpMethod) {
        return restTemplate.exchange(
                requestUrl,
                httpMethod,
                httpEntity,
                responseType
        );
    }
}
