package com.dornu.hsd.server.domain.models.mappers;

import com.dornu.hsd.server.domain.entities.CreditCard;
import com.dornu.hsd.server.domain.models.CreditCardModel;
import com.dornu.hsd.server.domain.models.EncryptedCreditCard;
import com.dornu.hsd.server.factory.ObjectFactory;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.util.List;
import java.util.stream.Collectors;

public class CreditCardMapper {

    public static CreditCard toCreditCardEntity(String encryptedCardDetails, String salt, String creditCardId, String createdOn) {
        return CreditCard.builder()
                .id(creditCardId)
                .encryptedCardDetails(encryptedCardDetails)
                .salt(salt)
                .createdOn(createdOn)
                .build();
    }

    public static List<CreditCardModel> toCreditCardModel(List<CreditCard> creditCards) throws IllegalStateException{
        return creditCards.stream().map(x->{
            String decryptedCardDetails = ObjectFactory.getInstance()
                    .getCrypto()
                    .getTextEncryptor("password", x.getSalt())
                    .decrypt(x.getEncryptedCardDetails());
            return ObjectFactory.getInstance().getGson().fromJson(
                    decryptedCardDetails, CreditCardModel.class
            );
        }).collect(Collectors.toList());
    }

    public static List<EncryptedCreditCard> toEncryptedCreditCard(List<CreditCard> creditCards) {
        return creditCards.stream().map(x->{
            return EncryptedCreditCard.builder()
                    .id(x.getId())
                    .encryptedCardDetails(x.getEncryptedCardDetails())
                    .salt(x.getSalt())
                    .createdOn(x.getCreatedOn())
                    .lastModifiedOn(x.getLastModifiedOn())
                    .build();
        }).collect(Collectors.toList());
    }
}
