package ru.yandex.practicum.tests;

import ru.yandex.practicum.models.User;
import io.restassured.response.Response;

public class UserBase extends Base {

    public Response createUser(User user) {
        return doPostRequest(
                Url.HOST_NAME + Url.CREATE_USER,
                user,
                "application/json"
        );
    }

    public Response loginUser(User user) {
        return doPostRequest(
                Url.HOST_NAME + Url.LOGIN_USER,
                user,
                "application/json"
        );
    }

    public Response deleteUser(String accessToken) {
        return doDeleteRequestWithToken(Url.HOST_NAME + Url.DELETE_USER, accessToken);
    }
}