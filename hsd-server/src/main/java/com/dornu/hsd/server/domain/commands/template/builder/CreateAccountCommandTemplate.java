package com.dornu.hsd.server.domain.commands.template.builder;

import com.dornu.hsd.server.domain.cache.AccessTokenCacheManager;
import com.dornu.hsd.server.domain.commands.CreateAccountCommand;
import com.dornu.hsd.server.domain.entities.CreditCard;
import com.dornu.hsd.server.domain.entities.Customer;
import com.dornu.hsd.server.domain.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

public class CreateAccountCommandTemplate {

    private final CreateAccountCommand createAccountCommand;
    private final CreateAccountRequestModel requestModel;
    private final AccessTokenCacheManager accessTokenCacheManager;
    private String createdOn;
    private List<CreditCard> encryptedCreditCardEntities;
    private Customer customer;
    private Customer savedCustomer;
    private String accessToken;
    private String requestUrl;
    private HsdProcessorAccountRequest hsdProcessorAccountRequest;
    private ResponseEntity<HsdProcessorAccountResponse> responseEntity;
    private boolean abort = false;

    private CreateAccountCommandTemplate(
            CreateAccountCommand createAccountCommand,
            CreateAccountRequestModel requestModel,
            AccessTokenCacheManager accessTokenCacheManager
    ) {
        this.createAccountCommand = createAccountCommand;
        this.requestModel = requestModel;
        this.accessTokenCacheManager = accessTokenCacheManager;
    }

    public static CreateAccountCommandTemplate templateBuilder(
            CreateAccountCommand createAccountCommand,
            CreateAccountRequestModel requestModel,
            AccessTokenCacheManager accessTokenCacheManager
            ) {
        return new CreateAccountCommandTemplate(createAccountCommand, requestModel, accessTokenCacheManager);
    }

    public CreateAccountCommandTemplate getAccessToken(){
        Optional<HsdProcessorAccessTokenResponse> optionalOfAccessToken = accessTokenCacheManager.getAccessTokenFromCache();
        accessToken = optionalOfAccessToken.isPresent() ? optionalOfAccessToken.get().getAccess_token() : createAccountCommand.getAccessToken();
        return this;
    }

    public CreateAccountCommandTemplate lookupHsdProcessorUrl(){
        if (!StringUtils.isEmpty(accessToken)) {
            requestUrl = createAccountCommand.lookupHsdProcessorUrl();
        } else abort = true;
        return this;
    }

    public CreateAccountCommandTemplate setCreatedOnDate(){
        if (!abort) createdOn = createAccountCommand.setCreatedOnDate();
        return this;
    }

    public CreateAccountCommandTemplate encryptCreditCards(){
        if (!abort) encryptedCreditCardEntities = createAccountCommand.encryptCreditCards(requestModel, createdOn);
        return this;
    }

    public CreateAccountCommandTemplate saveEncryptedCreditCards(){
        if (!abort && encryptedCreditCardEntities != null && !encryptedCreditCardEntities.isEmpty()) {
            createAccountCommand.saveEncryptedCreditCards(requestModel, encryptedCreditCardEntities);
        }
        return this;
    }

    public CreateAccountCommandTemplate prepareCustomerEntity(){
        if (!abort) customer = createAccountCommand.prepareCustomerEntity(requestModel, createdOn, encryptedCreditCardEntities);
        return this;
    }

    public CreateAccountCommandTemplate saveCustomerEntity(){
        if (!abort) savedCustomer = createAccountCommand.saveCustomerEntity(requestModel, customer);
        return this;
    }

    public CreateAccountCommandTemplate prepareProcessorAccountRequest(){
        if (!abort) hsdProcessorAccountRequest = createAccountCommand.prepareProcessorAccountRequest(savedCustomer);
        return this;
    }

    public CreateAccountCommandTemplate callHsdProcessorService(){
        if (!abort) responseEntity = createAccountCommand.callHsdProcessorService(accessToken, requestUrl, hsdProcessorAccountRequest);
        return this;
    }

    public CreateAccountResponseModel buildCommandResponse(){
        if (abort || responseEntity == null) {
            ErrorDetails error = ErrorDetails.builder()
                    .code(ErrorCode.SERVICE_UNAVAILABLE)
                    .message("We couldn't reach the HSD Processor Service, so ummm.... we have to abort :(")
                    .build();

            return CreateAccountResponseModel.builder()
                    .status(Status.FAILURE)
                    .clientRequestId(requestModel.getClientRequestId())
                    .error(error)
                    .build();
        }

        return createAccountCommand.buildCommandResponse(
                requestModel,
                createdOn,
                encryptedCreditCardEntities,
                savedCustomer,
                responseEntity
        );
    }
}
