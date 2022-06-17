package edu.gatech.cs6310.groceryexpress.POJO;

import lombok.Data;

@Data
public class LoginForm {
    private String username;
    private String password;
    private Integer userType;

    public LoginForm(String username, String password, Integer userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
}
