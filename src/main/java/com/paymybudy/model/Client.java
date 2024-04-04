package com.paymybudy.model;

import jakarta.persistence.*;

@Entity
@Table(name="client")
public class Client {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="CLIENT_ID")
    int client_id;
    @Column(name="FIRSTNAME")
    String firstName;
    @Column(name="LASTNAME")
    String lastName;
    @Column(name="EMAIL")
    String email;
    @Column(name="PASSWORD")
    String password;

    public Client() {    }
    public Client(int client_id, String firstName, String lastName, String email, String password) {
        this.client_id = client_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
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
}
