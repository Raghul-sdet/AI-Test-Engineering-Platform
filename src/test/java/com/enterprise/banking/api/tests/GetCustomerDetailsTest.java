package com.enterprise.banking.api.tests;

import com.enterprise.banking.api.base.BaseApiTest;
import com.enterprise.banking.api.utils.ApiUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class GetCustomerDetailsTest extends BaseApiTest {

    @Test(description = "Verify GET Customer Details API returns valid data")
    public void verifyGetCustomerDetails() {
        Map<String, Object> pathParams = new HashMap<>();
        String customerId = "12212"; 
        pathParams.put("customerId", customerId);

        String endpoint = "/customers/{customerId}";
        Response response = ApiUtils.getWithPathParams(requestSpec, endpoint, pathParams);

        response.then().spec(responseSpec);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");
        Assert.assertEquals(response.jsonPath().getString("id"), customerId, "Customer ID mismatch");
        Assert.assertNotNull(response.jsonPath().getString("firstName"), "First name cannot be null");
    }
}