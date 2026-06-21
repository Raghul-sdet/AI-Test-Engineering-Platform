package com.enterprise.banking.api.tests;

import com.enterprise.banking.api.base.BaseApiTest;
import com.enterprise.banking.api.utils.ApiUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class GetTransactionHistoryTest extends BaseApiTest {

    @Test(description = "Verify GET Transaction History API returns array of transactions")
    public void verifyTransactionHistory() {
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("accountId", "19005");

        String endpoint = "/accounts/{accountId}/transactions";
        Response response = ApiUtils.getWithPathParams(requestSpec, endpoint, pathParams);

        response.then().spec(responseSpec);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");

        int transactionCount = response.jsonPath().getList("$").size();
        Assert.assertTrue(transactionCount > 0, "Transaction history should not be empty");
        Assert.assertNotNull(response.jsonPath().getString("[0].transactionId"), "Transaction ID missing");
    }
}