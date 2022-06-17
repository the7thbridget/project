package edu.gatech.cs6310.groceryexpress.common.exception;

import org.springframework.dao.DataAccessException;

public class UnableToCancelException extends DataAccessException {
    public UnableToCancelException(String msg) {
        super(msg);
    }

    public UnableToCancelException() {
        super("Unable to cancel the order!");
    }
}
