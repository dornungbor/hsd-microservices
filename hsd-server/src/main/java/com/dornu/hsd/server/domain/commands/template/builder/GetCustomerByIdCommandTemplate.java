package com.dornu.hsd.server.domain.commands.template.builder;

import com.dornu.hsd.server.domain.commands.GetCustomerByIdCommand;
import com.dornu.hsd.server.domain.entities.Customer;
import com.dornu.hsd.server.domain.models.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class GetCustomerByIdCommandTemplate {

    private final GetCustomerByIdCommand getCustomerByIdCommand;
    private final String customerId;
    private ResponseEntity<HsdProcessorAccountResponse> responseEntity;
    private Optional<Customer> customerEntity;
    private GetCustomerResponseModel responseModel;

    private GetCustomerByIdCommandTemplate(
            GetCustomerByIdCommand getCustomerByIdCommand,
            String customerId
    ) {
        this.getCustomerByIdCommand = getCustomerByIdCommand;
        this.customerId = customerId;
    }

    public static GetCustomerByIdCommandTemplate templateBuilder(
            GetCustomerByIdCommand getCustomerByIdCommand,
            String customerId
    ) {
        return new GetCustomerByIdCommandTemplate(getCustomerByIdCommand, customerId);
    }

    public GetCustomerByIdCommandTemplate getCustomerById() {
        customerEntity = getCustomerByIdCommand.getCustomerById(customerId);
        return this;
    }

    public GetCustomerByIdCommandTemplate ifExistThenBuildSuccessResponse() {
        customerEntity.ifPresent(customer -> responseModel = getCustomerByIdCommand.ifExistThenBuildSuccessResponse(customerId, customer));
        return this;
    }

    public GetCustomerByIdCommandTemplate orElseBuildErrorResponse() {
        if (!customerEntity.isPresent()) {
            responseModel = getCustomerByIdCommand.orElseBuildErrorResponse(customerId);
        }
        return this;
    }

    public GetCustomerResponseModel resolve() {
        return responseModel;
    }
}
