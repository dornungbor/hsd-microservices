package com.dornu.hsd.server.controllers;

import com.dornu.hsd.security.auth.HsdAccessTokenGranter;
import com.dornu.hsd.server.domain.commands.GetCreditCardByIdCommand;
import com.dornu.hsd.server.domain.commands.GetCustomerByIdCommand;
import com.dornu.hsd.server.domain.commands.CreateAccountCommand;
import com.dornu.hsd.server.domain.models.*;
import com.dornu.hsd.server.factory.ObjectFactory;
import com.dornu.hsd.security.models.HsdAccessTokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.dornu.hsd.server.helpers.Helper.tryGetHttpStatus;

@RestController
@RequestMapping("/api/v1")
public class AccountControllerV1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountControllerV1.class);

    @Autowired
    private ObjectFactory objectFactory;

    @Autowired
    private HsdAccessTokenGranter hsdAccessTokenGranter;

    @Autowired
    private CreateAccountCommand createAccountCommand;

    @Autowired
    private GetCustomerByIdCommand getCustomerByIdCommand;

    @Autowired
    private GetCreditCardByIdCommand getCreditCardByIdCommand;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<HsdAccessTokenModel> accessToken(@RequestHeader(value = "Authorization") String authorizationHeader) {
        return hsdAccessTokenGranter.generateAccessTokenResponse(authorizationHeader);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity<CreateAccountResponseModel> accounts(@RequestBody CreateAccountRequestModel requestModel) {
        LOGGER.info("CreateAccountResponseModel from {} -> {}", requestModel.getClientRequestId(), objectFactory.getGson().toJson(requestModel));

        CreateAccountResponseModel responseModel = createAccountCommand.execute(requestModel);

        LOGGER.info("CreateAccountResponseModel for clientRequestId {} -> {}", requestModel.getClientRequestId(), objectFactory.getGson().toJson(responseModel));

        return new ResponseEntity<CreateAccountResponseModel>(responseModel, tryGetHttpStatus(responseModel));
    }

    @RequestMapping(value = "/accounts/customer/{id}", method = RequestMethod.GET)
    public ResponseEntity<GetCustomerResponseModel> customer(@PathVariable("id") String customerId) {
        LOGGER.info("Get Customer by ID: {}", customerId);

        GetCustomerResponseModel responseModel = getCustomerByIdCommand.execute(customerId);

        LOGGER.info("GetCustomerResponseModel for customerId {} -> {}", customerId, objectFactory.getGson().toJson(responseModel));

        return new ResponseEntity<GetCustomerResponseModel>(responseModel, tryGetHttpStatus(responseModel));
    }

    @RequestMapping(value = "/accounts/creditCard/{id}", method = RequestMethod.GET)
    public ResponseEntity<GetCreditCardResponseModel> creditCard(@PathVariable("id") String creditCardId) {
        LOGGER.info("Get Credit Card by ID: {}", creditCardId);

        GetCreditCardResponseModel responseModel = getCreditCardByIdCommand.execute(creditCardId);

        LOGGER.info("GetCreditCardResponseModel for creditCardId {} -> {}", creditCardId, objectFactory.getGson().toJson(responseModel));

        return new ResponseEntity<GetCreditCardResponseModel>(responseModel, tryGetHttpStatus(responseModel));
    }
}
