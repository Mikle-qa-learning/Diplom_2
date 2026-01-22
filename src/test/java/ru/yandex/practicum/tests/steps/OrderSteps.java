package ru.yandex.practicum.tests.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.yandex.practicum.models.OrderRequest;
import ru.yandex.practicum.tests.OrderBase;
import ru.yandex.practicum.tests.Url;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class OrderSteps {

    private final OrderBase orderBase = new OrderBase();

    @Step("Создание заказа без авторизации")
    public Response createOrder(List<String> ingredients) {
        OrderRequest orderRequest = new OrderRequest(ingredients);
        return orderBase.createOrder(orderRequest);
    }

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(List<String> ingredients, String accessToken) {
        OrderRequest orderRequest = new OrderRequest(ingredients);
        return orderBase.createOrderWithAuth(orderRequest, accessToken);
    }

    @Step("Получение случайных ингредиентов")
    public List<String> getRandomIngredients() {
        Response response = RestAssured.given()
                .when()
                .get("https://stellarburgers.education-services.ru" + Url.GET_INGREDIENTS);

        List<String> allIds = response.jsonPath().getList("data._id");
        Random random = new Random();

        int randomIndex1 = random.nextInt(allIds.size());
        int randomIndex2;

        do {
            randomIndex2 = random.nextInt(allIds.size());
        } while (randomIndex2 == randomIndex1);

        String randomId1 = allIds.get(randomIndex1);
        String randomId2 = allIds.get(randomIndex2);

        return List.of(randomId1, randomId2);
    }

    @Step("Создание списка с неверными ID ингредиентов")
    public List<String> createInvalidIngredients() {
        long timestamp = System.currentTimeMillis();
        return Arrays.asList(
                "invalid_id_" + timestamp + "_1",
                "invalid_id_" + timestamp + "_2"
        );
    }

    @Step("Проверка кода ответа")
    public void verifyStatusCode(Response response, int expectedCode) {
        Allure.addAttachment("Статус ответа", response.getStatusLine());
        response.then().statusCode(expectedCode);
    }

    @Step("Проверка поля success в ответе")
    public void verifySuccessField(Response response, boolean expectedValue) {
        Allure.addAttachment("Тело ответа", response.getBody().asString());
        response.then().assertThat().body("success", equalTo(expectedValue));
    }

    @Step("Проверка поля message в ответе")
    public void verifyMessageField(Response response, String expectedMessage) {
        Allure.addAttachment("Тело ответа", response.getBody().asString());
        response.then().assertThat().body("message", equalTo(expectedMessage));
    }

    @Step("Проверка наличия поля name в ответе")
    public void verifyNameFieldNotNull(Response response) {
        Allure.addAttachment("Тело ответа", response.getBody().asString());
        response.then().assertThat().body("name", notNullValue());
    }

    @Step("Проверка наличия поля order в ответе")
    public void verifyOrderFieldNotNull(Response response) {
        Allure.addAttachment("Тело ответа", response.getBody().asString());
        response.then().assertThat().body("order", notNullValue());
    }

    @Step("Проверка наличия поля order.number в ответе")
    public void verifyOrderNumberNotNull(Response response) {
        Allure.addAttachment("Тело ответа", response.getBody().asString());
        response.then().assertThat().body("order.number", notNullValue());
    }

    @Step("Проверка успешного создания заказа")
    public void verifySuccessfulOrderCreation(Response response) {
        verifyStatusCode(response, 200);
        verifySuccessField(response, true);
        verifyNameFieldNotNull(response);
        verifyOrderFieldNotNull(response);
        verifyOrderNumberNotNull(response);
    }

    @Step("Проверка ответа при отсутствии ингредиентов")
    public void verifyNoIngredientsResponse(Response response) {
        verifyStatusCode(response, 400);
        verifySuccessField(response, false);
        verifyMessageField(response, "Ingredient ids must be provided");
    }

    @Step("Проверка ответа при неверном хеше ингредиентов")
    public void verifyInvalidIngredientsResponse(Response response) {
        verifyStatusCode(response, 500);

    }

    @Step("Проверка успешного создания заказа без авторизации")
    public void verifyOrderCreationWithoutAuth(Response response) {
        verifyStatusCode(response, 200);
        verifySuccessField(response, true);
        verifyNameFieldNotNull(response);
        verifyOrderFieldNotNull(response);
        verifyOrderNumberNotNull(response);
    }


}
