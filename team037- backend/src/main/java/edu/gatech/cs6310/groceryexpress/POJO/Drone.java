package edu.gatech.cs6310.groceryexpress.POJO;

import lombok.Data;

@Data
public class Drone {
    private Integer droneId;
    private String droneIdentifier;
    private Integer storeId;
    private String storeName;
    private Integer maxCapacity;
    private Integer maxTrip;
    private Integer remainingTrip;

}
