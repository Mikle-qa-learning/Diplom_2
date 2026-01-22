package ru.yandex.practicum.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class Base {

    public Response doPostRequest(String url, Object requestBody, String contentType) {
        return given(baseRequest(contentType))
                .body(requestBody)
                .when()
                .post(url);
    }

    public Response doDeleteRequest(String url) {
        return given(baseRequest())
                .delete(url);
    }

    public Response doDeleteRequestWithToken(String url, String accessToken) {
        return given(baseRequest())
                .header("Authorization", accessToken)
                .when()
                .delete(url);
    }

    protected RequestSpecification baseRequest(String... headers) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .setRelaxedHTTPSValidation();

        if (headers.length > 0 && headers[0] != null) {
            builder.addHeader("Content-type", headers[0]);
        }

        return builder.build();
    }
}
