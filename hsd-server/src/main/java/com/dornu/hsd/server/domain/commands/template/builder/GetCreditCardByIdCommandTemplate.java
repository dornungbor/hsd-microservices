package com.dornu.hsd.server.domain.commands.template.builder;

import com.dornu.hsd.server.domain.commands.GetCreditCardByIdCommand;
import com.dornu.hsd.server.domain.entities.CreditCard;
import com.dornu.hsd.server.domain.entities.Customer;
import com.dornu.hsd.server.domain.models.GetCreditCardResponseModel;
import com.dornu.hsd.server.domain.models.GetCustomerResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class GetCreditCardByIdCommandTemplate {

    private final GetCreditCardByIdCommand getCustomerByIdCommand;
    private final String creditCardId;
    private ResponseEntity<GetCreditCardResponseModel> responseEntity;
    private Optional<CreditCard> creditCardEntity;
    private GetCreditCardResponseModel responseModel;

    private GetCreditCardByIdCommandTemplate(
            GetCreditCardByIdCommand getCustomerByIdCommand,
            String creditCardId
    ) {
        this.getCustomerByIdCommand = getCustomerByIdCommand;
        this.creditCardId = creditCardId;
    }

    public static GetCreditCardByIdCommandTemplate templateBuilder(
            GetCreditCardByIdCommand getCustomerByIdCommand,
            String creditCardId
    ) {
        return new GetCreditCardByIdCommandTemplate(getCustomerByIdCommand, creditCardId);
    }

    public GetCreditCardByIdCommandTemplate getCreditCardById() {
        creditCardEntity = getCustomerByIdCommand.getCreditCardById(creditCardId);
        return this;
    }

    public GetCreditCardByIdCommandTemplate ifExistThenBuildSuccessResponse() {
        creditCardEntity.ifPresent(creditCard -> responseModel = getCustomerByIdCommand.ifExistThenBuildSuccessResponse(creditCardId, creditCard));
        return this;
    }

    public GetCreditCardByIdCommandTemplate orElseBuildErrorResponse() {
        if (!creditCardEntity.isPresent()) {
            responseModel = getCustomerByIdCommand.orElseBuildErrorResponse(creditCardId);
        }
        return this;
    }

    public GetCreditCardResponseModel resolve() {
        return responseModel;
    }
}
