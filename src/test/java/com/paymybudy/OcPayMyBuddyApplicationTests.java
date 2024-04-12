package com.paymybudy;

import com.paymybudy.controller.TransactionsController;
import com.paymybudy.model.Accounts;
import com.paymybudy.model.Beneficiaries;
import com.paymybudy.model.Transactions;
import com.paymybudy.repository.AccountsRepository;
import com.paymybudy.repository.BeneficiariesRepository;
import com.paymybudy.repository.TransactionRepository;
import com.paymybudy.service.BalanceService;
import com.paymybudy.service.BeneficiaryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // https://stackoverflow.com/questions/73511395/intellij-could-not-autowire-no-beans-of-mockmvc-type-found-but-test-is-ok
class OcPayMyBuddyApplicationTests {

    @Autowired
    private BeneficiariesRepository beneficiariesRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port = 8080; //port configuration

    @Test
    @DisplayName("Beneficiary Record stored")
    void WhenBeneficiaryIsPassed_GivenToTheRepositoryThenRecordIsSaved_test() {
        //UnitTest
        //ARRANGE get post request
        Beneficiaries beneficiary = new Beneficiaries();
        beneficiary.setBeneficiaryId(1);
        beneficiary.setClientId(1);
        beneficiary.setBeneficiaryLastName("Test");
        beneficiary.setBeneficiaryFirstName("Test");
        beneficiary.setIban("FR76");
        beneficiary.setSwift("CCC");
        beneficiary.setEmail("CCC");

        //ACTION - save request in the DB
        Beneficiaries savedBeneficiary = beneficiariesRepository.save(beneficiary);

        //ASSERT
        assertNotEquals(savedBeneficiary.getBeneficiaryId(),0);
    }

    @Test
    @DisplayName("Beneficiary payment")
    void WhenClientDoesTransfer_GivenACertainBeneficiary_ThenTransactionIsSaved_test() {
        //ARRANGE - do a transfer
        Transactions transaction = new Transactions();
        //Transfer details
        transaction.setClientId(3);
        transaction.setBeneficiaryId(5);
        transaction.setAmount(500);
        transaction.setDescription("Epic purchase");
        transaction.setDate(new Date(124, Calendar.MAY,8,14,10,20));
        transaction.setStatus(1);
        transaction.setBeneficiaryName("Wopsy");

        //ACTION
        Transactions savedTransaction = transactionRepository.save(transaction);

        //ASSERT
        assertNotEquals(savedTransaction.getTransactionId(),0);
    }

    @Test
    @DisplayName("Updates Balance in the DB once a transaction is done")
    void WhenClientHasAnAccount_GivenDoesATransfer_ThenReturnsItsUpdatedBalance_test() {
        //ARRANGE
        Accounts account = new Accounts();
        //account setup
        account.setClient_ID(1);
        account.setBalance(3000.00F);
        account.setIban("CL2500");
        account.setSwift("CCC");

        //Transfer details
        Transactions transaction = new Transactions();

        transaction.setClientId(account.getClient_ID());
        transaction.setBeneficiaryId(5);
        transaction.setAmount(500.00F);
        transaction.setDescription("Epic purchase");
        transaction.setDate(new Date(124, Calendar.MAY,8,14,10,20));
        transaction.setStatus(1);
        transaction.setBeneficiaryName("Jessica");
        int accountId = balanceService.getAccountIdFromClientID(transaction.getClientId()); // transaction.setClientId(account.getClient_ID());

        //ACT - Do a transfer and Update Balance in the DB
        float balanceAfterOperation = account.getBalance() - transaction.getAmount();
        //not check if validOp
        account.setBalance(balanceAfterOperation); // definition of balance on the object
        accountsRepository.updateBalance(balanceAfterOperation, accountId); // maj balance in the DB

        //ASSERT
        assertEquals(balanceService.getBalance(accountId), balanceAfterOperation); // retrieves value from DB once updated
    }

    @Test
    @DisplayName("Get AccountID from ClientID - Accounts")
    void WhenATransactionIsDoneGivenClientIDThenReturnsAccountID() {
        //ARRANGE
        Transactions transaction = new Transactions();
        Accounts account = new Accounts();

        transaction.setClientId(1); //assumes we know ClientID from the session
        transaction.setBeneficiaryId(5);
        transaction.setBeneficiaryName("Jessica");
        transaction.setAmount(500.00F);
        transaction.setDescription("Epic purchase");
        transaction.setDate(new Date(124, Calendar.MAY,8,14,10,20));
        transaction.setStatus(1);

        //ACT
        int accountId = balanceService.getAccountIdFromClientID(transaction.getClientId());
        //ASSERT
        assertEquals(accountId>0,true);
    }

    @Test
    @DisplayName("Transfer - Looks Up and Determines BeneficiaryId")
    void WhenAPaymentIsRequestedByClientToABeneficiary_GivenClientIDAndFirstName_LooksUpForBeneficiaryId() {
        //ARRANGE
        //Positive Scenario, where the user is in the DB
        //let's assume the input is obtained from the form
        String beneficiaryFirstName = "James"; //needs to be obtained from the form select box
        int amount = 500;
        int clientId = 1; //assumes we know ClientId from the session

        //ACT
        int beneficiaryId =
                beneficiaryService.getBeneficiaryIdFromKeyClientIDAndBeneficiaryFirstName(clientId, beneficiaryFirstName);

        //ASSERT
        assertTrue(beneficiaryId > 0); //if more than 0 then exists a record for that person
    }

    /**
     *
     * Testing endpoints POST
     * Add beneficiary and payment
     *
     */

    @Test
    @DisplayName("Post - Fail Scenario - No Beneficiary is selected in the Form")
    @WithMockUser(username ="user", roles = {"ADMIN"}) // and csrf disabled -- to avoid 403
    void WhenBeneficiaryAddIsTriggered_GivenNoBeneficiaryIsSelected_ThenNoRecordIsAdded() throws Exception {
        //ARRANGE
        Iterable<Beneficiaries> beneficiaries = beneficiariesRepository.findAll();
        int elements = ((Collection) beneficiaries).size();

        //ACT
        this.mockMvc.perform(post("/newconnection")) //No value is selected
                .andExpect(status().is2xxSuccessful());
                //TODO -- "check if <option value="" selected></option>"
                //gets successful, but is it all data there?

        //ASSERT
        //assertEquals(((Collection) beneficiariesRepository.findAll()).size(), elements);

    }

    @Test
    @DisplayName("Post - Success Scenario - Beneficiary is selected in the Form and added correctly to the DB")
    @WithMockUser(username ="user", roles = {"ADMIN"}) // and csrf disabled -- to avoid 403
    void WhenBeneficiaryIsAdded_GivenAnEmailSelected_ThenAddANewRecordInBeneficiaries() throws Exception {
        //ARRANGE
        Iterable<Beneficiaries> beneficiaries = beneficiariesRepository.findAll();
        int elements = ((Collection) beneficiaries).size();

        //ACT
        //TODO -- Check how to pass the param to the post
        this.mockMvc.perform(post("/newconnection")
                        .param("selectedEmail", "michael.j@gmail.com")) //it selects a value in form
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(model().attributeExists("selectedEmail")); //MODEL attribute does not exist
        //.andExpect(content().string(containsString("id=html tags")))
        //.andExpect(content().string(containsString("html tags")));

        //ASSERT
        //assertTrue(((Collection) beneficiariesRepository.findAll()).size()>elements);
    }
    @Test
    @DisplayName("Post - Test Beneficiary is Added with UserInput Only")
    @WithMockUser(username ="user", roles = {"ADMIN"}) // csrf disabled
    void WhenBeneficiaryIsAdded_GivenClientInputInTheForm_ThenAddANewRecordInBeneficiariesIsOk() throws Exception {
        //ARRANGE
        Iterable<Beneficiaries> beneficiaries = beneficiariesRepository.findAll();
        int initialElements = ((Collection) beneficiaries).size(); //countsrecords the value in the DB

        //ACT
        this.mockMvc
                .perform(post("/newconnection")
                        .param("beneficiary.beneficiaryFirstName", "TestUser")
                        .param("beneficiary.beneficiaryLastName", "LastNameTest")
                        .param("beneficiary.iban", "ibantest")
                        .param("beneficiary.swift", "swiftTest")
                        .param("beneficiary.email", "test@test.com"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("beneficiaryAddForm"));

        //ASSERT
        //assertTrue(((Collection) beneficiariesRepository.findAll()).size() > initialElements);
    }
    /**
     *
     * Checks GET Paths are UP
     * @throws Exception
     *
     */
    @Test
    @DisplayName("GET - ENDPOINT 1 - New Transaction")
    @WithUserDetails // by default 'user'
    void testGetRequest_EndPoint1_NewTransaction_UP() throws Exception {
        //String url = "http://localhost:" + port + "/transaction";
        this.mockMvc
                .perform(get("/transactions"))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("GET - ENDPOINT 2 - New Connection")
    @WithUserDetails // by default 'user'
    void testGetRequest_EndPoint2_NewBeneficiary_UP() throws Exception {
        //String url = "http://localhost:" + port + "/transaction";
        this.mockMvc
                .perform(get("/newconnection"))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("GET - ENDPOINT 3 - Login page")
    @WithUserDetails // by default 'user'
    void testGetRequest_EndPoint3_Login_UP() throws Exception {
        this.mockMvc
                .perform(get("/login"))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("GET - ENDPOINT 4 - Home")
    @WithUserDetails // by default 'user'
    void testGetRequest_EndPoint4_Home_UP() throws Exception {
        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET - ENDPOINT 5 - Error")
    @WithUserDetails // by default 'user'
    void testGetRequest_EndPoint5_Error_UP() throws Exception {
        this.mockMvc
                .perform(get("/error"))
                .andExpect(status().isOk());
    }

    /**
     *
     * Unit tests
     *
     * @throws Exception
     *
     */

}


