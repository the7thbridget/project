package edu.gatech.cs6310.groceryexpress.serviceImpl;

import edu.gatech.cs6310.groceryexpress.DAO.ArchivedCustomerDAO;
import edu.gatech.cs6310.groceryexpress.DAO.CustomerDAO;
import edu.gatech.cs6310.groceryexpress.DAO.UserDAO;
import edu.gatech.cs6310.groceryexpress.POJO.Customer;
import edu.gatech.cs6310.groceryexpress.common.ErrorCode;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.common.UserTypeEnum;
import edu.gatech.cs6310.groceryexpress.common.OrderStatusCode;
import edu.gatech.cs6310.groceryexpress.common.exception.*;
import edu.gatech.cs6310.groceryexpress.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import java.sql.SQLException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerDAO customerDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    ArchivedCustomerDAO archivedCustomerDAO;


    @Override
    @Transactional(rollbackFor = {DataAccessException.class})
    public R addCustomer(Customer customer) {
        try {
            String username = customer.getUsername();
            String firstName = customer.getFirstName();
            String lastName = customer.getLastName();
            String phoneNumber = customer.getPhoneNumber();
            Integer credit = customer.getCredit();
            Integer rating = customer.getRating();
            List<Map<String, Object>> customerByUsername = userDAO.findUserIdByUsernameAndType(username, UserTypeEnum.CUSTOMER.getUserTypeCode());
            if (!customerByUsername.isEmpty()) return R.error(ErrorCode.USER_DUPLICATE.getErrorCode(), ErrorCode.USER_DUPLICATE.getErrorMsg());
            boolean res = customerDAO.addCustomerToUser(username, UserTypeEnum.CUSTOMER.getUserTypeCode());
            boolean res2 = customerDAO.addCustomerToActiveCustomer(getUserId(username), firstName, lastName, phoneNumber, rating, credit);
            return res && res2 ? R.ok() : R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
        } catch (DataAccessException e) {
            return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), e.getMessage());
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
    public R displayCustomerData() {
        try {
            Map<String, List<Map<String, Object>>> allCustomer = new HashMap();
            allCustomer.put("currentCustomers", customerDAO.findAllCustomer());
            allCustomer.put("archivedCustomers", archivedCustomerDAO.findAllArchivedCustomer());
            return R.ok().put("customers", allCustomer);
        } catch (DataAccessException e) {
            return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), "Unable to fetch customers!");
        }
    }

    /*
    @Override
    public R showCustomerData(Integer archiveData) {
        try {
            List<Map<String, Object>> allData = new ArrayList<>();
            switch (archiveData) {
                case 0:
                    break;
                case 1:
                    allData = archivedCustomerDAO.findAllArchivedCustomer();
                    break;
            }
            return R.ok().put("archivedCustomerData", allData);
        } catch (DataAccessException e) {
            return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), "Unable to fetch archived customers!");
        }
    }
     */

    public R manualReassignCustomerRating(Integer user_id, Integer manualRating) throws SQLException{
        // Assume only able to reassign ratings for active customers
        // TODO: catch create sql exception in case input bad value
        try {
            boolean ans = customerDAO.manualAssignRatingByUserId(user_id, manualRating);
            return ans ? R.ok() : R.error(ErrorCode.CUSTOMER_NOT_FOUND.getErrorCode(), ErrorCode.CUSTOMER_NOT_FOUND.getErrorMsg());
        } catch (DataAccessException e) {
            SQLException exception = (SQLException) e.getCause();
            throw exception;
        }
    }

    @Override
    public R autoReassignRating(Integer userId) {
        // Assume only able to reassign ratings for active customers
        // assignRule: byOrderNum, byOrderValue in the most recent year
        boolean ans = new Boolean(null);
        List<Map<String, Object>> customerRatingAssignRuleRes = customerDAO.findCustomerRatingAssignRule();
        if (customerRatingAssignRuleRes.isEmpty()) throw new OrderNotFoundException("Drone assign rule for order not found!");
        Integer assignRule = (Integer) customerRatingAssignRuleRes.get(0).get("customer_rating_rule");

        switch (assignRule) {
            case 1: {
                ans = customerDAO.autoAssignRatingByOrderNum(userId, OrderStatusCode.COMPLETE.getOrderStatusCode());
                break;
            }
            case 2: {
                ans = customerDAO.autoAssignRatingByOrderValue(userId, OrderStatusCode.COMPLETE.getOrderStatusCode());
                break;
            }
        }
        return ans ? R.ok() : R.error(ErrorCode.CUSTOMER_NOT_FOUND.getErrorCode(), ErrorCode.CUSTOMER_NOT_FOUND.getErrorMsg());
    }
}
