package com.enterprise.banking.api.tests;

import com.enterprise.banking.api.base.BaseApiTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateCustomerTest extends BaseApiTest {

    @Test(description = "Verify GET Login API for demo customer")
    public void verifyCreateCustomer() {
        String endpoint = "/login/john/demo";
        Response response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(endpoint);

        response.then().spec(responseSpec);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");
        Assert.assertEquals(response.jsonPath().getString("id"), "12212", "Customer ID mismatch");
    }
}