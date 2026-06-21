package com.enterprise.banking.api.utils;

import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.enterprise.banking.listeners.TestListener;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RestAssuredListener implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);

        if (TestListener.test.get() != null) {
            // Log Request
            TestListener.test.get().info("<b>API REQUEST</b>");
            TestListener.test.get().info("URI: " + requestSpec.getURI());
            TestListener.test.get().info("Method: " + requestSpec.getMethod());
            
            if (requestSpec.getBody() != null) {
                TestListener.test.get().info(MarkupHelper.createCodeBlock(requestSpec.getBody().toString(), CodeLanguage.JSON));
            }

            // Log Response
            TestListener.test.get().info("<b>API RESPONSE</b>");
            TestListener.test.get().info("Status Code: " + response.getStatusCode());
            TestListener.test.get().info("Response Time: " + response.getTime() + " ms");
            
            if (response.getBody() != null) {
                TestListener.test.get().info(MarkupHelper.createCodeBlock(response.getBody().asPrettyString(), CodeLanguage.JSON));
            }
        }
        return response;
    }
}