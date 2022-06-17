package edu.gatech.cs6310.groceryexpress.DAOImpl;

import edu.gatech.cs6310.groceryexpress.DAO.CustomerDAO;
import edu.gatech.cs6310.groceryexpress.common.UserTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CustomerDAOImpl implements CustomerDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Boolean addCustomerToUser(String username, Integer userType) {
        String sql = "INSERT INTO User (username, user_type) VALUES (?, ?)";
        Object[] args = {username, userType};
        //try {
        return jdbcTemplate.update(sql, args) > 0;
        //} catch (DataAccessException e) {
        //    throw e.getCause();
        //}
    }

    @Override
    public Boolean addCustomerToActiveCustomer(Integer userId, String firstName, String lastName, String phoneNumber, Integer rating, Integer credit) {
        String sql = "INSERT INTO Active_customer (user_id, first_name, last_name, phone_number, rating, credit, last_login) VALUES (?, ?, ?, ?, ?, ?, NULL)";
        Object[] args = {userId, firstName, lastName, phoneNumber, rating, credit};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public List<Map<String, Object>> findAllCustomer() {
        String sql = "SELECT Active_customer.user_id, CONCAT(first_name, '_', last_name) AS name, phone_number, rating, credit, last_login FROM Active_customer INNER JOIN User on Active_customer.user_id = User.user_id ORDER BY User.username ASC";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public Boolean updateCredit(Integer customerId, Integer orderId) {
        String sql = "UPDATE Active_customer SET credit = credit - (SELECT SUM(unit_price * quantity) FROM Order_2_item WHERE order_id = ?) WHERE user_id = ?";
        Object[] args = {orderId, customerId};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public List<Map<String, Object>> getCreditByUserId(Integer userId) {
        String sql = "SELECT credit FROM Active_customer WHERE user_id = ?";
        Object[] args = {userId};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public Boolean deleteActiveCustomer(Integer timePeriod) {
        String sql = "DELETE FROM Active_customer WHERE DATEDIFF(CURRENT_DATE(), last_login) > ?";
        Object[] args = {timePeriod};
        return jdbcTemplate.update(sql, args) > 0;
    }

    public Boolean manualAssignRatingByUserId(Integer userId, Integer manualRating) {
        String sql = "UPDATE Active_customer SET rating = (SELECT rating FROM Rating_orderNum WHERE rating = ?) WHERE user_id = ?";
        Object[] args = {manualRating, userId};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public Boolean retrieveCustomer() {
        String sql = "INSERT INTO Active_customer (user_id, rating, credit, last_login, first_name, last_name, phone_number)\n" +
                "SELECT user_id, rating, credit, last_login, first_name, last_name, phone_number FROM Archived_customer";
        return jdbcTemplate.update(sql) > 0;
    }

    @Override
    public Boolean autoAssignRatingByOrderNum(Integer userId, Integer status) {
        String sql = "UPDATE Active_customer SET rating = (SELECT rating\n" +
                "FROM Rating_orderNum\n" +
                "         JOIN (\n" +
                "    SELECT COALESCE(COUNT(*), 0) AS order_num\n" +
                "    FROM `Order`\n" +
                "    WHERE customer_id = ?\n" +
                "    AND `status` = ?\n" +
                ") calc\n" +
                "WHERE order_num >= Rating_orderNum.lower_bound\n" +
                "  AND (order_num <= Rating_orderNum.upper_bound OR upper_bound IS NULL)\n" +
                "GROUP BY 1) WHERE user_id = ?";
        Object[] args = {userId, status, userId};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public Boolean autoAssignRatingByOrderValue(Integer userId, Integer status) {
        String sql = "UPDATE Active_customer SET rating = (SELECT rating\n" +
                "FROM Rating_orderValue\n" +
                "         JOIN (\n" +
                "    SELECT SUM(quantity * unit_price) AS order_value\n" +
                "    FROM Order_2_item\n" +
                "    WHERE order_id IN (SELECT order_id FROM `Order` WHERE customer_id = ? AND `status` = ?)) order_val\n" +
                "\n" +
                "Where order_value >= Rating_orderValue.lower_bound\n" +
                "  AND (order_value <= Rating_orderValue.upper_bound\n" +
                "    OR upper_bound IS NULL))\n WHERE user_id = ?";
        Object[] args = {userId, status, userId};
        return jdbcTemplate.update(sql, args) > 0;
    }


    public List<Map<String, Object>> findOrderByUserId(Integer userId) {
        String sql = "SELECT * FROM `Order` WHERE customer_id = ?";
        Object[] args = {userId};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> findCustomerRatingAssignRule() {
        String sql = "SELECT customer_rating_rule FROM Setting_criteria LIMIT 1";
        Object[] args = {};
        return jdbcTemplate.queryForList(sql, args);
    }
}
