package edu.gatech.cs6310.groceryexpress.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.gatech.cs6310.groceryexpress.POJO.LoginForm;
import edu.gatech.cs6310.groceryexpress.POJO.RegisterForm;
import edu.gatech.cs6310.groceryexpress.common.R;

public interface UserService {
    R displayUser();
    R registerNewUser(RegisterForm registerForm);
    R loginUser(LoginForm loginForm) throws JsonProcessingException;
    R findAllFunctionsByUserType(Integer userType);
}
