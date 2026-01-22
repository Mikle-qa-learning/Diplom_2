package ru.yandex.practicum.models;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("user")
    private UserData user;

    @SerializedName("message")
    private String message;

    public UserResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "success=" + success +
                ", accessToken='" + (accessToken != null ? "[TOKEN]" : "null") + '\'' +
                ", refreshToken='" + (refreshToken != null ? "[TOKEN]" : "null") + '\'' +
                ", user=" + user +
                ", message='" + message + '\'' +
                '}';
    }
}