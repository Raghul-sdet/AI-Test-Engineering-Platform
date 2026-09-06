package com.enterprise.banking.api.tests;

import com.enterprise.banking.api.base.BaseApiTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TransferFundsApiTest extends BaseApiTest {

    @Test(description = "Verify POST Transfer Funds API processes transaction successfully")
    public void verifyTransferFunds() {
        String endpoint = "/transfer";
        Response response = RestAssured.given()
                .spec(requestSpec)
                .queryParam("fromAccountId", "12345")
                .queryParam("toAccountId", "12456")
                .queryParam("amount", "150.00")
                .when()
                .post(endpoint);

        response.then().spec(responseSpec);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");
        
        String responseBody = response.asString();
        Assert.assertTrue(responseBody.contains("Successfully transferred"), "Transfer status validation failed");
    }
}