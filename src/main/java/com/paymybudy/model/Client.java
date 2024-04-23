package com.paymybudy.model;

import jakarta.persistence.*;

@Entity
@Table(name="client")
public class Client {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="CLIENT_ID")
    int clientId;
    @Column(name="FIRSTNAME")
    String firstName;
    @Column(name="LASTNAME")
    String lastName;
    @Column(name="EMAIL", unique = true)
    String email;
    @Column(name="PASSWORD")
    String password;
    @Column(name="ROLE")
    String role;

    public Client() {
    }

    public Client(int clientId, String firstName, String lastName, String email, String password, String role) {
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
