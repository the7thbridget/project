package edu.gatech.cs6310.groceryexpress.DAO;

import java.util.List;
import java.util.Map;

public interface CustomerDAO {
    Boolean addCustomerToUser(String username, Integer userType);
    Boolean addCustomerToActiveCustomer(Integer userId, String firstName, String lastName, String phoneNumber, Integer rating, Integer credit);
    //Boolean addCustomerToActiveCustomer(Integer userId);
    List<Map<String, Object>> findAllCustomer();
    Boolean updateCredit(Integer customerId, Integer orderId);
    List<Map<String, Object>> getCreditByUserId(Integer userId);
    Boolean deleteActiveCustomer(Integer timePeriod);
    Boolean retrieveCustomer();
    Boolean manualAssignRatingByUserId(Integer userId, Integer manualRating);
    Boolean autoAssignRatingByOrderNum(Integer userId, Integer status);
    Boolean autoAssignRatingByOrderValue(Integer userId, Integer status);
    List<Map<String, Object>> findOrderByUserId(Integer userId);
    List<Map<String, Object>> findCustomerRatingAssignRule();
    //List<Map<String, Object>> findUsernameByUserId(Integer userId);
}
