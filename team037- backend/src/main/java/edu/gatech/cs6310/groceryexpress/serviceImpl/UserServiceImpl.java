package edu.gatech.cs6310.groceryexpress.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.gatech.cs6310.groceryexpress.DAO.CacheDAO;
import edu.gatech.cs6310.groceryexpress.DAO.CustomerDAO;
import edu.gatech.cs6310.groceryexpress.DAO.UserDAO;
import edu.gatech.cs6310.groceryexpress.POJO.LoginForm;
import edu.gatech.cs6310.groceryexpress.POJO.RegisterForm;
import edu.gatech.cs6310.groceryexpress.common.ErrorCode;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.common.UserTypeEnum;
import edu.gatech.cs6310.groceryexpress.common.exception.DataConnectionException;
import edu.gatech.cs6310.groceryexpress.common.exception.UserNotFoundException;
import edu.gatech.cs6310.groceryexpress.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final String FUNCTIONS = "functions";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserDAO userDAO;

    @Autowired
    CustomerDAO customerDAO;

    @Autowired
    CacheDAO cacheDAO;

    @Override
    public R displayUser() {
        return null;
    }

    @Override
    public R registerNewUser(RegisterForm registerForm) {
        String username = registerForm.getUsername();
        String initPassword = registerForm.getPassword();
        Integer userType = registerForm.getUserType();
        List<Map<String, Object>> userIdByUsernameAndType = userDAO.findUserIdByUsernameAndType(username, userType);
        if (!userIdByUsernameAndType.isEmpty()) return R.error(ErrorCode.USER_DUPLICATE.getErrorCode(), ErrorCode.USER_DUPLICATE.getErrorMsg());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(initPassword);
        Boolean res = userDAO.registerNewUser(username, encodedPassword, userType);
        return res ? R.ok() : R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
    }

    @Override
    public R loginUser(LoginForm loginForm) throws JsonProcessingException {
        String username = loginForm.getUsername();
        String providedPassword = loginForm.getPassword();
        Integer userType = loginForm.getUserType();
        Map<String, Boolean> functionBooleans = new HashMap<>();
        List<Map<String, Object>> allFunctions = userDAO.findAllFunctions();
        for (Map<String, Object> pair : allFunctions) {
            functionBooleans.put((String) pair.get("function_name"), false);
        }
        Object temp = cacheDAO.readTest(username + "_" + userType);
        Set<String> functions = cacheDAO.checkSession(username, userType);
        Map<String, Object> soleUser = null;
        if (functions == null) {
            List<Map<String, Object>> userList = userDAO.findUserByUsernameAndType(username, userType);
            if (userList.isEmpty()) return R.error(ErrorCode.USER_NOT_FOUND.getErrorCode(), ErrorCode.USER_NOT_FOUND.getErrorMsg());
            soleUser = userList.get(0);
            String storedPassword = (String) soleUser.get("password");
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matches = passwordEncoder.matches(providedPassword, storedPassword);
            if (!matches) return R.error(ErrorCode.USER_NOT_FOUND.getErrorCode(), ErrorCode.USER_NOT_FOUND.getErrorMsg());
            String availableFunctions = (String) soleUser.get(FUNCTIONS);
            String[] funcArray = availableFunctions.split(",");
            Set<String> functionSet = new HashSet<>();
            for (String func : funcArray) {
                functionBooleans.put(func, true);
                functionSet.add(func);
            }
            cacheDAO.writeSession(username, userType, functionSet);
            return R.ok().put("functions", functionBooleans);
        } else {
            for (String func : functions) {
                functionBooleans.put(func, true);
            }
            return R.ok().put("functions", functionBooleans);
        }
    }

    private Integer getUserId(String username) {
        try {
            List<Map<String, Object>> res = userDAO.findUserIdByUsernameAndType(username,UserTypeEnum.CUSTOMER.getUserTypeCode());
            if (res.isEmpty()) throw new UserNotFoundException("User name not found!");
            return (Integer) res.get(0).get("user_id");
        } catch (DataAccessException e) {
            throw new DataConnectionException();
        }
    }

    @Override
    public R findAllFunctionsByUserType(Integer userType) {
        List<Map<String, Object>> allFunctionsByUserType = userDAO.findAllFunctionsByUserType(userType);
        String functions = (String) allFunctionsByUserType.get(0).get("functions");
        List<Map<String, Object>> allFunctions = userDAO.findAllFunctions();
        HashMap<String, Boolean> functionBoolMap = new HashMap<>();
        for (Map<String, Object> eachFunction : allFunctions) {
            String func = (String) eachFunction.get("function_name");
            functionBoolMap.put(func, false);
        }
        String[] funcArray = functions.split(",");
        for (String str : funcArray) {
            functionBoolMap.put(str, true);
        }
        return R.ok().put("functions", functionBoolMap);
    }

}
