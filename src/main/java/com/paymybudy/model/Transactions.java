package com.paymybudy.model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.ManyToAny;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="transactions")
@Transactional
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private int transactionId;
    @Column(name = "CLIENT_ID")
    private int clientId;
    @Column(name = "BENEFICIARY_ID")
    private int beneficiaryId;
    @Column(name = "AMOUNT")
    private float amount;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "DATE")
    private Date date;
    @Column(name = "STATUS")
    private int status;
    @Column(name = "beneficiary_firstname") //the name of the joined column needs to be included in the model
    private String beneficiaryName;

    public Transactions() {
    }

    public Transactions(int transactionId, int clientId, int beneficiaryId, float amount, String description, Date date, int status, String beneficiaryName) {
        this.transactionId = transactionId;
        this.clientId = clientId;
        this.beneficiaryId = beneficiaryId;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.status = status;
        this.beneficiaryName = beneficiaryName;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(int beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }
}

