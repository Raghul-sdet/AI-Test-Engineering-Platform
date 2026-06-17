package com.enterprise.banking.api;

import com.enterprise.banking.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class AccountApiTest {

    @BeforeClass
    public void setupApi() {
        RestAssured.baseURI = ConfigReader.getProperty("apiBaseUrl");
    }

    @Test
    public void verifyGetAccountsApi() {
        int customerId = 12212; // Standard Parabank Test ID
        
        Response response = given()
                .header("Accept", "application/json")
                .when()
                .get("/customers/" + customerId + "/accounts")
                .then()
                .extract().response();

        System.out.println("API Response: " + response.getBody().asString());
        
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        Assert.assertTrue(response.jsonPath().getList("$").size() > 0, "Expected accounts list to be populated");
    }
}