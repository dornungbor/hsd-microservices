package com.dornu.hsd.server.domain.commands;

import com.dornu.hsd.server.HsdCrypto;
import com.dornu.hsd.server.domain.commands.template.builder.GetCreditCardByIdCommandTemplate;
import com.dornu.hsd.server.domain.dao.CreditCardRepository;
import com.dornu.hsd.server.domain.entities.CreditCard;
import com.dornu.hsd.server.domain.models.*;
import com.dornu.hsd.server.domain.validation.DefaultDataValidationManager;
import com.dornu.hsd.server.factory.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.dornu.hsd.server.domain.constants.Constants.DATE_TIME_FORMATTER;

@Service
public class GetCreditCardByIdCommand extends AbstractCommand<String, GetCreditCardResponseModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCreditCardByIdCommand.class);

    @Autowired
    private ObjectFactory objectFactory;

    @Autowired
    private HsdCrypto crypto;

    @Autowired
    private DefaultDataValidationManager<String> validationManager;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Override
    public Set<String> doDataValidation(String requestModel) {
        return validationManager.validate(requestModel);
    }

    @Override
    public Set<String> doBusinessValidation(String requestModel) {
        return Collections.emptySet();
    }

    @Override
    public GetCreditCardResponseModel handle(String creditCardId) {
        return GetCreditCardByIdCommandTemplate.templateBuilder(this, creditCardId)
                .getCreditCardById()
                .ifExistThenBuildSuccessResponse()
                .orElseBuildErrorResponse()
                .resolve();
    }

    @Override
    public void preHandle(String creditCardId) {

    }

    @Override
    public void postHandle(String creditCardId, GetCreditCardResponseModel responseModel) {

    }

    @Override
    public GetCreditCardResponseModel handleError(String creditCardId, GetCreditCardResponseModel responseModel, Exception exception) {
        return getCreditCardErrorResponse(creditCardId);
    }

    @Override
    public GetCreditCardResponseModel handleValidationError(String creditCardId, Set<String> violations) {
        return getCreditCardErrorResponse(creditCardId);
    }

    public Optional<CreditCard> getCreditCardById(String creditCardId) {
        return creditCardRepository.findById(creditCardId);
    }

    public GetCreditCardResponseModel ifExistThenBuildSuccessResponse(String creditCardId, CreditCard creditCardEntity) throws IllegalStateException {
        String decryptedCardDetails = crypto.getTextEncryptor("password", creditCardEntity.getSalt())
                .decrypt(creditCardEntity.getEncryptedCardDetails());
        CreditCardModel creditCardModel = objectFactory.getGson().fromJson(
                decryptedCardDetails, CreditCardModel.class
        );
        GetCreditCardResponseModel responseModel = GetCreditCardResponseModel.builder()
                .creditCard(creditCardModel)
                .status(Status.SUCCESS)
                .hsdResponseId(UUID.randomUUID().toString())
                .processedAt(LocalDateTime.now(ZoneOffset.UTC).format(DATE_TIME_FORMATTER))
                .build();
        LOGGER.info("GetCustomerResponseModel for creditCardId {} -> {}", creditCardId, objectFactory.getGson().toJson(responseModel));
        return responseModel;

    }

    public GetCreditCardResponseModel orElseBuildErrorResponse(String creditCardId) {
        return getCreditCardErrorResponse(creditCardId);
    }

    private GetCreditCardResponseModel getCreditCardErrorResponse(String creditCardId) {
        LOGGER.info("Credit Card with given ID {} does NOT exist", creditCardId);
        ErrorDetails error = ErrorDetails.builder()
                .code(ErrorCode.INVALID_CREDIT_CARD_ID)
                .message(ErrorCode.INVALID_CREDIT_CARD_ID.getDefaultMessage())
                .build();
        return GetCreditCardResponseModel.builder()
                .status(Status.FAILURE)
                .error(error)
                .processedAt(LocalDateTime.now(ZoneOffset.UTC).format(DATE_TIME_FORMATTER))
                .build();
    }
}



