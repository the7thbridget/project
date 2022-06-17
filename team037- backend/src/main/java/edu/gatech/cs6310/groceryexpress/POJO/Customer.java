package edu.gatech.cs6310.groceryexpress.POJO;

import lombok.Data;

@Data
public class Customer extends User {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Integer credit;
    private Integer rating;
}
