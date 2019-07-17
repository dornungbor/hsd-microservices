package com.dornu.hsd.server.domain.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
public class CreateAccountRequestModel extends AbstractCoomandRequest {

    @Valid
    private CustomerModel customer;

    @Builder
    public CreateAccountRequestModel(
            String clientRequestId,
            CustomerModel customer
    ){
        super(clientRequestId);
        this.customer = customer;
    }
}
