package com.enterprise.banking.api.base;

import com.enterprise.banking.api.utils.RestAssuredListener;
import com.enterprise.banking.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeSuite;

public class BaseApiTest {

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;

    @BeforeSuite(alwaysRun = true)
    public void setupApiEnvironment() {
        // Initialize base URI from properties file
        RestAssured.baseURI = ConfigReader.getProperty("apiBaseUrl");

        // Define global request specifications including Extent Reports logging
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new RestAssuredListener())
                .build();

        // Define global response specifications (e.g., SLA response time < 5 seconds)
        responseSpec = new ResponseSpecBuilder()
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();
    }
}