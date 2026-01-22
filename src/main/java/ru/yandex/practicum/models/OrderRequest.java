package ru.yandex.practicum.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderRequest {
    @SerializedName("ingredients")
    private List<String> ingredients;

    public OrderRequest() {
    }

    public OrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "ingredients=" + ingredients +
                '}';
    }
}
