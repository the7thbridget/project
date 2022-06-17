package edu.gatech.cs6310.groceryexpress.POJO;

import lombok.Data;

@Data
public class Order {
    private String storeName;
    private String orderName;
    private String droneIdentifier;
    private String customerUsername;
}
