package edu.gatech.cs6310.groceryexpress;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gatech.cs6310.groceryexpress.POJO.LoginForm;
import edu.gatech.cs6310.groceryexpress.POJO.RegisterForm;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

@SpringBootApplication
@EnableScheduling
public class GroceryExpressApplication implements CommandLineRunner {


    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserService userService;
    private String currUsername;
    private Set<String> functionsAllowed;
    private Integer currUserType;
    private ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        SpringApplication.run(GroceryExpressApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner input = new Scanner(System.in);
        String inputLine;
        System.out.println("Please log in or register...");
        while (input.hasNextLine()) {
            inputLine = input.nextLine();
            System.out.println(">" + inputLine);
            String[] tokens = inputLine.split(",");
            if (tokens[0].equals("stop")) break;
            while (currUsername == null) {
                if (tokens[0].equals("register")) {
                    if (tokens.length != 4) {
                        System.out.println("Invalid argument numbers");
                        break;
                    } else {
                        String username = tokens[1];
                        String password = tokens[2];
                        Integer userType = Integer.parseInt(tokens[3]);
                        RegisterForm registerForm = new RegisterForm(username, password, userType);
                        R res = userService.registerNewUser(registerForm);
                        System.out.println(res);
                        break;
                    }
                } else if (tokens[0].equals("logIn")) {
                    if (tokens.length != 4) {
                        System.out.println("Invalid argument numbers");
                    } else {
                        String username = tokens[1];
                        String password = tokens[2];
                        Integer userType = Integer.parseInt(tokens[3]);
                        LoginForm loginForm = new LoginForm(username, password, userType);
                        R res = userService.loginUser(loginForm);
                        if (res.get("code").equals(200)) {
                            Map<String, Boolean> functionBoolean = (Map<String, Boolean>) res.get("functions");
                            this.currUsername = username;
                            this.currUserType = userType;
                            this.functionsAllowed = functionBoolean.keySet();
                            System.out.println("Log in successfully!");
                            break;
                        } else {
                            System.out.println("Invalid username or password!");
                            break;
                        }
                    }
                }
            }
        }
    }
}
