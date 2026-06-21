package com.enterprise.banking.api.tests;

import com.enterprise.banking.api.base.BaseApiTest;
import com.enterprise.banking.api.payloads.CustomerPayload;
import com.enterprise.banking.api.utils.ApiUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateCustomerTest extends BaseApiTest {

    @Test(description = "Verify POST Create Customer API generates a new user")
    public void verifyCreateCustomer() {
        CustomerPayload newCustomer = new CustomerPayload(
                "John", "Doe", "123-45-6789", "johndoe_api", "securePassword123"
        );

        String endpoint = "/customers";
        Response response = ApiUtils.post(requestSpec, endpoint, newCustomer);

        response.then().spec(responseSpec);
        Assert.assertEquals(response.getStatusCode(), 201, "Expected HTTP 201 Created");
        Assert.assertNotNull(response.jsonPath().getString("id"), "New Customer ID should be generated");
    }
}