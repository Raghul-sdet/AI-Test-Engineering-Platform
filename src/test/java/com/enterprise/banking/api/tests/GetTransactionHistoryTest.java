package com.enterprise.banking.api.tests;

import com.enterprise.banking.api.base.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class GetTransactionHistoryTest extends BaseApiTest {

    @Test(description = "Verify GET Transaction History API returns array of transactions")
    public void verifyTransactionHistory() {
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("accountId", profileLoader.getTestData("defaultAccountId"));

        Response response = executeRequest("getTransactionHistory", pathParams);

        response.then().spec(responseSpec);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");

        int transactionCount = response.jsonPath().getList("$").size();
        Assert.assertTrue(transactionCount > 0, "Transaction history should not be empty");
        
        String successField = profileLoader.getApiEndpoint("getTransactionHistory").get("successField").toString();
        Assert.assertNotNull(response.jsonPath().getString("[0]." + successField), "Transaction ID missing");
    }
}