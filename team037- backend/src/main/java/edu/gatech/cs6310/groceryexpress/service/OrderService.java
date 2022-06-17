package edu.gatech.cs6310.groceryexpress.service;

import edu.gatech.cs6310.groceryexpress.POJO.Order;
import edu.gatech.cs6310.groceryexpress.common.R;

public interface OrderService {
    R addOrder(Order order);
    R displayOrderByStoreName(String storeName);
    R requestItem(String storeName, String orderName, String itemName, Integer quantity, Integer unitPrice);
    R purchaseOrder(String storeName, String orderName);
    R cancelOrder(String storeName, String orderName);
    R showCustomerOrder(String username, String storeName);
    R manualReassignDroneToOrder(Integer orderId, Integer droneId, String droneIdentifier);
    R autoReassignDroneToOrder(Integer orderId, Integer droneId);
}
