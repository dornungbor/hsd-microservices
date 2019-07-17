package com.dornu.hsd.server.helpers;

import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

@Getter
public class RestTemplateParameters<T, R> {

    private final RestTemplate restTemplate;
    private final String requestUrl;
    private final HttpEntity<T> httpEntity;
    private final Class<R> responseType;
    private final HttpMethod httpMethod;

    public RestTemplateParameters(RestTemplate restTemplate, String requestUrl, HttpEntity<T> httpEntity, Class<R> responseType, HttpMethod httpMethod) {
        this.restTemplate = restTemplate;
        this.requestUrl = requestUrl;
        this.httpEntity = httpEntity;
        this.responseType = responseType;
        this.httpMethod = httpMethod;
    }

    public static <T, R> RestTemplateParametersBuilder<T, R> builder() {
        return new RestTemplateParametersBuilder<T, R>();
    }

    public static class RestTemplateParametersBuilder<T, R> {

        private RestTemplateParametersBuilder() {
        }

        private RestTemplate restTemplate;
        private String requestUrl;
        private HttpEntity<T> httpEntity;
        private Class<R> responseType;
        private HttpMethod httpMethod;

        public RestTemplateParametersBuilder<T,R> restTemplate(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
            return this;
        }

        public RestTemplateParametersBuilder<T,R> requestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }
        public RestTemplateParametersBuilder<T,R> httpEntity(HttpEntity<T> httpEntity) {
            this.httpEntity = httpEntity;
            return this;
        }

        public RestTemplateParametersBuilder<T,R> responseType(Class<R> responseType) {
            this.responseType = responseType;
            return this;
        }

        public RestTemplateParametersBuilder<T,R> httpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public RestTemplateParameters<T, R> build() {
            return new RestTemplateParameters<T, R>(
                    restTemplate,
                    requestUrl,
                    httpEntity,
                    responseType,
                    httpMethod
            );
        }
    }
}
