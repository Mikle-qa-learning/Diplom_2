package ru.yandex.practicum.tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import ru.yandex.practicum.tests.steps.UserSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class LoginUserTests {
    private UserSteps userSteps;
    private String email;
    private String password;
    private String name;

    @Before
    @Step("Подготовка тестовых данных и создание пользователя")
    public void prepareTestData() {
        userSteps = new UserSteps();

        this.email = "test_user_" + UUID.randomUUID() + "@example.com";
        this.password = "password" + UUID.randomUUID();
        this.name = "Test_User" + UUID.randomUUID();

        Response createResponse = userSteps.createUser(email, password, name);

        if (userSteps.verifyCreationSuccess(createResponse, 200)) {
            userSteps.markAsCreated();
            userSteps.getAccessToken(createResponse);
        }
    }

    @After
    @Step("Очистка данных после теста")
    public void cleanAfterTests() {
        if (!userSteps.isUserCreated()) {
            return;
        }

        String accessToken = userSteps.getAccessToken();
        if (accessToken != null && !accessToken.isEmpty()) {
            Response deleteResponse = userSteps.deleteUser(accessToken);
            userSteps.verifyStatusCode(deleteResponse, 202);
        }

        userSteps.resetCreatedFlag();
    }

    @Test
    @Description("Проверяет успешную авторизацию существующего пользователя")
    public void loginWithExistingUserSuccessfully() {

        Response loginResponse = userSteps.loginUser(email, password);

        userSteps.verifySuccessfulLoginResponse(loginResponse, email, name);

    }

    @Test
    @Description("Проверяет ошибку при попытке входа с неверным паролем")
    public void loginWithWrongPassword() {
        String wrongPassword = "wrong_password_123";

        Response loginResponse = userSteps.loginUser(email, wrongPassword);

        userSteps.verifyUnauthorizedResponse(loginResponse);

    }

    @Test
    @Description("Проверяет ошибку при попытке входа с неверным email")
    public void loginWithWrongEmail() {
        String wrongEmail = "nonexistent_" + UUID.randomUUID() + "@example.com";

        Response loginResponse = userSteps.loginUser(wrongEmail, password);

        userSteps.verifyUnauthorizedResponse(loginResponse);

    }

    @Test
    @Description("Проверяет ошибку при попытке входа с неверным email и паролем")
    public void loginWithWrongEmailAndPassword() {
        String wrongEmail = "wrong_" + UUID.randomUUID() + "@example.com";
        String wrongPassword = "wrong_password_" + UUID.randomUUID();

        Response loginResponse = userSteps.loginUser(wrongEmail, wrongPassword);

        userSteps.verifyUnauthorizedResponse(loginResponse);

    }

    @Test
    @Description("Проверяет ошибку при попытке входа без указания email")
    public void loginWithoutEmail() {

        Response loginResponse = userSteps.loginUser("", password);

        userSteps.verifyUnauthorizedResponse(loginResponse);

    }

    @Test
    @Description("Проверяет ошибку при попытке входа без указания пароля")
    public void loginWithoutPassword() {

        Response loginResponse = userSteps.loginUser(email, "");

        userSteps.verifyUnauthorizedResponse(loginResponse);

    }

    @Test
    @Description("Проверяет ошибку при попытке входа без указания email и пароля")
    public void loginWithoutEmailAndPassword() {
        Response loginResponse = userSteps.loginUser("", "");

        userSteps.verifyUnauthorizedResponse(loginResponse);

    }

}
