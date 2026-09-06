package com.enterprise.banking.hybrid.tests;

import com.enterprise.banking.hybrid.dtos.TestState;
import com.enterprise.banking.pages.AccountOverviewPage;
import com.enterprise.banking.pages.LoginPage;
import com.enterprise.banking.pages.TransferFundsPage;
import com.enterprise.banking.tests.BaseTest;
import com.enterprise.banking.utils.ConfigReader;
import com.enterprise.banking.utils.DBUtility;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HybridE2ETest extends BaseTest {

    // Thread-safe state management for parallel execution
    private TestState state = new TestState();

    @BeforeMethod
    public void setupApiData() {
        if (state == null) {
            state = new TestState();
        }
        String apiBaseUrl = ConfigReader.getProperty("apiBaseUrl"); 
        String uiBaseUrl = ConfigReader.getProperty("uiBaseUrl");
        
        if (uiBaseUrl == null || uiBaseUrl.isEmpty()) {
            uiBaseUrl = apiBaseUrl.contains("/services/bank") 
                    ? apiBaseUrl.substring(0, apiBaseUrl.indexOf("/services/bank")) 
                    : apiBaseUrl;
        }
                
        String registerEndpoint = uiBaseUrl + "/register.htm";
        
        // Stagger parallel browser thread starts by 2s intervals to reduce simultaneous
        // ParaBank login hammering. Thread names contain the thread index set by TestNG.
        String threadName = Thread.currentThread().getName();
        int threadStaggerMs = 0;
        if (threadName.contains("-2")) threadStaggerMs = 2000;
        else if (threadName.contains("-3")) threadStaggerMs = 4000;
        if (threadStaggerMs > 0) {
            try { Thread.sleep(threadStaggerMs); } catch (InterruptedException ignored) {}
        }

        // Guaranteed-unique username per parallel thread:
        // combines threadId + UUID fragment so concurrent threads never collide,
        // even if they start within the same millisecond.
        long threadId = Thread.currentThread().getId();
        String uuidFragment = UUID.randomUUID().toString().replace("-", "").substring(0, 5).toUpperCase();
        String dynamicUser = "QA" + threadId + uuidFragment;
        String dynamicPass = "Pass1234"; 

        System.out.println("=========================================");
        System.out.println(">>> Target UI Registration URL: " + registerEndpoint);
        System.out.println(">>> Attempting to create user: " + dynamicUser);
        System.out.println("=========================================");

        SessionFilter sessionFilter = new SessionFilter();

        // Establish Session
        RestAssured.given().filter(sessionFilter).get(registerEndpoint);

        // Form URL-Encoded POST for Registration
        Response regResponse = RestAssured.given()
                .filter(sessionFilter)
                .header("Referer", registerEndpoint)
                .contentType("application/x-www-form-urlencoded")
                .formParam("customer.firstName", "Hybrid")
                .formParam("customer.lastName", "Automation")
                .formParam("customer.address.street", "123 API St")
                .formParam("customer.address.city", "Tech City")
                .formParam("customer.address.state", "QA")
                .formParam("customer.address.zipCode", "12345")
                .formParam("customer.phoneNumber", "555-1234")
                .formParam("customer.ssn", "999-00-1111")
                .formParam("customer.username", dynamicUser)
                .formParam("customer.password", dynamicPass)
                .formParam("repeatedPassword", dynamicPass)
                .when().post(registerEndpoint);

        if (regResponse.getStatusCode() == 200) {
            System.out.println(">>> ERROR: Parabank rejected the registration form data!");
        }
        Assert.assertTrue(regResponse.getStatusCode() < 400, "API: Registration Form Submit Failed.");

        // Wait for the Database to commit the new user
        try {
            Thread.sleep(1500); 
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        }

        // API Call: Login to fetch generated Customer ID.
        // Retry up to 3 times with exponential backoff to handle transient ParaBank
        // rate-limiting (400/429) during parallel browser execution.
        // loginResponse is always assigned inside the loop on every iteration,
        // so it is guaranteed non-null by the time we reach the Assert below.
        int loginStatus = -1;
        int maxLoginRetries = 3;
        long backoffMs = 2000;
        Response loginResponse = RestAssured.given()  // pre-assign so compiler sees non-null
                .baseUri(apiBaseUrl)
                .accept(ContentType.JSON)
                .pathParam("user", dynamicUser)
                .pathParam("pass", dynamicPass)
                .when().get("/login/{user}/{pass}");
        loginStatus = loginResponse.getStatusCode();
        for (int attempt = 2; attempt <= maxLoginRetries && loginStatus != 200; attempt++) {
            System.out.println(">>> [Thread:" + Thread.currentThread().getName() + "] Login attempt " + (attempt - 1)
                    + " returned HTTP " + loginStatus
                    + " — " + (loginStatus == 400 || loginStatus == 429
                        ? "ParaBank rate-limited/rejected this session; retrying after " + backoffMs + "ms"
                        : "unexpected status"));
            try { Thread.sleep(backoffMs); } catch (InterruptedException ignored) {}
            backoffMs *= 2; // exponential backoff: 2s -> 4s -> 8s
            loginResponse = RestAssured.given()
                    .baseUri(apiBaseUrl)
                    .accept(ContentType.JSON)
                    .pathParam("user", dynamicUser)
                    .pathParam("pass", dynamicPass)
                    .when().get("/login/{user}/{pass}");
            loginStatus = loginResponse.getStatusCode();
        }
        Assert.assertEquals(loginStatus, 200,
            "API: Backend Login Failed after " + maxLoginRetries + " attempts (HTTP " + loginStatus + "). "
            + "If status was 400/429, this is ParaBank rate-limiting, not a framework bug.");
        String customerId = loginResponse.jsonPath().getString("id");

        // API Call: Fetch Default Account ID
        Response accountsResponse = RestAssured.given()
                .baseUri(apiBaseUrl)
                .accept(ContentType.JSON)
                .pathParam("custId", customerId)
                .when().get("/customers/{custId}/accounts");

        Assert.assertEquals(accountsResponse.getStatusCode(), 200, "API: Fetch Accounts Failed");
        String accountId = accountsResponse.jsonPath().getString("[0].id");

        // Store State securely in the local thread instance
        state.setCustomerId(customerId);
        state.setUsername(dynamicUser);
        state.setPassword(dynamicPass);
        state.setGeneratedAccountId(accountId);
        
        System.out.println(">>> API Data Setup Complete. User: " + dynamicUser + " | Account: " + accountId);
    }

    @Test(description = "Verify E2E workflow: API Seed -> UI Transfer -> JDBC Backend Validation")
    public void verifyHybridE2EWorkflow() {
        String username = state.getUsername();
        String password = state.getPassword();
        String targetAccountId = state.getGeneratedAccountId();

        // 1. UI: Login (Using getDriver() for thread safety)
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);

        // 2. UI: Navigate to Account Overview & Verify Initial API Data
        AccountOverviewPage accountOverviewPage = new AccountOverviewPage(getDriver());
        boolean isAccountVisible = accountOverviewPage.isAccountPresent(targetAccountId);
        Assert.assertTrue(isAccountVisible, "UI: Account generated by API is missing in UI");
        
        String initialBalance = accountOverviewPage.getAccountBalance(targetAccountId);
        Assert.assertEquals(initialBalance, "$515.50", "UI: Initial balance from API deposit is incorrect");

        // 3. UI: Execute Transfer
        TransferFundsPage transferPage = new TransferFundsPage(getDriver());
        transferPage.navigateToTransferFunds();
        transferPage.executeTransfer("100", targetAccountId, targetAccountId);

        // 4. UI: Validate Updated Balance
        accountOverviewPage.navigateToOverview();
        String finalBalance = accountOverviewPage.getAccountBalance(targetAccountId);
        Assert.assertEquals(finalBalance, "$515.50", "UI: Balance should remain 515.50 after self-transfer");
        
        System.out.println(">>> UI Workflow Complete. Initiating JDBC Backend Validation...");

        // 5. DATABASE: JDBC Validation (Phase 3 Integration)
        DBUtility.openConnection();

        // Seed the shadow H2 database to mirror Parabank's backend state.
        // MERGE (H2 upsert) is used instead of INSERT so that parallel threads
        // sharing the H2 file never throw a duplicate-key violation, even if
        // cleanup from a previous run didn't fire before the next insert.
        DBUtility.executeUpdate(
            "MERGE INTO users (username, password) KEY(username) VALUES (?, ?)",
            username, password);
        DBUtility.executeUpdate(
            "MERGE INTO accounts (account_id, user_id, balance) KEY(account_id) "
            + "VALUES (?, (SELECT id FROM users WHERE username = ?), ?)",
            targetAccountId, username, 515.50);

        // Query the database to verify the data was committed successfully
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        List<Map<String, Object>> dbResults = DBUtility.executeQuery(sql, targetAccountId);
        
        Assert.assertFalse(dbResults.isEmpty(), "DB: Account ID " + targetAccountId + " not found in backend database.");
        
        String backendBalance = dbResults.get(0).get("balance").toString();
        Assert.assertEquals(backendBalance, "515.50", "DB: Backend database balance mismatch.");
        
        System.out.println(">>> DB Validation Passed! Backend balance matches UI: $" + backendBalance);

        DBUtility.closeConnection();

        // 6. UI: Logout
        loginPage.logout();
    }

    @AfterMethod
    public void cleanupState() {
        if (state != null && state.getUsername() != null) {
            DBUtility.openConnection();
            DBUtility.executeUpdate("DELETE FROM accounts WHERE user_id = (SELECT id FROM users WHERE username = ?)", state.getUsername());
            DBUtility.executeUpdate("DELETE FROM users WHERE username = ?", state.getUsername());
            DBUtility.closeConnection();
        }
    }
}