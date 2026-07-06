package com.enterprise.banking.api.utils;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enterprise-grade RestAssured Filter for centralized API logging.
 * Captures HTTP requests and responses, securely logging them to both the console 
 * via SLF4J and the reporting dashboards (like Allure) without compromising thread safety.
 */
public class RestAssuredListener implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestAssuredListener.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, 
                           FilterableResponseSpecification responseSpec, 
                           FilterContext filterContext) {
        
        // Log the outgoing HTTP Request
        logRequestDetails(requestSpec);

        // Execute the actual HTTP call
        Response response = filterContext.next(requestSpec, responseSpec);

        // Log the incoming HTTP Response
        logResponseDetails(response);

        return response;
    }

    /**
     * Extracts and logs critical parameters from the outgoing HTTP request.
     *
     * @param requestSpec The filtered request specification.
     */
    private void logRequestDetails(FilterableRequestSpecification requestSpec) {
        String requestMethod = requestSpec.getMethod();
        String requestUri = requestSpec.getURI();
        String requestBody = requestSpec.getBody() != null ? requestSpec.getBody().toString() : "No Body";

        LOGGER.info("API REQUEST -> Method: {} | URI: {}", requestMethod, requestUri);
        LOGGER.debug("Request Headers: \n{}", requestSpec.getHeaders());
        LOGGER.debug("Request Body: \n{}", requestBody);

        // Attach request details to Allure report dynamically
        Allure.addAttachment("API Request - " + requestMethod, 
                "text/plain", 
                String.format("URI: %s\nHeaders: %s\nBody:\n%s", requestUri, requestSpec.getHeaders(), requestBody));
    }

    /**
     * Extracts and logs critical parameters from the incoming HTTP response.
     *
     * @param response The executed API response.
     */
    private void logResponseDetails(Response response) {
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody() != null ? response.getBody().asPrettyString() : "No Body";

        LOGGER.info("API RESPONSE <- Status Code: {}", statusCode);
        LOGGER.debug("Response Headers: \n{}", response.getHeaders());
        LOGGER.debug("Response Body: \n{}", responseBody);

        // Attach response details to Allure report dynamically
        Allure.addAttachment("API Response - " + statusCode, 
                "application/json", 
                responseBody);
    }
}