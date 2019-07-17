package com.dornu.hsd.server.domain.commands;

import com.dornu.hsd.server.domain.cache.AccessTokenCacheManager;
import com.dornu.hsd.server.domain.models.*;
import com.dornu.hsd.server.helpers.AuthorizationType;
import com.dornu.hsd.server.helpers.Helper;
import com.dornu.hsd.server.helpers.HttpAuthorization;
import com.dornu.hsd.server.helpers.RestTemplateParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.dornu.hsd.server.domain.constants.Constants.DATE_TIME_FORMATTER;
import static com.dornu.hsd.server.helpers.RestTemplateHelper.executeHttpRequest;

@Service
public class HsdProcessorAccessTokenCommand extends AbstractCommand<HsdProcessorAccessTokenRequest, HsdProcessorAccessTokenResponse> {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private AccessTokenCacheManager abstractCacheManager;

    @Override
    public Set<String> doDataValidation(HsdProcessorAccessTokenRequest requestModel) {
        return Collections.emptySet();
    }

    @Override
    public Set<String> doBusinessValidation(HsdProcessorAccessTokenRequest requestModel) {
        return Collections.emptySet();
    }

    @Override
    public HsdProcessorAccessTokenResponse handle(HsdProcessorAccessTokenRequest requestModel) {
        String now = LocalDateTime.now(ZoneOffset.UTC).format(DATE_TIME_FORMATTER);

        List<ServiceInstance> instances = this.discoveryClient.getInstances("hsd-processor");

        if (instances == null || instances.isEmpty()) {
            ErrorDetails error = ErrorDetails.builder()
                    .code(ErrorCode.SERVICE_UNAVAILABLE)
                    .message(ErrorCode.SERVICE_UNAVAILABLE.getDefaultMessage())
                    .build();
            return HsdProcessorAccessTokenResponse.builder()
                    .status(Status.FAILURE)
                    .error(error)
                    .build();
        }

        String requestUrl = "http://" + instances.get(0).getHost() + ":" + instances.get(0).getPort() + "/api/v1/token";

        HttpHeaders requestHeaders = getRequestHeaders(requestModel);
        RestTemplateParameters<MultiValueMap<String, String>, HsdProcessorAccessTokenResponse> restTemplateParameters =
                RestTemplateParameters
                        .<MultiValueMap<String, String>, HsdProcessorAccessTokenResponse>builder()
                        .restTemplate(restTemplate)
                        .requestUrl(requestUrl)
                        .httpEntity(Helper.createHeaderOnlyRequestEntity(requestHeaders))
                        .responseType(HsdProcessorAccessTokenResponse.class)
                        .httpMethod(HttpMethod.POST)
                        .build();

        return executeHttpRequest(restTemplateParameters).getBody();
    }

    private HttpHeaders getRequestHeaders(HsdProcessorAccessTokenRequest requestModel) {
        HttpAuthorization authorization = HttpAuthorization.builder()
                .type(AuthorizationType.BASIC)
                .username(requestModel.getUsername())
                .password(requestModel.getPassword())
                .build();
        return Helper.createHttpHeaders(authorization, MediaType.APPLICATION_FORM_URLENCODED, Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    @Override
    public void preHandle(HsdProcessorAccessTokenRequest requestModel) {

    }

    @Override
    public void postHandle(HsdProcessorAccessTokenRequest hsdProcessorAccessTokenRequest, HsdProcessorAccessTokenResponse hsdProcessorAccessTokenResponse) {
        if (hsdProcessorAccessTokenResponse.getStatus() == Status.FAILURE) return;
        abstractCacheManager.saveAccessTokenToCache(hsdProcessorAccessTokenResponse);
    }

    @Override
    public HsdProcessorAccessTokenResponse handleError(HsdProcessorAccessTokenRequest requestModel, HsdProcessorAccessTokenResponse responseModel, Exception exception) {
        return null;
    }

    @Override
    public HsdProcessorAccessTokenResponse handleValidationError(HsdProcessorAccessTokenRequest requestModel, Set<String> violations) {
        ErrorDetails error = ErrorDetails.builder()
                .code(ErrorCode.INVALID_PARAMETERS)
                .message(ErrorCode.INVALID_PARAMETERS.getDefaultMessage())
                .violations(violations.stream().map(x -> x).toArray(String[]::new))
                .build();

        return HsdProcessorAccessTokenResponse.builder()
                .status(Status.FAILURE)
                .error(error)
                .build();
    }
}


