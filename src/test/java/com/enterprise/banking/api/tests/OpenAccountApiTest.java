package com.enterprise.banking.api.tests;

import com.enterprise.banking.api.base.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class OpenAccountApiTest extends BaseApiTest {

    @Test(description = "Verify POST Open Account API creates a new account")
    public void verifyOpenAccount() {
        String customerId = profileLoader.getTestData("defaultCustomerId");
        String accountType = "1"; // 0 = CHECKING, 1 = SAVINGS
        String fromAccountId = profileLoader.getTestData("defaultAccountId");

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("customerId", customerId);
        queryParams.put("newAccountType", accountType);
        queryParams.put("fromAccountId", fromAccountId);

        Response response = executeRequest("openAccount", queryParams);

        response.then().spec(responseSpec);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");
        
        String successField = profileLoader.getApiEndpoint("openAccount").get("successField").toString();
        Assert.assertNotNull(response.jsonPath().getString(successField), "New Account ID should be generated");
        Assert.assertEquals(response.jsonPath().getString("customerId"), customerId, "Customer ID mismatch");
    }
}