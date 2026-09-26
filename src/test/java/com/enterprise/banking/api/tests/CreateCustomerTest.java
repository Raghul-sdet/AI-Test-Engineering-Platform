package com.enterprise.banking.api.tests;

import com.enterprise.banking.api.base.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateCustomerTest extends BaseApiTest {

    @Test(description = "Verify GET Login API for demo customer")
    public void verifyCreateCustomer() {
        Response response = executeRequest("createCustomer", null);
        
        response.then().spec(responseSpec);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");
        
        String successField = profileLoader.getApiEndpoint("createCustomer").get("successField").toString();
        Assert.assertEquals(response.jsonPath().getString(successField), profileLoader.getTestData("defaultCustomerId"), "Customer ID mismatch");
    }
}