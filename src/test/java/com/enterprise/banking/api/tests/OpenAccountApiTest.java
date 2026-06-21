package com.enterprise.banking.api.tests;

import com.enterprise.banking.api.base.BaseApiTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OpenAccountApiTest extends BaseApiTest {

    @Test(description = "Verify POST Open Account API creates a new account")
    public void verifyOpenAccount() {
        String customerId = "12212";
        String accountType = "1"; // 0 = CHECKING, 1 = SAVINGS
        String fromAccountId = "19005";

        String endpoint = "/createAccount";

        Response response = RestAssured.given()
                .spec(requestSpec)
                .queryParam("customerId", customerId)
                .queryParam("newAccountType", accountType)
                .queryParam("fromAccountId", fromAccountId)
                .when()
                .post(endpoint);

        response.then().spec(responseSpec);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");
        Assert.assertNotNull(response.jsonPath().getString("id"), "New Account ID should be generated");
        Assert.assertEquals(response.jsonPath().getString("customerId"), customerId, "Customer ID mismatch");
    }
}