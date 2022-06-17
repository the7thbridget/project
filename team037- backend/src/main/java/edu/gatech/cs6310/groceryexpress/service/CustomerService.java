package edu.gatech.cs6310.groceryexpress.service;

import edu.gatech.cs6310.groceryexpress.POJO.Customer;
import edu.gatech.cs6310.groceryexpress.common.R;
import java.sql.SQLException;

public interface CustomerService {
    R addCustomer(Customer customer);
    R displayCustomerData();
    //R showCustomerData(Integer archiveData);
    R manualReassignCustomerRating(Integer userId, Integer manualRating) throws SQLException;
    R autoReassignRating(Integer userId);
}
