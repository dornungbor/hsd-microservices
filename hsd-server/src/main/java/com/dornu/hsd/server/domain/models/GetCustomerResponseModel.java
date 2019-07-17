package com.dornu.hsd.server.domain.models;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetCustomerResponseModel extends AbstractCoomandResponse {

    private CustomerModel customer;

    @Builder
    public GetCustomerResponseModel(
            CustomerModel customer,
            Status status,
            String clientRequestId,
            String hsdResponseId,
            ErrorDetails error,
            String processedAt
    ){
        super(
                status, clientRequestId,
                hsdResponseId, error, processedAt
        );
        this.customer = customer;
    }
}
