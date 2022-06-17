package edu.gatech.cs6310.groceryexpress.common.exception;

import org.springframework.dao.DataAccessException;

public class DuplicateUserException extends DataAccessException {
    public DuplicateUserException(String msg) {
        super(msg);
    }

    public DuplicateUserException() {
        super("Duplicate users exist!");
    }
}
