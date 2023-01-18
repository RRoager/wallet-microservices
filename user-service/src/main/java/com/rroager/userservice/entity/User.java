package com.rroager.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID walletId;
    private String firstName;
    private String lastName;
    @Column(unique=true)
    private String email;
    private String password;
    private Date dateOfBirth;
    private String phoneNumber;
    private String zipCode;
    private String city;
    private String address;
    private String country;

    public User(UUID walletId, String firstName, String lastName, String email, String password, Date dateOfBirth, String phoneNumber, String zipCode, String city, String address, String country) {
        this.walletId = walletId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
        this.country = country;
    }
}
