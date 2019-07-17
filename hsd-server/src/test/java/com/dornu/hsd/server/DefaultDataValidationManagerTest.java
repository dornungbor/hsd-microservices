package com.dornu.hsd.server;

import com.dornu.hsd.server.domain.models.CreateAccountRequestModel;
import com.dornu.hsd.server.domain.models.CreditCardModel;
import com.dornu.hsd.server.domain.models.CustomerModel;
import com.dornu.hsd.server.domain.validation.DefaultDataValidationManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
public class DefaultDataValidationManagerTest {

    @TestConfiguration
    static class DefaultDataValidationManagerTestConfiguration {

        @Bean
        public DefaultDataValidationManager<CreateAccountRequestModel> defaultDataValidationManager() {
            return new DefaultDataValidationManager<CreateAccountRequestModel>();
        }
    }

    @Autowired
    private DefaultDataValidationManager<CreateAccountRequestModel> defaultDataValidationManager;

    @Test
    public void when_valid_then_return() throws ParseException {
        CreditCardModel creditCard = CreditCardModel.builder()
                .cardNumber("test")
                .cardVerificationValue("test")
                .cardExpiryMonth("test")
                .cardExpiryYear("test")
                .cardHolderName("test")
                .cardHolderEmail("test@test.test")
                .cardHolderPhone("test")
                .build();
        CustomerModel customer = CustomerModel.builder()
                .firstName("test")
                .lastName("test")
                .dateOfBirth(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").parse("2010-07-02T00:00:00Z"))
                .email("test@test.test")
                .phone("test")
                .address("test")
                .creditCards(Collections.singletonList(creditCard))
                .build();
        CreateAccountRequestModel requestModel = CreateAccountRequestModel.builder()
                .clientRequestId(UUID.randomUUID().toString())
                .customer(customer)
                .build();

        Set<String> violations = defaultDataValidationManager.validate(requestModel);

        assertThat(violations).isEmpty();
    }

    @Test
    public void when_invalid_then_expect_correct_violations_count() throws ParseException {
        CreditCardModel creditCard = CreditCardModel.builder()
                .cardNumber("test")
                .cardVerificationValue("test")
                .cardExpiryMonth("test")
                .cardExpiryYear("test")
                .cardHolderName("") // invalid
                .cardHolderEmail("test") // invalid
                .cardHolderPhone("test")
                .build();
        CustomerModel customer = CustomerModel.builder()
                .firstName("") // invalid
                .lastName("test")
                .dateOfBirth(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").parse("2010-07-02T00:00:00Z"))
                .email("test@test.test")
                .phone("test")
                .address("test")
                .creditCards(Collections.singletonList(creditCard))
                .build();
        CreateAccountRequestModel requestModel = CreateAccountRequestModel.builder()
                .clientRequestId(UUID.randomUUID().toString())
                .customer(customer)
                .build();

        Set<String> violations = defaultDataValidationManager.validate(requestModel);

        assertThat(violations).isNotEmpty().size().isEqualTo(3);
    }
}
