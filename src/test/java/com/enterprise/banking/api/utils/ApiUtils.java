package com.enterprise.banking.api.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class ApiUtils {

    public static Response get(RequestSpecification requestSpec, String endpoint) {
        return RestAssured.given().spec(requestSpec).when().get(endpoint);
    }

    public static Response getWithPathParams(RequestSpecification requestSpec, String endpoint, Map<String, Object> pathParams) {
        return RestAssured.given().spec(requestSpec).pathParams(pathParams).when().get(endpoint);
    }

    public static Response post(RequestSpecification requestSpec, String endpoint, Object payload) {
        return RestAssured.given().spec(requestSpec).body(payload).when().post(endpoint);
    }
}