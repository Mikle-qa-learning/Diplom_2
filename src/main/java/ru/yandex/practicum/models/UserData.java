package ru.yandex.practicum.models;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    public UserData() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}