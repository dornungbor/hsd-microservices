package com.dornu.hsd.server.domain.models;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetCreditCardResponseModel extends AbstractCoomandResponse {

    private CreditCardModel creditCard;

    @Builder
    public GetCreditCardResponseModel(
            CreditCardModel creditCard,
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
        this.creditCard = creditCard;
    }
}
