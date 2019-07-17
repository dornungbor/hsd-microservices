package com.dornu.hsd.processor.controllers;

import com.dornu.hsd.processor.models.HsdProcessorAccountRequest;
import com.dornu.hsd.processor.models.HsdProcessorAccountResponse;
import com.dornu.hsd.processor.models.xommon.Status;
import com.dornu.hsd.security.auth.HsdAccessTokenGranter;
import com.dornu.hsd.security.models.HsdAccessTokenModel;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AccountControllerV1 {

    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Autowired
    private HsdAccessTokenGranter hsdAccessTokenGranter;

    @Value("${spring.application.name}")
    private String applicationName;

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity<HsdProcessorAccountResponse> processAccount(@RequestBody HsdProcessorAccountRequest request) {
        String message = String.format("%s has received the request.", eurekaClient.getApplication(applicationName).getName());

        return new ResponseEntity<>(
                HsdProcessorAccountResponse.builder()
                        .message(message)
                        .status(Status.SUCCESS)
                        .build(),
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<HsdAccessTokenModel> accessToken(@RequestHeader(value = "Authorization") String authorizationHeader) {
        return hsdAccessTokenGranter.generateAccessTokenResponse(authorizationHeader);
    }
}
