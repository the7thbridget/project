package edu.gatech.cs6310.groceryexpress.common;

public enum ErrorCode implements IErrorCode{
    STORE_NOT_FOUND(1000, "Store not found!"),
    STORE_DUPLICATE(1100, "Duplicate store exists!"),
    UNKNOWN_ERROR(500, "Unknown error!"),
    PILOT_NOT_FOUND(2000, "Pilot not found!"),
    PILOT_ACCOUNT_DUPLICATE(2100, "Pilot account duplicate!"),
    PILOT_LICENSE_DUPLICATE(2200, "Pilot licenseId duplicated!"),
    PILOT_TAX_DUPLICATE(2300, "Pilot taxId duplicated!"),
    PILOT_OCCUPIED(2300, "Pilot already assigned a drone!"),
    DRONE_NOT_FOUND(3000, "Drone not found!"),
    DRONE_DUPLICATE(3100, "Drone duplicated!"),
    DRONE_HAS_UNDELIVERED_ORDER(3200, "Maintain request failed. Drone has undelivered order!"),
    USER_NOT_FOUND(4000, "Invalid username or password!"),
    CUSTOMER_NOT_FOUND(3500, "Customer not found!"),
    //CUSTOMER_NOT_FOUND(4000, "User not found!"),
    USER_DUPLICATE(4100, "Duplicate user found!"),
    ORDER_NOT_FOUND(5000, "Order not found!"),
    ORDER_DUPLICATE(5100, "Order duplicate!"),
    ORDER_ALREADY_COMPLETE(5200, "Order already completed!"),
    NOT_ENOUGH_CREDIT(6000, "The user does not have enough credit!"),
    NOT_ENOUGH_CAPACITY(6100, "The drone does not have enough capacity!"),
    DRONE_NOT_HAVE_ENOUGH_TRIPS(7000, "Drone does not have enough trips!"),
    DRONE_NEEDS_PILOT(7100, "The drone needs pilot!"),
    UNABLE_TO_CANCEL_ORDER(7200, "Unable to cancel the order!"),
    ITEM_NOT_FOUND(8000, "Item not found!"),
    ITEM_DUPLICATE(8100, "Duplicate item exists!"),
    ITEM_ALREADY_ORDERED(8200, "Item already ordered with a different price!"),
    USER_TYPE_NOT_SUPPORTED(9000, "User type not supported!")
    ;

    ErrorCode(Integer errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    Integer errorCode;
    String msg;

    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }
}
