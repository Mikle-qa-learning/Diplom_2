package ru.yandex.practicum.tests.steps;

import ru.yandex.practicum.models.User;
import ru.yandex.practicum.models.UserResponse;
import ru.yandex.practicum.tests.UserBase;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class UserSteps {

    private final UserBase userBase = new UserBase();
    private boolean isCreated = false;
    private String accessToken;
    private String email;

    @Step("Создание пользователя")
    public Response createUser(String email, String password, String name) {
        this.email = email;
        return userBase.createUser(new User(email, password, name));
    }

    @Step("Логин пользователя")
    public Response loginUser(String email, String password) {
        return userBase.loginUser(new User(email, password));
    }

    @Step("Получение access токена")
    public String getAccessToken(Response response) {
        try {
            UserResponse userResponse = response.body().as(UserResponse.class);
            this.accessToken = userResponse.getAccessToken();
            return this.accessToken;
        } catch (Exception e) {
            return null;
        }
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return userBase.deleteUser(accessToken);
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

    @Step("Проверка успешного создания пользователя")
    public boolean verifyCreationSuccess(Response response, int expectedCode) {
        return response.getStatusCode() == expectedCode;
    }

    @Step("Проверка наличия access токена в ответе")
    public void verifyAccessTokenNotNull(Response response) {
        Allure.addAttachment("Тело ответа", response.getBody().asString());
        response.then().assertThat().body("accessToken", notNullValue());
    }

    @Step("Проверка наличия refresh токена в ответе")
    public void verifyRefreshTokenNotNull(Response response) {
        Allure.addAttachment("Тело ответа", response.getBody().asString());
        response.then().assertThat().body("refreshToken", notNullValue());
    }

    @Step("Проверка данных пользователя в ответе")
    public void verifyUserData(Response response, String expectedEmail, String expectedName) {
        Allure.addAttachment("Тело ответа", response.getBody().asString());
        response.then().assertThat().body("user.email", equalTo(expectedEmail.toLowerCase()));
        response.then().assertThat().body("user.name", equalTo(expectedName));
    }

    @Step("Проверка что accessToken начинается с 'Bearer '")
    public void verifyAccessTokenStartsWithBearer(Response response) {
        Allure.addAttachment("Тело ответа", response.getBody().asString());
        response.then().assertThat().body("accessToken", org.hamcrest.Matchers.startsWith("Bearer "));
    }

    @Step("Проверка структуры ответа при успешном логине")
    public void verifySuccessfulLoginResponse(Response response, String expectedEmail, String expectedName) {
        verifyStatusCode(response, 200);
        verifySuccessField(response, true);
        verifyAccessTokenNotNull(response);
        verifyAccessTokenStartsWithBearer(response);
        verifyRefreshTokenNotNull(response);
        verifyUserData(response, expectedEmail, expectedName);
    }

    @Step("Проверка ответа при неверных учетных данных")
    public void verifyUnauthorizedResponse(Response response) {
        verifyStatusCode(response, 401);
        verifySuccessField(response, false);
        verifyMessageField(response, "email or password are incorrect");
    }

    @Step("Пользователь помечен как созданный")
    public void markAsCreated() {
        this.isCreated = true;
    }

    @Step("Сброс флага создания пользователя")
    public void resetCreatedFlag() {
        this.isCreated = false;
        this.accessToken = null;
        this.email = null;
    }

    public boolean isUserCreated() {
        return this.isCreated;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getEmail() {
        return this.email;
    }
}
