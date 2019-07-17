package com.dornu.hsd.server.domain.validation;

import com.dornu.hsd.server.domain.dao.CustomerRepository;
import com.dornu.hsd.server.domain.entities.Customer;
import com.dornu.hsd.server.domain.models.CreateAccountRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class AccountValidationManager implements ValidationManager<CreateAccountRequestModel> {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Set<String> validate(CreateAccountRequestModel requestModel) {
        Customer customer = customerRepository.findByEmail(requestModel.getCustomer().getEmail());
        if (customer == null) return Collections.emptySet();
        return Collections.singleton("customer.email: Duplicate Customer email found");
    }
}
