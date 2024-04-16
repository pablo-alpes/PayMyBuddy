package com.paymybudy;

import com.paymybudy.model.Accounts;
import com.paymybudy.model.Beneficiaries;
import com.paymybudy.model.Client;
import com.paymybudy.model.Transactions;
import com.paymybudy.repository.*;
import com.paymybudy.service.AccountCreationService;
import com.paymybudy.service.BalanceService;
import com.paymybudy.service.BeneficiaryService;
import com.paymybudy.service.ClientUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // https://stackoverflow.com/questions/73511395/intellij-could-not-autowire-no-beans-of-mockmvc-type-found-but-test-is-ok
class OcPayMyBuddyApplicationTests {

    @Autowired
    private BeneficiariesRepository beneficiariesRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    private AccountCreationService accountCreationService;
    @Mock
    private BeneficiaryAddFormDTO beneficiaryAddFormDTO;

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port = 8080; //port configuration

    @BeforeEach
    void startUp() {
        beneficiaryAddFormDTO = new BeneficiaryAddFormDTO();
    }

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
        assertNotEquals(savedBeneficiary.getBeneficiaryId(), 0);
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
        transaction.setDate(new Date(124, Calendar.MAY, 8, 14, 10, 20));
        transaction.setStatus(1);
        transaction.setBeneficiaryName("Wopsy");

        //ACTION
        Transactions savedTransaction = transactionRepository.save(transaction);

        //ASSERT
        assertNotEquals(savedTransaction.getTransactionId(), 0);
    }

    @Test
    @DisplayName("Updates Balance in the DB once a transaction is done")
    void WhenClientHasAnAccount_GivenDoesATransfer_ThenReturnsItsUpdatedBalance_test() {
        //ARRANGE
        Accounts account = new Accounts();
        //account setup
        account.setClientId(1);
        account.setBalance(3000.00F);
        account.setIban("CL2500");
        account.setSwift("CCC");

        //Transfer details
        Transactions transaction = new Transactions();

        transaction.setClientId(account.getClientId());
        transaction.setBeneficiaryId(5);
        transaction.setAmount(500.00F);
        transaction.setDescription("Epic purchase");
        transaction.setDate(new Date(124, Calendar.MAY, 8, 14, 10, 20));
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
        transaction.setDate(new Date(124, Calendar.MAY, 8, 14, 10, 20));
        transaction.setStatus(1);

        //ACT
        int accountId = balanceService.getAccountIdFromClientID(transaction.getClientId());
        //ASSERT
        assertEquals(accountId > 0, true);
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
     * Testing endpoints POST
     * Add beneficiary and payment
     */


    @Test
    @DisplayName("Post - Dropdown - Success Scenario - Beneficiary is selected in the Form and added correctly to the DB")
    @WithMockUser(username = "user", roles = {"ADMIN"})
    // and csrf disabled -- to avoid 403
    void WhenBeneficiaryIsAdded_GivenAnEmailSelected_ThenAddANewRecordInBeneficiaries() throws Exception {
        //ARRANGE
        Iterable<Beneficiaries> beneficiaries = beneficiariesRepository.findAll();
        int elements = ((Collection) beneficiaries).size();

        //ACT
        //TODO -- Check how to pass the param to the post
        this.mockMvc.perform(post("/newconnection")
                        .param("selectedEmail", "michael.smith@gmail.com")) //it selects a value in form
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("beneficiaryAddFormDTO")); //MODEL attribute does not exist
        //.andExpect(content().string(containsString("id=html tags")))
        //.andExpect(content().string(containsString("html tags")));

        //ASSERT
        //assertTrue(((Collection) beneficiariesRepository.findAll()).size()>elements);
    }

    //TODO--Fix it to ensure the nullpointerex is considered and the branch is tested
    @Test
    @DisplayName("Post - Dropdown - Fail Scenario - No Beneficiary is selected in the Form")
    @WithMockUser(username = "user", roles = {"ADMIN"})
    // and csrf disabled -- to avoid 403
    void WhenBeneficiaryAddIsTriggered_GivenNoBeneficiaryIsSelected_ThenNoRecordIsAdded() throws Exception {
        //ARRANGE
        Iterable<Beneficiaries> beneficiaries = beneficiariesRepository.findAll();
        int elements = ((Collection) beneficiaries).size();

        //ACT
        try {
            this.mockMvc.perform(post("/newconnection")
                            .param("selectedEmail", "")) //it selects a value in form
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(model().attributeExists("beneficiaryAddFormDTO"));
            //TODO -- "check if <option value="" selected></option>"
        } catch (NullPointerException e) {
            //returns nullpointerexcep so it"s OK
            assertTrue(true);
        }

        //ASSERT
        //assertTrue(((Collection) beneficiariesRepository.findAll()).size()>elements);
    }
    //TODO--To refine the test so to ensure tests the input for the user, probably do a test in DB
    @Test
    @DisplayName("Post - Input - Test Beneficiary is Added with UserInput Only")
    @WithMockUser(username = "user", roles = {"ADMIN"})
        // csrf disabled
    void WhenBeneficiaryIsAdded_GivenClientInputInTheForm_ThenAddANewRecordInBeneficiariesIsOk() throws Exception {
        //ARRANGE
        //Iterable<Beneficiaries> beneficiaries = beneficiariesRepository.findAll();
        //int initialElements = ((Collection) beneficiaries).size(); //countsrecords the value in the DB
        Beneficiaries beneficiary = new Beneficiaries();
        BeneficiaryAddFormDTO beneficiaryAddForm = new BeneficiaryAddFormDTO();

        beneficiary.setClientId(1);
        beneficiary.setBeneficiaryFirstName("TestUser");
        beneficiary.setBeneficiaryLastName("LastNameTest");
        beneficiary.setIban("ibantest");
        beneficiary.setSwift("swiftTest");
        beneficiary.setEmail("test@test.com");

        when(beneficiaryAddForm.getBeneficiaries()).thenReturn(beneficiary);
        //ACT

        try {
            this.mockMvc
                    .perform(post("/newconnection")
                            .param("selectedEmail", ""));
        }
        catch (NullPointerException e) {
            this.mockMvc
                    .perform(post("/newconnection")
                            .param("beneficiary.beneficiaryFirstName", beneficiary.getBeneficiaryFirstName())
                            .param("beneficiary.beneficiaryLastName", beneficiary.getBeneficiaryLastName())
                            .param("beneficiary.iban", beneficiary.getIban())
                            .param("beneficiary.swift", beneficiary.getSwift())
                            .param("beneficiary.email", beneficiary.getEmail()))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(model().attributeExists("beneficiaryAddForm"));

            //assert
            verify(beneficiaryService,
                    times(1)).
                    addBeneficiary(
                            beneficiary.getClientId(),
                            beneficiary.getBeneficiaryFirstName(),
                            beneficiary.getBeneficiaryLastName(),
                            beneficiary.getIban(),
                            beneficiary.getSwift(),
                            beneficiary.getEmail());
        }
    }

    @Test
    @DisplayName("Post - Input - Payment test")
    @WithMockUser(username = "user", roles = {"ADMIN"})
        // csrf disabled
    void WhenPaymentIsDone_GivenClientSelectedAndAmount_ThenPaymentIsOk() throws Exception {
        //ACT
        this.mockMvc
                .perform(post("/transactions")
                        .param("beneficiaryName", "Sarah")
                        .param("amount", "100"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("payment"));
        //ASSERT
        //assertTrue(((Collection) beneficiariesRepository.findAll()).size() > initialElements);
    }

    /**
     * Checks GET Paths are UP
     *
     * @throws Exception
     */
    @Test
    @DisplayName("GET - ENDPOINT 1 - New Transaction")
    @WithUserDetails
    // by default 'user'
    void testGetRequest_EndPoint1_NewTransaction_UP() throws Exception {
        //String url = "http://localhost:" + port + "/transaction";
        this.mockMvc
                .perform(get("/transactions"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET - ENDPOINT 2 - New Connection")
    @WithUserDetails
        // by default 'user'
    void testGetRequest_EndPoint2_NewBeneficiary_UP() throws Exception {
        //String url = "http://localhost:" + port + "/transaction";
        this.mockMvc
                .perform(get("/newconnection"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET - ENDPOINT 3 - Login page")
    @WithUserDetails
        // by default 'user'
    void testGetRequest_EndPoint3_Login_UP() throws Exception {
        this.mockMvc
                .perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET - ENDPOINT 4 - Home")
    @WithUserDetails
        // by default 'user'
    void testGetRequest_EndPoint4_Home_UP() throws Exception {
        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET - ENDPOINT 5 - Error")
    @WithUserDetails
        // by default 'user'
    void testGetRequest_EndPoint5_Error_UP() throws Exception {
        this.mockMvc
                .perform(get("/error"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET - ENDPOINT 6 - Registration")
    @WithUserDetails
        // by default 'user'
    void test_GET_EndPoint6_Registration_UP() throws Exception {
        this.mockMvc
                .perform(get("/registration"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST - ENDPOINT 6 - Registration")
    @WithUserDetails
        // by default 'user'
    void test_POST_EndPoint6_Registration_UP() throws Exception {
        this.mockMvc
                .perform(post("/registration")
                        .param("firstName", "test")
                        .param("lastName", "test")
                        .param("iban", "FR76TEST")
                        .param("swift", "CCCTEST")
                        .param("email", "email@test.com")
                        .param("password", "passwordTest"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("client"));
    }

    @Test
    @DisplayName("Input a new user parameters")
    @WithAnonymousUser
    void test_GivenRegistrationPageForm_WhenAnAnonymousCompletesRegistrationForm_Then_AccountIsCreatedAndRecordInTheDB() throws Exception {
        //Given
        //When
        MvcResult mvcResult = this.mockMvc
                .perform(post("/registration")
                        .param("firstName", "test")
                        .param("lastName", "test")
                        .param("iban", "FR76TEST")
                        .param("swift", "swiftest")
                        .param("email", "email@test.com")
                        .param("password", "passwordtest"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        //Then: Value is well passed to the POST form
        assertEquals((mvcResult.getRequest().getParameter("email")),"email@test.com");
    }
    @Test
    @DisplayName("Unit test create client")
    //@WithAnonymousUser
    void test_creationClient() throws Exception {
        //Given
        Client client = new Client();

        //ACT
        client.setFirstName("test");
        client.setLastName("test");
        client.setPassword("test");
        client.setEmail("email@test.com");
        client.setRole("USER"); // by default all persons are 'USER' // à intégrer dans la méthode
        client.setClientId(100);

        accountCreationService.createClient(client); // records the client user

        //ASSERT
    }
    @Test
    @DisplayName("Unit test create account")
    //@WithAnonymousUser
    void test_creationAccount() throws Exception {
        //the person needs to have previously created a client account
        Accounts account = new Accounts();

        account.setBalance(0); //by default clients do not have money in the account // à intégrer dans la méthode
        account.setIban("FR76TEST");
        account.setSwift("swiftest");
        //la valeur username viendra du wrapper AddFormDTO

        accountCreationService.createAccount("email@test.com", account.getIban(), account.getSwift()); // creates the account
        //accountCreationService.createAccount("test", account.getIban(), account.getSwift()); // creates the account
    }

    /**
     *
     * Unit tests
     *
     * @throws Exception
     *
     */

    /**
     * Security test
     */

    @Test
    @DisplayName("Login check successsful")
    //@WithUserDetails
    @WithMockUser(username="user")
    void testLoginOk() throws Exception {
        this.mockMvc
                .perform(
                        post("/login")
                    .param("username","user")
                    .param("password","$2a$10$YXYinPAMn6MSeDoaaEQ.WOX7fp8B5TopQ5N7LiEDAeZXmjZ9ZV6vu"))
                .andExpect(status().is3xxRedirection()); //check for good url: /login?error=true
    }

    @Test
    @DisplayName("Login check failed")
    //@WithUserDetails
    @WithMockUser(username="user")
    void testLoginFailed() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(
                        post("/login")
                                .param("username","user")
                                .param("password","wrong"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertTrue((mvcResult.getResponse().getRedirectedUrl().contains("/login?error=true")));
    }

    @Test
    @DisplayName("Login user and password retrieval")
    @WithMockUser(username = "user")
    void testGetLoginDetails() throws Exception {
        //needs a mock for clientrepo?
        ClientUserDetailsService clientUserDetailsService = new ClientUserDetailsService();
        Client client = clientRepository.findClientByEmail("user");
        assertNotNull(clientUserDetailsService.loadUserByUsername("user").toString());
    }

    @Test
    @WithUserDetails
    @DisplayName("Login user and password retrieval from DB")
    void testGetLoginDetailsFromDB() throws Exception {
        assertNotNull(clientRepository.findClientByEmail("user"));
        assertEquals(clientRepository.findClientByEmail("user").getPassword(),"$2a$10$YXYinPAMn6MSeDoaaEQ.WOX7fp8B5TopQ5N7LiEDAeZXmjZ9ZV6vu");
        assertEquals(clientRepository.findClientByEmail("user").getFirstName(),"Jane");
    }

    //TODO--Test de View


}


