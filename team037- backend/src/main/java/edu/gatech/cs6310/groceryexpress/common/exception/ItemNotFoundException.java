package edu.gatech.cs6310.groceryexpress.common.exception;

import org.springframework.dao.DataAccessException;

public class ItemNotFoundException extends DataAccessException {
    public ItemNotFoundException(String msg) {
        super(msg);
    }
    public ItemNotFoundException() {
        super("Could not find this order!");
    }
}
