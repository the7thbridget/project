package edu.gatech.cs6310.groceryexpress.common.exception;

import org.springframework.dao.DataAccessException;

public class CreditNotEnoughException extends DataAccessException {
    public CreditNotEnoughException(String msg) {
        super(msg);
    }

    public CreditNotEnoughException() {
        super("The customer does not have enough credits!");
    }
}
