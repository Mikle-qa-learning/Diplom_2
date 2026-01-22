package ru.yandex.practicum.tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import ru.yandex.practicum.tests.steps.UserSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class CreateUserTests {
    private UserSteps userSteps;
    private String email;
    private String password;
    private String name;

    @Before
    @Step("Подготовка тестовых данных")
    public void prepareTestData() {
        userSteps = new UserSteps();
        this.email = "test_user_" + UUID.randomUUID() + "@example.com";
        this.password = "password" + UUID.randomUUID();
        this.name = "Test_User" + UUID.randomUUID();
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
    @Description("Проверяет успешную регистрацию пользователя с уникальными данными")
    public void createUniqueUserSuccessfully() {

        Response createResponse = userSteps.createUser(email, password, name);

        userSteps.getAccessToken(createResponse);

        userSteps.verifyStatusCode(createResponse, 200);
        userSteps.verifySuccessField(createResponse, true);
        userSteps.verifyAccessTokenNotNull(createResponse);
        userSteps.verifyRefreshTokenNotNull(createResponse);
        userSteps.verifyUserData(createResponse, email, name);
        userSteps.verifyAccessTokenStartsWithBearer(createResponse);

        if (userSteps.verifyCreationSuccess(createResponse, 200)) {
            userSteps.markAsCreated();
        }

    }

    @Test
    @Description("Проверяет ошибку при попытке зарегистрировать пользователя с уже существующим email")
    public void createAlreadyRegisteredUserError() {
        Response firstCreateResponse = userSteps.createUser(email, password, name);
        userSteps.verifyStatusCode(firstCreateResponse, 200);
        userSteps.verifySuccessField(firstCreateResponse, true);

        if (userSteps.verifyCreationSuccess(firstCreateResponse, 200)) {
            userSteps.markAsCreated();
            userSteps.getAccessToken(firstCreateResponse);
        }

        Response secondCreateResponse = userSteps.createUser(email, "differentPassword123", "Different Name");

        userSteps.verifyStatusCode(secondCreateResponse, 403);
        userSteps.verifySuccessField(secondCreateResponse, false);
        userSteps.verifyMessageField(secondCreateResponse, "User already exists");

    }

    @Test
    @Description("Проверяет ошибку при регистрации пользователя без указания email")
    public void createUserWithoutEmail() {

        Response createResponse = userSteps.createUser("", password, name);


        userSteps.verifyStatusCode(createResponse, 403);
        userSteps.verifySuccessField(createResponse, false);
        userSteps.verifyMessageField(createResponse, "Email, password and name are required fields");

    }

    @Test
    @Description("Проверяет ошибку при регистрации пользователя без указания пароля")
    public void createUserWithoutPassword() {

        Response createResponse = userSteps.createUser(email, "", name);

        userSteps.verifyStatusCode(createResponse, 403);
        userSteps.verifySuccessField(createResponse, false);
        userSteps.verifyMessageField(createResponse, "Email, password and name are required fields");

    }

    @Test
    @Description("Проверяет ошибку при регистрации пользователя без указания имени")
    public void createUserWithoutName() {

        Response createResponse = userSteps.createUser(email, password, "");

        userSteps.verifyStatusCode(createResponse, 403);
        userSteps.verifySuccessField(createResponse, false);
        userSteps.verifyMessageField(createResponse, "Email, password and name are required fields");

    }

    @Test
    @Description("Проверяет ошибку при регистрации пользователя с пустыми всеми полями")
    public void createUserWithAllEmptyFields() {

        Response createResponse = userSteps.createUser("", "", "");


        userSteps.verifyStatusCode(createResponse, 403);
        userSteps.verifySuccessField(createResponse, false);
        userSteps.verifyMessageField(createResponse, "Email, password and name are required fields");

    }
}