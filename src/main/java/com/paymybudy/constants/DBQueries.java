package com.paymybudy.constants;


//TODO--To review if this approach can be substituted with Spring JPA
//Comment: This approach can be partially substitued by Spring JPA.
public class DBQueries {
    //CREATE
    public static final String INSERT_CLIENT  = "INSERT INTO CLIENT (FIRSTNAME, LASTNAME, EMAIL, PASSWORD) VALUES (?, ?, ?, ?)";
    public static final String INSERT_ACCOUNT  = "INSERT INTO ACCOUNTS (CLIENT_ID, BALANCE) VALUES (?, ?)";
    public static final String INSERT_TRANSACTION  = "INSERT INTO TRANSACTIONS (CLIENT_ID, BENEFICIARY_ID, AMOUNT, DESCRIPTION, DATE, STATUS) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String INSERT_BENEFICIARY  = "INSERT INTO BENEFICIARIES (CLIENT_ID, BENEFICIARY_FIRSTNAME, BENEFICIARY_LASTNAME) VALUES (?, ?, ?)";

    //READ
    public static final String SELECT_CLIENT = "SELECT * FROM CLIENT WHERE EMAIL = ?";

    public static final String SELECT_ACCOUNT = "SELECT * FROM ACCOUNTS WHERE CLIENT_ID = ?";

    public static final String SELECT_TRANSACTION = "SELECT * FROM TRANSACTIONS WHERE CLIENT_ID = ?"; //retrieves all the transaction of the client

    public static final String SELECT_CLIENT_BENEFICIARIES = "SELECT * FROM BENEFICIARY WHERE CLIENT_ID = ?"; //retrieves all the beneficiary of the client

    public static final String SELECT_BENEFICIARY = "SELECT * FROM BENEFICIARY WHERE BENEFICIARY_ID = ?"; //retrieves all matches for the beneficiary id (a beneficiary can have more than host account)

    //UPDATE
    public static final String UPDATE_CLIENT = "UPDATE CLIENT SET FIRSTNAME = ?, LASTNAME = ?, EMAIL = ?, PASSWORD = ? WHERE CLIENT_ID = ?";
    public static final String UPDATE_ACCOUNT = "UPDATE ACCOUNTS SET BALANCE = ? WHERE CLIENT_ID = ?";
    public static final String UPDATE_TRANSACTION = "UPDATE TRANSACTIONS SET CLIENT_ID = ?, BENEFICIARY_ID = ?, AMOUNT = ?, DESCRIPTION = ?, DATE = ?, STATUS = ? WHERE TRANSACTION_ID = ?";
    public static final String UPDATE_BENEFICIARY = "UPDATE BENEFICIARY SET CLIENT_ID = ?, BENEFICIARY_FIRSTNAME = ?, BENEFICIARY_LASTNAME = ? WHERE CLIENT_ID = ?";

    //DELETE
    public static final String DELETE_CLIENT = "DELETE FROM CLIENT WHERE EMAIL = ?"; //delete the client's associated to the email. If a client has 2 different email account one will persist.
    public static final String DELETE_ACCOUNT = "DELETE FROM CLIENT WHERE ACCOUNT_ID = ?"; //delete only the account of the client
    public static final String DELETE_TRANSACTION = "DELETE FROM TRANSACTIONS WHERE TRANSACTION_ID = ?";
    public static final String DELETE_BENEFICIARY = "SELECT * FROM CLIENT WHERE BENEFICIARY_ID = ?"; //the beneficiary is linked to a client, so if the client is deleted the beneficiary also is removed.

}
