package com.enterprise.banking.api.tests;

import com.enterprise.banking.api.base.BaseApiTest;
import com.enterprise.banking.api.payloads.TransferPayload;
import com.enterprise.banking.api.utils.ApiUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TransferFundsApiTest extends BaseApiTest {

    @Test(description = "Verify POST Transfer Funds API processes transaction successfully")
    public void verifyTransferFunds() {
        TransferPayload transferData = new TransferPayload("19005", "19116", 150.00);

        String endpoint = "/transfer";
        Response response = ApiUtils.post(requestSpec, endpoint, transferData);

        response.then().spec(responseSpec);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 OK");
        
        String status = response.jsonPath().getString("status");
        Assert.assertEquals(status, "Successfully transferred", "Transfer status validation failed");
    }
}