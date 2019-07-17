package com.dornu.hsd.server.domain.models;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
public class CreateAccountResponseModel extends AbstractCoomandResponse {

    private String customerId;
    private Set<String> creditCardIds;
    private HsdProcessorAccountResponse processorResponse;

    @Builder
    public CreateAccountResponseModel(
            String customerId,
            Set<String> creditCardIds,
            Status status,
            String clientRequestId,
            String hsdResponseId,
            ErrorDetails error,
            String processedAt,
            HsdProcessorAccountResponse processorResponse
    ){
        super(
                status, clientRequestId,
                hsdResponseId, error, processedAt
        );
        this.customerId = customerId;
        this.creditCardIds = creditCardIds;
        this.processorResponse = processorResponse;
    }
}
