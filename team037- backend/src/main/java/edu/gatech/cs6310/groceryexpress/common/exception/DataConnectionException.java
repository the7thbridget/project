package edu.gatech.cs6310.groceryexpress.common.exception;

import org.springframework.dao.DataAccessException;

public class DataConnectionException extends DataAccessException {
    public DataConnectionException(String msg) {
        super(msg);
    }

    public DataConnectionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DataConnectionException() {
        super("Unable to connect to database!");
    }
}
