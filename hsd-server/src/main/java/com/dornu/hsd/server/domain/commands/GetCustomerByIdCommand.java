package com.dornu.hsd.server.domain.commands;

import com.dornu.hsd.server.domain.commands.template.builder.GetCustomerByIdCommandTemplate;
import com.dornu.hsd.server.domain.dao.CustomerRepository;
import com.dornu.hsd.server.domain.entities.Customer;
import com.dornu.hsd.server.domain.models.*;
import com.dornu.hsd.server.domain.models.mappers.CustomerMapper;
import com.dornu.hsd.server.domain.validation.DefaultDataValidationManager;
import com.dornu.hsd.server.factory.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.dornu.hsd.server.domain.constants.Constants.DATE_TIME_FORMATTER;

@Service
public class GetCustomerByIdCommand extends AbstractCommand<String, GetCustomerResponseModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCustomerByIdCommand.class);

    @Autowired
    private ObjectFactory objectFactory;

    @Autowired
    private DefaultDataValidationManager<String> validationManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Set<String> doDataValidation(String requestModel) {
        return validationManager.validate(requestModel);
    }

    @Override
    public Set<String> doBusinessValidation(String requestModel) {
        return Collections.emptySet();
    }

    @Override
    public GetCustomerResponseModel handle(String customerId) {
        return GetCustomerByIdCommandTemplate.templateBuilder(this, customerId)
                .getCustomerById()
                .ifExistThenBuildSuccessResponse()
                .orElseBuildErrorResponse()
                .resolve();
    }

    @Override
    public void preHandle(String customerId) {

    }

    @Override
    public void postHandle(String customerId, GetCustomerResponseModel responseModel) {

    }

    @Override
    public GetCustomerResponseModel handleError(String customerId, GetCustomerResponseModel responseModel, Exception exception) {
        return getCustomerErrorResponse(customerId);
    }

    @Override
    public GetCustomerResponseModel handleValidationError(String customerId, Set<String> violations) {
        return getCustomerErrorResponse(customerId);
    }

    public Optional<Customer> getCustomerById(String customerId) {
        return customerRepository.findById(customerId);
    }

    public GetCustomerResponseModel ifExistThenBuildSuccessResponse(String customerId, Customer customerEntity) {
        CustomerModel customerModel = CustomerMapper.toCustomerModel(customerEntity);
        GetCustomerResponseModel responseModel = GetCustomerResponseModel.builder()
                .customer(customerModel)
                .status(Status.SUCCESS)
                .hsdResponseId(UUID.randomUUID().toString())
                .processedAt(LocalDateTime.now(ZoneOffset.UTC).format(DATE_TIME_FORMATTER))
                .build();
        LOGGER.info("GetCustomerResponseModel for clientRequestId {} -> {}", customerId, objectFactory.getGson().toJson(customerModel));
        return responseModel;
    }

    public GetCustomerResponseModel orElseBuildErrorResponse(String customerId) {
        return getCustomerErrorResponse(customerId);
    }

    private GetCustomerResponseModel getCustomerErrorResponse(String customerId) {
        LOGGER.info("Customer with given ID {} does NOT exist", customerId);
        ErrorDetails error = ErrorDetails.builder()
                .code(ErrorCode.INVALID_CUSTOMER_ID)
                .message(ErrorCode.INVALID_CUSTOMER_ID.getDefaultMessage())
                .build();

        return GetCustomerResponseModel.builder()
                .status(Status.FAILURE)
                .error(error)
                .processedAt(LocalDateTime.now(ZoneOffset.UTC).format(DATE_TIME_FORMATTER))
                .build();
    }
}


