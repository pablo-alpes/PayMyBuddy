package com.paymybudy;

import com.paymybudy.configuration.SecurityConfiguration;
import com.paymybudy.model.Accounts;
import com.paymybudy.model.Beneficiaries;
import com.paymybudy.model.Client;
import com.paymybudy.model.Transactions;
import com.paymybudy.repository.*;
import com.paymybudy.service.*;
import jakarta.servlet.ServletException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

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

    @Autowired
    private ClientUserDetailsService clientUserDetailsService;

    @Autowired
    private TransactionService transactionService;
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

    @Nested
    @Component
    class endpoints
    {
        @Autowired
        private MockMvc mockMvc;
        @Test
        @DisplayName("Post - Dropdown - Success Scenario - Beneficiary is selected in the Form and added correctly to the DB")
        @WithMockUser(username = "user", roles = {"ADMIN"})
            // and csrf disabled -- to avoid 403
        void WhenBeneficiaryIsAdded_GivenAnEmailSelected_ThenAddANewRecordInBeneficiaries() throws Exception {
            //ARRANGE
            Iterable<Beneficiaries> beneficiaries = beneficiariesRepository.findAll();
            int elements = ((Collection) beneficiaries).size();

            //ACT
            MvcResult mvcResult =
                    this.mockMvc.perform(post("/newconnection")
                                    .param("selectedEmail", "michael.smith@gmail.com")) //it selects a value in form
                            .andExpect(status().is2xxSuccessful())
                            .andExpect(model().attributeExists("beneficiaryAddFormDTO"))
                            .andReturn();

            //ASSERT
            assertTrue(mvcResult.getRequest().getParameter("selectedEmail").contains("michael.smith@gmail.com"));
        }

        @Test
        @DisplayName("Post - Dropdown - Fail Scenario - No Beneficiary is selected in the Form")
        @WithMockUser(username = "user", roles = {"ADMIN"})
        void WhenBeneficiaryAddIsTriggered_GivenNoBeneficiaryIsSelected_ThenNoRecordIsAdded() throws Exception {
            //ARRANGE
            //ACT
            try {
                this.mockMvc
                        .perform(post("/newconnection")
                                .param("selectedEmail", "")); //it selects the blank cell of the dropdown
            } catch (ServletException e) {
                assertTrue(e.getRootCause().toString().contains("NullPointerException")); //gets nullpointerexception once trying to invoke the methods to search
            }
        }

        @Test
        @DisplayName("Post - Input - Test Beneficiary is Added with UseserInput Only")
        @WithMockUser(username = "user", roles = {"ADMIN"})
            // csrf disabled
        void WhenBeneficiaryIsAdded_GivenClientInputInTheForm_ThenAddANewRecordInBeneficiariesIsOk() throws Exception {
            //ARRANGE
            //Iterable<Beneficiaries> beneficiaries = beneficiariesRepository.findAll();
            //int initialElements = ((Collection) beneficiaries).size(); //countsrecords the value in the DB
            Beneficiaries beneficiary = new Beneficiaries();

            beneficiary.setClientId(1);
            beneficiary.setBeneficiaryFirstName("TestUser");
            beneficiary.setBeneficiaryLastName("LastNameTest");
            beneficiary.setIban("ibantest");
            beneficiary.setSwift("swiftTest");
            beneficiary.setEmail("test@test.com");

            Beneficiaries beneficiarySaved = beneficiariesRepository.save(beneficiary);

            //ACT
            MvcResult mvcResult = this.mockMvc
                    .perform(post("/newconnection")
                            .param("beneficiaryFirstName", beneficiary.getBeneficiaryFirstName())
                            .param("beneficiaryLastName", beneficiary.getBeneficiaryLastName())
                            .param("iban", beneficiary.getIban())
                            .param("swift", beneficiary.getSwift())
                            .param("email", beneficiary.getEmail()))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(model().attributeExists("beneficiaryAddForm"))
                    .andReturn();

            //assert
            assertNotEquals(beneficiarySaved.getBeneficiaryId(), 0);
            assertEquals((mvcResult.getRequest().getParameter("email")), "test@test.com");
        }

        @Test
        @DisplayName("Post - Input - Form is correctly fulfilled to post new beneficiary")
            // csrf disabled
        void WhenBeneficiaryIsAdded_GivenClientInputInTheForm_ThenFormIsFulfilledProperlyAndRedirects() throws Exception {
            //ARRANGE
            //Iterable<Beneficiaries> beneficiaries = beneficiariesRepository.findAll();
            //int initialElements = ((Collection) beneficiaries).size(); //countsrecords the value in the DB
            Beneficiaries beneficiary = new Beneficiaries();

            beneficiary.setClientId(1);
            beneficiary.setBeneficiaryFirstName("TestUser");
            beneficiary.setBeneficiaryLastName("LastNameTest");
            beneficiary.setIban("ibantest");
            beneficiary.setSwift("swiftTest");
            beneficiary.setEmail("test@test.com");

            //ACT
            MvcResult mvcResult = this.mockMvc
                    .perform(post("/newconnection")
                            .param("beneficiaryFirstName", beneficiary.getBeneficiaryFirstName())
                            .param("beneficiaryLastName", beneficiary.getBeneficiaryLastName())
                            .param("iban", beneficiary.getIban())
                            .param("swift", beneficiary.getSwift())
                            .param("email", beneficiary.getEmail()))
                    .andExpect(status().is3xxRedirection())
                    .andReturn();

            //assert
            assertEquals((mvcResult.getRequest().getParameter("email")), "test@test.com");
            assertTrue((mvcResult.getResponse().getRedirectedUrl().contains("login")));
        }

        @Test
        @DisplayName("Post - Input - Payment test")
        @WithMockUser(username = "user", roles = {"ADMIN"})
            // csrf disabled
        void WhenPaymentIsDone_GivenClientSelectedAndAmount_ThenPaymentIsOk() throws Exception {
            //ACT
            MvcResult mvcResult =
                    this.mockMvc
                            .perform(post("/transactions")
                                    .param("beneficiaryName", "Sarah")
                                    .param("amount", "100"))
                            .andExpect(status().is2xxSuccessful())
                            .andExpect(model().attributeExists("payment")).andReturn();
            //ASSERT
            assertEquals(Integer.parseInt(mvcResult.getRequest().getParameter("amount")), 100);

        }

        /**
         * Checks GET Paths are UP
         *
         * @throws Exception
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
            MvcResult mvcResult = this.mockMvc
                    .perform(post("/registration")
                            .param("firstName", "test")
                            .param("lastName", "test")
                            .param("iban", "FR76TEST")
                            .param("swift", "CCCTEST")
                            .param("email", "email@test.com")
                            .param("password", "passwordTest"))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(model().attributeExists("client"))
                    .andReturn();

            assertTrue(mvcResult.getRequest().getParameter("password").toString().equals("passwordTest"));
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
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(model().attributeExists("registrationAddFormDTO"))
                    .andReturn();
            //Then: Value is well passed to the POST form. We check for one of latest as is sequentially added.
            assertEquals((mvcResult.getRequest().getParameter("password")), "passwordtest");
        }


        @Test
        @DisplayName("GET - Bank transactions")
        @WithMockUser(username = "user", roles = {"ADMIN"})
        void test_DepositAccount_UP() throws Exception {
            this.mockMvc
                    .perform(get("/bank"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("POST - Deposit to account")
        @WithMockUser(username = "user", roles = {"ADMIN"})
            // by default 'user'
        void test_DepositAccount_Post() throws Exception {
            this.mockMvc
                    .perform(post("/balance")
                            .param("deposit", "1000"))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(model().attributeExists("bank"));
        }

        @Test
        @DisplayName("POST - Withdraw from account")
        @WithMockUser(username = "user", roles = {"ADMIN"})
            // by default 'user'
        void test_Withdraw_Post() throws Exception {
            this.mockMvc
                    .perform(post("/withdraw")
                            .param("withdraw", "1000"))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(model().attributeExists("bank"));
        }



    }
    @Nested
    @Transactional
    class db {
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
    }

    @Nested
    @Component
    @Transactional
    class security {
        @Autowired
        private MockMvc mockMvc;
        /**
         * Security tests for endpoints
         */

        @Test
        @DisplayName("Login check successsful")
        @WithMockUser(username = "user")
        void testLoginOk() throws Exception {
            this.mockMvc
                    .perform(
                            post("/login")
                                    .param("username", "user")
                                    .param("password", "$2a$10$YXYinPAMn6MSeDoaaEQ.WOX7fp8B5TopQ5N7LiEDAeZXmjZ9ZV6vu"))
                    .andExpect(status().is3xxRedirection());
        }

        @Test
        @DisplayName("Login check failed")
        @WithMockUser(username = "user")
        void testLoginFailed() throws Exception {
            MvcResult mvcResult = this.mockMvc
                    .perform(
                            post("/login")
                                    .param("username", "user")
                                    .param("password", "wrong"))
                    .andExpect(status().is3xxRedirection())
                    .andReturn();
            assertTrue((mvcResult.getResponse().getRedirectedUrl().contains("/login?error=true")));
        }

        @Test
        @DisplayName("Login user and password retrieval")
        @WithMockUser(username = "user")
        void testGetLoginDetails() throws Exception {
            Client client = clientRepository.findClientByEmail("user");
            assertNotNull(clientUserDetailsService.loadUserByUsername("user").toString());
        }

        @Test
        @WithUserDetails
        @DisplayName("Login user and password retrieval from DB")
        void testGetLoginDetailsFromDB() throws Exception {
            assertNotNull(clientRepository.findClientByEmail("user"));
            assertEquals(clientRepository.findClientByEmail("user").getPassword(), "$2a$10$YXYinPAMn6MSeDoaaEQ.WOX7fp8B5TopQ5N7LiEDAeZXmjZ9ZV6vu");
            assertEquals(clientRepository.findClientByEmail("user").getFirstName(), "Jane");
        }

        /**
         * Testing endpoints POST
         * Add beneficiary and payment
         */

        @Test
        @DisplayName("IT create client")
        void test_creationClient() throws Exception {
            //Given
            Client client = new Client();

            //ACT
            client.setFirstName("test");
            client.setLastName("test");
            client.setPassword("test");
            client.setEmail("test@tester.com");
            client.setRole("USER"); // by default all persons are 'USER'

            accountCreationService.createClient(client); // records the client user

            //ASSERT
            assertEquals(clientRepository.findClientByEmail(client.getEmail()).getEmail(), client.getEmail());
        }

        @Test
        @DisplayName("IT create account")
        void test_creationAccount() {
            //the person needs to have previously created a client account, we take "user"
            Accounts account = new Accounts();

            account.setBalance(0); //by default clients do not have money in the account // à intégrer dans la méthode
            account.setIban("FR76TEST");
            account.setSwift("swiftest");

            int before = (int) accountsRepository.count();
            //la valeur username viendra du wrapper AddFormDTO
            accountCreationService.createAccount("francesco.martelli@gmail.com", account.getIban(), account.getSwift()); // creates the account

            //ASSERT
            assertNotEquals(accountsRepository.count(), before);

        }

        @Test
        @DisplayName("Bank Transaction - Checks deposit is OK (transaction record and balance)")
        void test_updateBalance() {
            int clientId = 1; //vient du session actuelle

            float balanceToDeposit = 1000F;
            float balance = balanceService.getBalance(clientId);
            float totalOperation = balance + balanceToDeposit;

            //Transfer details
            Transactions transaction = new Transactions();
            transaction.setClientId(clientId);
            transaction.setBeneficiaryId(1);  // le beneficiare doit etre enregistré en amont dans la liste de beneficiaries
            transaction.setDate(new Date());
            transaction.setAmount(balanceToDeposit);
            transaction.setDescription("Credit deposit from User");
            transaction.setStatus(1);
            transaction.setBeneficiaryName("Client USER");

            //ACTION
            balanceService.updateBalance(clientId, totalOperation);

            Transactions savedTransaction = transactionRepository.save(transaction);

            //ASSERT
            assertNotEquals(savedTransaction.getTransactionId(), 0);
            assertEquals(savedTransaction.getAmount(), balanceToDeposit);
        }


        @Test
        @DisplayName("Bank Transaction - Withdraw is OK (transaction record and balance)")
        void test_withdrawExecution() {
            int clientId = 1; //vient du session actuelle

            float withdraw = -1000;
            float balance = balanceService.getBalance(clientId);
            float totalOperation = balance + withdraw;

            balanceService.updateBalance(clientId, totalOperation); //on décompte le montant du versement, mettre à jour balance du compte

            //Transfer details
            Transactions transaction = new Transactions();
            transaction.setClientId(clientId);
            transaction.setBeneficiaryId(1); // le beneficiare doit etre enr'egistré en amont dans la liste de beneficiaries
            transaction.setDate(new Date());
            transaction.setAmount(withdraw);
            transaction.setDescription("Withdraw from User");
            transaction.setStatus(1);
            transaction.setBeneficiaryName("Client USER");

            //ACTION
            // transaction doit se compter, funds check is needed -> dotransfer needs to tbe used
            Transactions savedTransaction = transactionRepository.save(transaction); // Note: Good practice is to return the object, so remains testable

            //ASSERT
            assertNotEquals(savedTransaction.getTransactionId(), 0);
            assertEquals(savedTransaction.getAmount(), withdraw);
        }

        @Test
        @DisplayName("Decrypted password matching for a new user")
        void test_GIVENnewUserRegister_WHENPasswordEncodedByBcrypt_THENMatchesLoginStoredPassword() {
            //arrange
            SecurityConfiguration securityConfiguration = new SecurityConfiguration();

            //act
            String encriptedPassword = securityConfiguration.passwordEncoder().encode("test");
            String password = "test";

            //assert
            assertTrue(securityConfiguration.passwordEncoder().matches(password,encriptedPassword));
        }

        @Test
        @DisplayName("Fail - Decrypted password non matching registered one")
        void test_GIVENStoragedPasswordInDb_WHENMatchCheck_THENRawPasswordNotMatchesWithEncodedOne() {
            //arrange
            SecurityConfiguration securityConfiguration = new SecurityConfiguration();

            //act
            String password = ",password";
            String encriptedPassword = "$2a$10$YXYinPAMn6MSeDoaaEQ.WOX7fp8B5TopQ5N7LiEDAeZXmjZ9ZV6vu";

            //assert
            assertFalse(securityConfiguration.passwordEncoder().matches(password,encriptedPassword));
        }


    }

}


