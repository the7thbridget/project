package edu.gatech.cs6310.groceryexpress.common.exception;

import org.springframework.dao.DataAccessException;

public class StoreNotFoundException extends DataAccessException {
    public StoreNotFoundException(String msg) {
        super(msg);
    }

    public StoreNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
