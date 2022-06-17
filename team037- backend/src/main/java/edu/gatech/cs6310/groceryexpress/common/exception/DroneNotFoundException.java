package edu.gatech.cs6310.groceryexpress.common.exception;

import org.springframework.dao.DataAccessException;

public class DroneNotFoundException extends DataAccessException {
    public DroneNotFoundException(String msg) {
        super(msg);
    }

    public DroneNotFoundException() {
        super("Drone is not found!");
    }
}
