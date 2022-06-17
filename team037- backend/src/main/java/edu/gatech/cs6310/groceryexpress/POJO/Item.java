package edu.gatech.cs6310.groceryexpress.POJO;

import lombok.Data;

@Data
public class Item {
    private String storeName;
    private String itemName;
    private Integer weight;
}
