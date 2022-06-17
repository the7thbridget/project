package edu.gatech.cs6310.groceryexpress.DAOImpl;

import edu.gatech.cs6310.groceryexpress.DAO.OrderDAO;
import edu.gatech.cs6310.groceryexpress.common.OrderStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OrderDAOImpl implements OrderDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Boolean addOrder(Integer droneId, Integer customerId, String orderName, Integer status) {
        String sql = "INSERT INTO `Order` (drone_id, customer_id, order_name, status, create_date) VALUES (?, ?, ?, ?, CURRENT_DATE())";
        Object[] args = {droneId, customerId, orderName, status};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public List<Map<String, Object>> findOrderByDroneId(Integer droneId) {
        String sql = "SELECT * FROM `Order` WHERE drone_id = ? AND status = ?";
        Object[] args = {droneId, OrderStatusCode.PENDING.getOrderStatusCode()};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> findAllOrderByDroneId(Integer droneId) {
        String sql = "SELECT * FROM `Order` WHERE drone_id = ? ORDER by order_name ASC";
        Object[] args = {droneId};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public Boolean requestItem(Integer orderId, Integer itemId, Integer quantity, Integer unitPrice) {
        String sql = "INSERT INTO Order_2_item (order_id, item_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        Object[] args = {orderId, itemId, quantity, unitPrice};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public List<Map<String, Object>> showCustomerOrder(Integer customerId, Integer storeId) {
        String sql = "SELECT * FROM `Order`\n" +
                "    JOIN Drone D on D.drone_id = `Order`.drone_id\n" +
                "    WHERE D.store_id = ? AND customer_id = ?";
        Object[] args = {storeId, customerId};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> findOrderByStoreNameAndOrderName(String storeName, String orderName) {
        String sql = "SELECT status, `Order`.order_id AS order_id, `Order`.drone_id AS drone_id, customer_id AS user_id\n" +
                "FROM `Order`\n" +
                "         LEFT JOIN Drone ON Drone.drone_id = `Order`.drone_id\n" +
                "         LEFT JOIN Store S on Drone.store_id = S.store_id\n" +
                "WHERE S.store_name = ? AND order_name = ?";
        Object[] args = {storeName, orderName};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public Boolean changeOrderStatus(Integer orderId) {
        String sql = "UPDATE `Order` SET status = ?, close_date = CURRENT_DATE() WHERE order_id = ?";
        Object[] args = {OrderStatusCode.COMPLETE.getOrderStatusCode(),orderId};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public List<Map<String, Object>> findOrderCredit(Integer orderId) {
        String sql = "SELECT SUM(quantity * unit_price) AS order_credit FROM Order_2_item WHERE order_id = ?";
        Object[] args = {orderId};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> findOrderWeight(Integer orderId) {
        String sql = "SELECT IFNULL(SUM(quantity * unit_weight), 0) AS order_weight FROM Order_2_item o2i JOIN Item i ON o2i.item_id = i.item_id WHERE order_id = ?";
        Object[] args = {orderId};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public Boolean cancelOrderFromOrder(Integer orderId) {
//        String sql = "DELETE FROM Order_2_item WHERE order_id = ? AND DELETE FROM `Order`  WHERE order_id = ?";
        String sql = "UPDATE `Order` SET status = ?, close_date = CURRENT_DATE() WHERE order_id = ?";
        Object[] args = {OrderStatusCode.CANCEL.getOrderStatusCode() ,orderId};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public Boolean assignDroneToOrder(Integer droneId, Integer orderId) {
        String sql = "UPDATE `Order` SET drone_id = ? WHERE order_id = ?";
        Object[] args = {droneId, orderId};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public Boolean deleteActiveOrder(Integer timePeriod) {
        String sql = "DELETE FROM `Order` WHERE DATEDIFF(CURRENT_DATE(), close_date) > ?";
        Object[] args = {timePeriod};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public Boolean deleteOrder2Item(Integer timePeriod) {
        String sql = "DELETE FROM Order_2_item WHERE EXISTS(SELECT order_id FROM `Order` WHERE `Order`.order_id = Order_2_item.order_id AND DATEDIFF(CURRENT_DATE(), close_date) > ?)";
        Object[] args = {timePeriod};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public Boolean retrieveOrder2Item() {
        String sql = "INSERT INTO Order_2_item (order_id, item_id, quantity, unit_price)\n" +
                "SELECT archived_order_id, archived_item_id, quantity, unit_price FROM Archived_order_2_item";
        return jdbcTemplate.update(sql) > 0;
    }

    @Override
    public Boolean retrieveOrder() {
        String sql = "INSERT INTO `Order` (drone_id, customer_id, order_name, close_date, create_date, status)\n +" +
                "SELECT drone_id, customer_id, order_name, close_date, create_date, status FROM Archived_order";
        return jdbcTemplate.update(sql) > 0;
    }

    @Override
    public Boolean cancelOrderFromOrder2Item(Integer orderId) {
        return null;
    }

    @Override
    public List<Map<String, Object>> findItemsByOrderId(Integer orderId) {
        String sql = "SELECT item_name, quantity, unit_price*quantity AS total_price, unit_weight*Order_2_item.quantity AS total_weight\n" +
                "FROM Order_2_item LEFT JOIN Item I on Order_2_item.item_id = I.item_id\n" +
                "WHERE order_id = ?";
        Object[] args = {orderId};
        return jdbcTemplate.queryForList(sql, args);
    }


    @Override
    public List<Map<String, Object>> findItemOnOrderByItemIdAndOrderId(Integer itemId, Integer orderId) {
        String sql = "SELECT * FROM Order_2_item WHERE item_id = ? AND order_id = ?";
        Object[] args = {itemId, orderId};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public Boolean updateItemQuantity(Integer orderId, Integer itemId, Integer newQuantity) {
        String sql = "UPDATE Order_2_item SET quantity = quantity + ? WHERE order_id = ? AND item_id = ?";
        Object[] args = {newQuantity, orderId, itemId};
        return jdbcTemplate.update(sql, args) > 0;
    }


}
