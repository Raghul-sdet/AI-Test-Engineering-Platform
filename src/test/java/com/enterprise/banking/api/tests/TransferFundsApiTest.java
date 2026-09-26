package com.enterprise.banking.api.tests;

import com.enterprise.banking.api.base.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class TransferFundsApiTest extends BaseApiTest {

    @Test(description = "Verify POST Transfer Funds API processes transaction successfully")
    public void verifyTransferFunds() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("fromAccountId", profileLoader.getTestData("defaultAccountId"));
        queryParams.put("toAccountId", profileLoader.getTestData("transferToAccountId"));
        queryParams.put("amount", profileLoader.getTestData("transferAmount"));

        Response response = executeRequest("transferFunds", queryParams);

        System.out.println("RESPONSE BODY: " + response.getBody().asString());
        response.then().spec(responseSpec);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");
        
        String responseBody = response.asString();
        String successField = profileLoader.getApiEndpoint("transferFunds").get("successField").toString();
        Assert.assertTrue(responseBody.contains(successField), "Transfer status validation failed");
    }
}