package com.enterprise.banking.api.base;

import com.enterprise.banking.api.utils.RestAssuredListener;
import com.enterprise.banking.config.SiteProfileLoader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeSuite;

import java.util.Map;

public class BaseApiTest {

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;
    protected static SiteProfileLoader profileLoader = new SiteProfileLoader();

    @BeforeSuite(alwaysRun = true)
    public void setupApiEnvironment() {
        // Initialize base URI from SiteProfileLoader
        RestAssured.baseURI = profileLoader.getApiBaseUrl();

        // Define global request specifications including Extent Reports logging
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new RestAssuredListener())
                .build();

        // Define global response specifications (e.g., SLA response time < 5 seconds)
        responseSpec = new ResponseSpecBuilder()
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();

        try {
            // Dynamic Customer Registration for API tests
            String uniqueSuffix = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 10);
            String username = "user" + uniqueSuffix;
            String password = "password";

            // 1a. GET register.htm to establish a session
            Response getRegResp = RestAssured.given()
                    .baseUri(profileLoader.getBaseUrl())
                    .get("/register.htm");
            
            String sessionId = getRegResp.getCookie("JSESSIONID");
            
            // 1b. POST to register.htm
            Response regResp = RestAssured.given()
                    .baseUri(profileLoader.getBaseUrl())
                    .cookie("JSESSIONID", sessionId)
                    .contentType(ContentType.URLENC)
                    .formParam("customer.firstName", "Api")
                    .formParam("customer.lastName", "Test")
                    .formParam("customer.address.street", "123 Main")
                    .formParam("customer.address.city", "City")
                    .formParam("customer.address.state", "ST")
                    .formParam("customer.address.zipCode", "12345")
                    .formParam("customer.phoneNumber", "555-5555")
                    .formParam("customer.ssn", "123-45-6789")
                    .formParam("customer.username", username)
                    .formParam("customer.password", password)
                    .formParam("repeatedPassword", password)
                    .post("/register.htm");
            
            if (regResp.statusCode() >= 500) {
                throw new RuntimeException("ParaBank registration endpoint returned 500. Registration might be genuinely broken on the backend.");
            }
            
            String regBody = regResp.asString();
            if (!regBody.contains("Your account was created successfully")) {
                throw new RuntimeException("Registration failed validation or didn't succeed. Response: " + regBody);
            }

            // 2. Fetch customerId via login API
            Response loginResp = RestAssured.given()
                    .baseUri(profileLoader.getApiBaseUrl())
                    .accept(ContentType.JSON)
                    .get("/login/" + username + "/" + password);

            if (loginResp.statusCode() != 200) {
                throw new RuntimeException("Login API failed after registration: " + loginResp.statusCode());
            }

            String customerId = loginResp.jsonPath().getString("id");
            if (customerId == null || customerId.isEmpty()) {
                throw new RuntimeException("Login API returned a null/empty customerId.");
            }
            profileLoader.setTestData("defaultCustomerId", customerId);
            profileLoader.setTestData("defaultUsername", username);
            profileLoader.setTestData("defaultPassword", password);

            // 3. Fetch default accountId
            Response accountsResp = RestAssured.given()
                    .baseUri(profileLoader.getApiBaseUrl())
                    .accept(ContentType.JSON)
                    .get("/customers/" + customerId + "/accounts");

            if (accountsResp.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch accounts for customer ID: " + customerId);
            }

            String accountId = accountsResp.jsonPath().getString("[0].id");
            if (accountId == null || accountId.isEmpty()) {
                throw new RuntimeException("No default account found for customer ID: " + customerId);
            }
            profileLoader.setTestData("defaultAccountId", accountId);

            // 4. Create a second account for transfer API testing
            Response createResp = RestAssured.given()
                    .baseUri(profileLoader.getApiBaseUrl())
                    .accept(ContentType.JSON)
                    .queryParam("customerId", customerId)
                    .queryParam("newAccountType", 0)
                    .queryParam("fromAccountId", accountId)
                    .post("/createAccount");

            if (createResp.statusCode() != 200) {
                throw new RuntimeException("Failed to create a second account for transfer testing.");
            }

            String toAccountId = createResp.jsonPath().getString("id");
            if (toAccountId == null || toAccountId.isEmpty()) {
                throw new RuntimeException("Created second account but received empty ID.");
            }
            profileLoader.setTestData("transferToAccountId", toAccountId);

            System.out.println(">>> Dynamic API Test Customer Setup Complete. CustomerId: " + customerId + " | Default Account: " + accountId + " | Transfer Account: " + toAccountId);
        } catch (Exception e) {
            throw new RuntimeException("Dynamic customer setup failed at step: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to build and execute API requests dynamically based on YAML config.
     */
    protected Response executeRequest(String operationName, Map<String, Object> params) {
        Map<String, String> endpointDetails = profileLoader.getApiEndpoint(operationName);
        String method = endpointDetails.get("method");
        String path = endpointDetails.get("path");
        String paramStyle = endpointDetails.get("paramStyle");

        RequestSpecification spec = RestAssured.given().spec(requestSpec);

        if (params != null && !params.isEmpty()) {
            if ("query".equalsIgnoreCase(paramStyle)) {
                spec.queryParams(params);
            } else if ("path".equalsIgnoreCase(paramStyle)) {
                spec.pathParams(params);
            } else if ("json".equalsIgnoreCase(paramStyle)) {
                spec.body(params);
            }
        }

        return spec.request(method, path);
    }
}