package edu.gatech.cs6310.groceryexpress.DAOImpl;

import edu.gatech.cs6310.groceryexpress.DAO.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserDAOImpl implements UserDAO {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> findUserIdByUsernameAndType(String username, Integer userType) {
        String sql = "SELECT user_id FROM User WHERE username = ? AND user_type = ?";
        Object[] args = {username, userType};
        logger.info("Checked MYSQL for user info...");
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public Boolean registerNewUser(String username, String password, Integer userType) {
        String sql = "INSERT INTO User (username, password, user_type) VALUES (?, ?, ?)";
        Object[] args = {username, password, userType};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public List<Map<String, Object>> findUserByUsernameAndType(String username, Integer userType) {
        String sql = "SELECT username, user_type, GROUP_CONCAT(function_name) AS functions, password FROM User\n" +
                "LEFT JOIN (\n" +
                "    SELECT user_type_id, function_name FROM\n" +
                "    User_type_2_function\n" +
                "    LEFT JOIN `Function` F on User_type_2_function.function_id = F.function_id\n" +
                "    ) AS type2name\n" +
                "ON User.user_type = type2name.user_type_id\n" +
                "WHERE username = ? AND user_type = ?\n"+
                "GROUP BY user_id\n" ;
        Object[] args = {username, userType};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> findAllFunctions() {
        String sql = "SELECT function_id, function_name FROM `Function`";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> findAllFunctionsByUserType(Integer userType) {
        String sql = "SELECT user_type_name, GROUP_CONCAT(function_name) AS functions FROM User_type\n" +
                "LEFT JOIN User_type_2_function ON User_type.user_type_id = User_type_2_function.user_type_id\n" +
                "LEFT JOIN `Function` F on User_type_2_function.function_id = F.function_id\n" +
                "WHERE User_type.user_type_id = ?\n" +
                "GROUP BY user_type_name";
        Object[] args = {userType};
        return jdbcTemplate.queryForList(sql, args);
    }
}
