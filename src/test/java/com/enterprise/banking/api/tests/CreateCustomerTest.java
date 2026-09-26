package com.enterprise.banking.api.tests;

import com.enterprise.banking.api.base.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateCustomerTest extends BaseApiTest {

    @Test(description = "Verify GET Login API for demo customer")
    public void verifyCreateCustomer() {
        java.util.Map<String, Object> params = java.util.Map.of(
            "username", profileLoader.getTestData("defaultUsername"),
            "password", profileLoader.getTestData("defaultPassword")
        );
        Response response = executeRequest("createCustomer", params);
        
        response.then().spec(responseSpec);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");
        
        String successField = profileLoader.getApiEndpoint("createCustomer").get("successField").toString();
        Assert.assertEquals(response.jsonPath().getString(successField), profileLoader.getTestData("defaultCustomerId"), "Customer ID mismatch");
    }
}