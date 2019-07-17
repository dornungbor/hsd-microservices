package com.dornu.hsd.server.domain.models.mappers;

import com.dornu.hsd.server.domain.entities.Customer;
import com.dornu.hsd.server.domain.models.CustomerModel;
import com.dornu.hsd.server.domain.models.HsdProcessorAccountRequest;

import java.text.SimpleDateFormat;

public class CustomerMapper {
    public static Customer toCustomerEntity(CustomerModel customerModel, String customerId, String createdOn) {
        return Customer.builder()
                .id(customerId)
                .firstName(customerModel.getFirstName())
                .lastName(customerModel.getLastName())
                .dateOfBirth(customerModel.getDateOfBirth())
                .email(customerModel.getEmail())
                .phone(customerModel.getPhone())
                .address(customerModel.getAddress())
                .createdOn(createdOn)
                .build();
    }

    public static CustomerModel toCustomerModel(Customer customer) {
        return CustomerModel.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dateOfBirth(customer.getDateOfBirth())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .creditCards(CreditCardMapper.toCreditCardModel(customer.getCreditCards()))
                .build();
    }

    public static HsdProcessorAccountRequest toHsdProcessorAccountRequest(Customer customer) {
        return HsdProcessorAccountRequest.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dateOfBirth(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").format(customer.getDateOfBirth()))
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .creditCards(CreditCardMapper.toEncryptedCreditCard(customer.getCreditCards()))
                .build();
    }
}
