package com.dornu.hsd.server.domain.commands;

import com.dornu.hsd.server.HsdCrypto;
import com.dornu.hsd.server.domain.cache.AccessTokenCacheManager;
import com.dornu.hsd.server.domain.commands.template.builder.CreateAccountCommandTemplate;
import com.dornu.hsd.server.domain.dao.CreditCardRepository;
import com.dornu.hsd.server.domain.dao.CustomerRepository;
import com.dornu.hsd.server.domain.entities.CreditCard;
import com.dornu.hsd.server.domain.entities.Customer;
import com.dornu.hsd.server.domain.models.*;
import com.dornu.hsd.server.domain.models.mappers.CreditCardMapper;
import com.dornu.hsd.server.domain.models.mappers.CustomerMapper;
import com.dornu.hsd.server.domain.validation.AccountValidationManager;
import com.dornu.hsd.server.domain.validation.DefaultDataValidationManager;
import com.dornu.hsd.server.factory.ObjectFactory;
import com.dornu.hsd.server.helpers.Helper;
import com.dornu.hsd.server.helpers.RestTemplateParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.dornu.hsd.server.domain.constants.Constants.DATE_TIME_FORMATTER;
import static com.dornu.hsd.server.helpers.RestTemplateHelper.executeHttpRequest;

@Service
public class CreateAccountCommand extends AbstractCommand<CreateAccountRequestModel, CreateAccountResponseModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateAccountCommand.class);

    @Autowired
    private ObjectFactory objectFactory;

    @Autowired
    private HsdCrypto crypto;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private HsdProcessorAccessTokenCommand hsdProcessorAccessTokenCommand;

    @Autowired
    private DefaultDataValidationManager<CreateAccountRequestModel> dataValidationManager;

    @Autowired
    private AccountValidationManager businessValidationManager;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccessTokenCacheManager accessTokenCacheManager;

    private final String salt = KeyGenerators.string().generateKey(), password = "password";

    @Override
    public Set<String> doDataValidation(CreateAccountRequestModel requestModel) {
        return dataValidationManager.validate(requestModel);
    }

    @Override
    public Set<String> doBusinessValidation(CreateAccountRequestModel requestModel) {
        return businessValidationManager.validate(requestModel);
    }

    @Override
    public CreateAccountResponseModel handle(CreateAccountRequestModel requestModel) {
        return CreateAccountCommandTemplate.templateBuilder(this, requestModel, accessTokenCacheManager)
                .getAccessToken()
                .lookupHsdProcessorUrl()
                .setCreatedOnDate()
                .encryptCreditCards()
                .saveEncryptedCreditCards()
                .prepareCustomerEntity()
                .saveCustomerEntity()
                .prepareProcessorAccountRequest()
                .callHsdProcessorService()
                .buildCommandResponse();
    }

    @Override
    public void preHandle(CreateAccountRequestModel requestModel) {

    }

    @Override
    public void postHandle(CreateAccountRequestModel requestModel, CreateAccountResponseModel responseModel) {

    }

    @Override
    public CreateAccountResponseModel handleError(CreateAccountRequestModel requestModel, CreateAccountResponseModel responseModel, Exception exception) {
        return null;
    }

    @Override
    public CreateAccountResponseModel handleValidationError(CreateAccountRequestModel requestModel, Set<String> violations) {
        ErrorDetails error = ErrorDetails.builder()
                .code(ErrorCode.INVALID_PARAMETERS)
                .message(ErrorCode.INVALID_PARAMETERS.getDefaultMessage())
                .violations(violations.stream().map(x -> x).toArray(String[]::new))
                .build();

        return CreateAccountResponseModel.builder()
                .status(Status.FAILURE)
                .clientRequestId(requestModel.getClientRequestId())
                .error(error)
                .build();
    }

    public String setCreatedOnDate() {
        return LocalDateTime.now(ZoneOffset.UTC).format(DATE_TIME_FORMATTER);
    }

    public List<CreditCard> encryptCreditCards(CreateAccountRequestModel requestModel, String createdOn) {
        List<CreditCard> encryptedCreditCardEntities = new ArrayList<>();
        requestModel.getCustomer().getCreditCards().forEach(creditCardModel -> {
            String creditCardJson = objectFactory.getGson().toJson(creditCardModel);
            String encryptedCardDetails = crypto.getTextEncryptor(password, salt).encrypt(creditCardJson);
            LOGGER.info("encryptedCardDetails: {}", objectFactory.getGson().toJson(encryptedCardDetails));
            CreditCard encryptedCreditCard = CreditCardMapper.toCreditCardEntity(encryptedCardDetails, salt, UUID.randomUUID().toString(), createdOn);
            encryptedCreditCardEntities.add(encryptedCreditCard);
        });
        return encryptedCreditCardEntities;
    }

    public void saveEncryptedCreditCards(CreateAccountRequestModel requestModel, List<CreditCard> encryptedCreditCardEntities) {
        encryptedCreditCardEntities.forEach(encryptedCreditCard->{
            CreditCard savedCreditCard = creditCardRepository.save(encryptedCreditCard);
            LOGGER.info("encrypted savedCreditCard for clientRequestId {} -> {}", requestModel.getClientRequestId(), objectFactory.getGson().toJson(savedCreditCard));
        });
    }

    public Customer prepareCustomerEntity(CreateAccountRequestModel requestModel, String createdOn, List<CreditCard> encryptedCreditCardEntities) {
        Customer customer = CustomerMapper.toCustomerEntity(requestModel.getCustomer(), UUID.randomUUID().toString(), createdOn);
        customer.setCreditCards(encryptedCreditCardEntities);
        return customer;
    }

    public Customer saveCustomerEntity(CreateAccountRequestModel requestModel, Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        LOGGER.info("savedCustomer for clientRequestId {} -> {}", requestModel.getClientRequestId(), objectFactory.getGson().toJson(savedCustomer));
        return savedCustomer;
    }

    public String getAccessToken() {
        HsdProcessorAccessTokenResponse tokenResponse = hsdProcessorAccessTokenCommand.execute(new HsdProcessorAccessTokenRequest("dornu", "dornu"));
        return tokenResponse.getAccess_token();
    }

    public String lookupHsdProcessorUrl() {
        List<ServiceInstance> instances = this.discoveryClient.getInstances("hsd-processor");
        if (instances == null || instances.isEmpty()) return "";
        return "http://" + instances.get(0).getHost() + ":" + instances.get(0).getPort() + "/api/v1/accounts";
    }

    public HsdProcessorAccountRequest prepareProcessorAccountRequest(Customer savedCustomer) {
        return CustomerMapper.toHsdProcessorAccountRequest(savedCustomer);
    }

    public ResponseEntity<HsdProcessorAccountResponse> callHsdProcessorService(String accessToken, String requestUrl, HsdProcessorAccountRequest hsdProcessorAccountRequest) {
        LOGGER.info("sending HsdProcessorAccountRequest for customer {} -> {}", hsdProcessorAccountRequest.getEmail(), objectFactory.getGson().toJson(hsdProcessorAccountRequest));
        HttpHeaders requestHeaders = Helper.getRequestHeaders(accessToken);
        RestTemplateParameters<HsdProcessorAccountRequest, HsdProcessorAccountResponse> restTemplateParameters =
                RestTemplateParameters
                        .<HsdProcessorAccountRequest, HsdProcessorAccountResponse>builder()
                        .restTemplate(restTemplate)
                        .requestUrl(requestUrl)
                        .httpEntity(Helper.createHeaderAndPayloadRequestEntity(hsdProcessorAccountRequest, requestHeaders))
                        .responseType(HsdProcessorAccountResponse.class)
                        .httpMethod(HttpMethod.POST)
                        .build();
        return executeHttpRequest(restTemplateParameters);
    }

    public CreateAccountResponseModel buildCommandResponse(CreateAccountRequestModel requestModel, String createdOn, List<CreditCard> encryptedCreditCardEntities, Customer savedCustomer, ResponseEntity<HsdProcessorAccountResponse> responseEntity) {
        if (responseEntity == null || responseEntity.getBody() == null || responseEntity.getBody().getStatus() != Status.SUCCESS) {
            ErrorDetails error = ErrorDetails.builder()
                    .code(ErrorCode.SOMETHING_WENT_WRONG)
                    .message("This is weird!!! HSD Processor Service isn't doing anything fancy, so why is this???")
                    .build();

            return CreateAccountResponseModel.builder()
                    .status(Status.FAILURE)
                    .error(error)
                    .build();
        }

        return CreateAccountResponseModel.builder()
                .status(Status.SUCCESS)
                .customerId(savedCustomer.getId())
                .creditCardIds(encryptedCreditCardEntities.stream().map(CreditCard::getId).collect(Collectors.toSet()))
                .clientRequestId(requestModel.getClientRequestId())
                .hsdResponseId(UUID.randomUUID().toString())
                .processedAt(createdOn)
                .processorResponse(responseEntity.getBody())
                .build();
    }
}


