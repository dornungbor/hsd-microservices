package com.dornu.hsd.security.auth;

import com.dornu.hsd.security.models.ErrorCode;
import com.dornu.hsd.security.models.ErrorDetails;
import com.dornu.hsd.security.models.HsdAccessTokenModel;
import com.dornu.hsd.security.models.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
public class HsdAccessTokenGranter {

    @Autowired
    private HsdTokenStore hsdTokenStore;

    @Autowired
    private HsdAuthenticationManager hsdAuthenticationManager;

    public ResponseEntity<HsdAccessTokenModel> generateAccessTokenResponse(@RequestHeader("Authorization") String authorizationHeader) {
        String basicAuthCredential = HsdAuthenticationManager.extractBasicAuth(authorizationHeader);
        if (hsdAuthenticationManager.isValidCredentials(basicAuthCredential)) {
            return new ResponseEntity<HsdAccessTokenModel>(hsdTokenStore.generateAccessToken(), HttpStatus.OK);
        }
        ErrorDetails error = ErrorDetails.builder()
                .code(ErrorCode.INVALID_CLIENT_CREDENTIALS)
                .message(ErrorCode.INVALID_CLIENT_CREDENTIALS.getDefaultMessage())
                .build();
        HsdAccessTokenModel token = HsdAccessTokenModel.builder().status(Status.FAILURE).error(error).build();
        return new ResponseEntity<HsdAccessTokenModel>(token, HttpStatus.UNAUTHORIZED);
    }
}
