package edu.gatech.cs6310.groceryexpress.common.exception;

import org.springframework.dao.DataAccessException;

public class UserNotFoundException extends DataAccessException {
    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException() {
        super("User not found!");
    }
}
