package edu.gatech.cs6310.groceryexpress.POJO;

import lombok.Data;

@Data
public class RegisterForm {
    String username;
    String password;
    Integer userType;

    public RegisterForm(String username, String password, Integer userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
}
