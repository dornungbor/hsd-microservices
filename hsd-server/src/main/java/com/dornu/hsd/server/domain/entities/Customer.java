package com.dornu.hsd.server.domain.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Builder(builderClassName = "CustomerBuilder", toBuilder = true)
@JsonDeserialize(builder = Customer.CustomerBuilder.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Customer {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String phone;
    private String address;
    private String createdOn;
    private String lastModifiedOn;
    @JoinTable
    @OneToMany
    private List<CreditCard> creditCards;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CustomerBuilder {}
}
