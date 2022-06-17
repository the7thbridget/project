package edu.gatech.cs6310.groceryexpress.common.exception;

import org.springframework.dao.DataAccessException;

public class PilotNotFoundException extends DataAccessException {

    public PilotNotFoundException(String msg) {
        super(msg);
    }

    public PilotNotFoundException() {
        super("Could not find pilot!");
    }
}
