package com.dornu.hsd.server.domain.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.util.Date;
import java.util.List;

@Builder(builderClassName = "CustomerBuilder", toBuilder = true)
@JsonDeserialize(builder = CustomerModel.CustomerBuilder.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomerModel {

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @Past
    private Date dateOfBirth;
    @Email
    private String email;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String address;
    @NotEmpty
    @Valid
    private List<CreditCardModel> creditCards;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CustomerBuilder {}
}
