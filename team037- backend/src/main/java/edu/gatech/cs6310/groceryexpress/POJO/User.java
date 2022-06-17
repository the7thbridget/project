package edu.gatech.cs6310.groceryexpress.POJO;

import lombok.Data;

@Data
public class User {
    private String username;
    private String password;
    private Integer userType;
}
