package com.paymybudy.repository;

import org.springframework.stereotype.Component;

@Component
public class RegistrationAddFormDTO {
    String firstName;
    String lastName;
    String email;
    String password;
    String iban;
    String swift;

    public RegistrationAddFormDTO() {
    }

    public RegistrationAddFormDTO(String firstName, String lastName, String email, String password, String iban, String swift) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.iban = iban;
        this.swift = swift;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }
}
