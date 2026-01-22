package ru.yandex.practicum.tests;

import ru.yandex.practicum.models.OrderRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderBase extends Base {

    public Response createOrder(OrderRequest orderRequest) {
        return doPostRequest(
                Url.HOST_NAME + Url.CREATE_ORDER,
                orderRequest,
                "application/json"
        );
    }

    public Response createOrderWithAuth(OrderRequest orderRequest, String accessToken) {
        return given(baseRequest("application/json"))
                .header("Authorization", accessToken)
                .body(orderRequest)
                .when()
                .post(Url.HOST_NAME + Url.CREATE_ORDER);
    }
}