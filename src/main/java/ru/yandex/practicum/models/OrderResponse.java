package ru.yandex.practicum.models;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {
    @SerializedName("name")
    private String name;

    @SerializedName("order")
    private OrderDetails order;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public OrderResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderDetails getOrder() {
        return order;
    }

    public void setOrder(OrderDetails order) {
        this.order = order;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "name='" + name + '\'' +
                ", order=" + order +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }

    public static class OrderDetails {
        @SerializedName("number")
        private Integer number;

        public OrderDetails() {
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return "OrderDetails{" +
                    "number=" + number +
                    '}';
        }
    }
}
