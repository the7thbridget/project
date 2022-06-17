package edu.gatech.cs6310.groceryexpress.DAO;

import java.util.List;
import java.util.Map;

public interface UserDAO {
    List<Map<String, Object>> findUserIdByUsernameAndType(String username, Integer userType);
    Boolean registerNewUser(String username, String password, Integer userType);
    List<Map<String, Object>> findUserByUsernameAndType(String username, Integer userType);
    List<Map<String, Object>> findAllFunctions();
    List<Map<String, Object>> findAllFunctionsByUserType(Integer userType);
}
