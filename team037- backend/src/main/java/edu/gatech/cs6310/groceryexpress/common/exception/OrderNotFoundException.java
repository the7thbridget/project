package edu.gatech.cs6310.groceryexpress.common.exception;

import org.springframework.dao.DataAccessException;

public class OrderNotFoundException extends DataAccessException {
    public OrderNotFoundException(String msg) {
        super(msg);
    }

    public OrderNotFoundException() {
        super("Could not find this order!");
    }
}
