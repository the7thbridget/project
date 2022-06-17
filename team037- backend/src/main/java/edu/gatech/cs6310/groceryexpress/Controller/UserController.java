package edu.gatech.cs6310.groceryexpress.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.gatech.cs6310.groceryexpress.POJO.LoginForm;
import edu.gatech.cs6310.groceryexpress.POJO.RegisterForm;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    UserService userService;


    @PostMapping("/api/user/register")
    public R register(@RequestBody RegisterForm registerForm) {
        return userService.registerNewUser(registerForm);
    }

    @PostMapping("/api/user/login")
    public R logIn(@RequestBody LoginForm loginForm) throws JsonProcessingException {
        return userService.loginUser(loginForm);
    }

    @GetMapping("api/user/func/type")
    public R findAllTypesByType(@RequestParam Integer userType) {
        return userService.findAllFunctionsByUserType(userType);
    }
}
