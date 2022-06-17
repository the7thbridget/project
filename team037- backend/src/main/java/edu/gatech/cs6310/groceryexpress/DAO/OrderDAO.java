package edu.gatech.cs6310.groceryexpress.DAO;

import java.util.List;
import java.util.Map;

public interface OrderDAO {
    Boolean addOrder(Integer droneId, Integer customerId, String orderName, Integer status);
    List<Map<String, Object>> findOrderByDroneId(Integer droneId);
    List<Map<String, Object>> findAllOrderByDroneId(Integer droneId);
    Boolean requestItem(Integer orderId, Integer itemId, Integer quantity, Integer unitPrice);
    //List<Map<String, Object>> findOrderIdByStoreNameAndOrderName(String storeName, String orderName);
    List<Map<String, Object>> findOrderByStoreNameAndOrderName(String storeName, String orderName);
    Boolean changeOrderStatus(Integer orderId);
    List<Map<String, Object>> findOrderCredit(Integer orderId);
    List<Map<String, Object>> findOrderWeight(Integer orderId);
    Boolean cancelOrderFromOrder(Integer orderId);
    Boolean assignDroneToOrder(Integer droneId, Integer orderId);
    Boolean deleteActiveOrder(Integer timePeriod);
    Boolean deleteOrder2Item(Integer timePeriod);
    Boolean retrieveOrder2Item();
    Boolean retrieveOrder();
    Boolean cancelOrderFromOrder2Item(Integer orderId);
    List<Map<String, Object>> findItemsByOrderId(Integer orderId);
    List<Map<String, Object>> findItemOnOrderByItemIdAndOrderId(Integer itemId, Integer orderId);
    Boolean updateItemQuantity(Integer orderId, Integer itemId, Integer newQuantity);
    List<Map<String, Object>> showCustomerOrder(Integer customerId, Integer storeId);
}
