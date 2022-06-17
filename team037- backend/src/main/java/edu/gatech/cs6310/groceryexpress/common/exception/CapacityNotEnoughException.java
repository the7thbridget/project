package edu.gatech.cs6310.groceryexpress.common.exception;

import org.springframework.dao.DataAccessException;


public class CapacityNotEnoughException extends DataAccessException {
    public CapacityNotEnoughException(String msg) {
        super(msg);
    }

    public CapacityNotEnoughException() {
        super("The drone does not have enough capacity!");
    }
}
