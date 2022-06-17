package edu.gatech.cs6310.groceryexpress.common;

public enum OrderStatusCode implements IOrderStatus{
    PENDING(0, "pending order"),
    COMPLETE(1, "complete delivery"),
    CANCEL(2, "order canceled");

    Integer code;
    String status;

    private OrderStatusCode(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    @Override
    public Integer getOrderStatusCode() {
        return code;
    }

    @Override
    public String getOrderStatus() {
        return status;
    }
}
