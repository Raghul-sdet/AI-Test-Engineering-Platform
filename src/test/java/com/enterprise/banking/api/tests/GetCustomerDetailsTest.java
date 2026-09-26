package com.enterprise.banking.api.tests;

import com.enterprise.banking.api.base.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class GetCustomerDetailsTest extends BaseApiTest {

    @Test(description = "Verify GET Customer Details API returns valid data")
    public void verifyGetCustomerDetails() {
        String customerId = profileLoader.getTestData("defaultCustomerId"); 
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("customerId", customerId);

        Response response = executeRequest("getCustomerDetails", pathParams);

        response.then().spec(responseSpec);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");
        
        String successField = profileLoader.getApiEndpoint("getCustomerDetails").get("successField").toString();
        Assert.assertEquals(response.jsonPath().getString(successField), customerId, "Customer ID mismatch");
        Assert.assertNotNull(response.jsonPath().getString("firstName"), "First name cannot be null");
    }
}