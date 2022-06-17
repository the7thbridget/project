package edu.gatech.cs6310.groceryexpress.common.exception;

import org.springframework.dao.DataAccessException;

public class DroneNotEnoughRemainingTripException extends DataAccessException {
    public DroneNotEnoughRemainingTripException(String msg) {
        super(msg);
    }

    public DroneNotEnoughRemainingTripException() {
        super("Drone does not have enough remaining trip.");
    }
}
