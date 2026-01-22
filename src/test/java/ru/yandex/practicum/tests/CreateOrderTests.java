package ru.yandex.practicum.tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import ru.yandex.practicum.tests.steps.OrderSteps;
import ru.yandex.practicum.tests.steps.UserSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class CreateOrderTests {
    private OrderSteps orderSteps;
    private UserSteps userSteps;
    private String accessToken;
    private List<String> randomIngredients;

    @Before
    @Step("Подготовка тестовых данных и логин пользователя")
    public void prepareTestData() {
        orderSteps = new OrderSteps();
        userSteps = new UserSteps();

        String email = "test_user_" + UUID.randomUUID() + "@example.com";
        String password = "password" + UUID.randomUUID();
        String name = "Test_User" + UUID.randomUUID();

        Response createResponse = userSteps.createUser(email, password, name);

        if (userSteps.verifyCreationSuccess(createResponse, 200)) {
            userSteps.markAsCreated();

            Response loginResponse = userSteps.loginUser(email, password);
            userSteps.verifyStatusCode(loginResponse, 200);
            userSteps.verifySuccessField(loginResponse, true);

            this.accessToken = userSteps.getAccessToken(loginResponse);

            if (this.accessToken == null || this.accessToken.isEmpty()) {
                throw new RuntimeException("Не удалось получить access token при логине");
            }
        } else {
            throw new RuntimeException("Не удалось создать пользователя для теста");
        }

        this.randomIngredients = orderSteps.getRandomIngredients();
    }

    @After
    @Step("Очистка данных после теста")
    public void cleanAfterTests() {
        if (!userSteps.isUserCreated()) {
            return;
        }

        if (accessToken != null && !accessToken.isEmpty()) {
            try {
                Response deleteResponse = userSteps.deleteUser(accessToken);
                userSteps.verifyStatusCode(deleteResponse, 202);
            } catch (Exception e) {
                System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
            }
        }

        userSteps.resetCreatedFlag();
    }

    @Test
    @Description("1. Создание заказа с авторизацией и с ингредиентами ")
    public void createOrderWithAuthAndIngredientsSuccessfully() {

        Response orderResponse = orderSteps.createOrderWithAuth(randomIngredients, accessToken);
        orderSteps.verifySuccessfulOrderCreation(orderResponse);

    }

    @Test
    @Description("2. Создание заказа с авторизацией без ингредиентов ")
    public void createOrderWithAuthWithoutIngredientsError() {

        Response orderResponse = orderSteps.createOrderWithAuth(List.of(), accessToken);
        orderSteps.verifyNoIngredientsResponse(orderResponse);

    }

    @Test
    @Description("3. Создание заказа с авторизацией и неверным id ингредиентов ")
    public void createOrderWithAuthAndInvalidIngredientIdsError() {

        List<String> invalidIngredients = orderSteps.createInvalidIngredients();

        Response orderResponse = orderSteps.createOrderWithAuth(invalidIngredients, accessToken);
        orderSteps.verifyInvalidIngredientsResponse(orderResponse);

    }

    @Test
    @Description("4. Создание заказа без авторизации с ингредиентами ")
    public void createOrderWithoutAuthWithIngredients() {

        Response orderResponse = orderSteps.createOrder(randomIngredients);
        orderSteps.verifyOrderCreationWithoutAuth(orderResponse);

    }

    @Test
    @Description("5. Создание заказа без авторизации без ингредиентов ")
    public void createOrderWithoutAuthWithoutIngredientsError() {

        Response orderResponse = orderSteps.createOrder(List.of());
        orderSteps.verifyNoIngredientsResponse(orderResponse);

    }

    @Test
    @Description("6. Создание заказа без авторизации с неверным id ингредиентов ")
    public void createOrderWithoutAuthWithInvalidIngredientIdsError() {

        List<String> invalidIngredients = orderSteps.createInvalidIngredients();

        Response orderResponse = orderSteps.createOrder(invalidIngredients);
        orderSteps.verifyInvalidIngredientsResponse(orderResponse);

    }
}
