package edu.gatech.cs6310.groceryexpress.DAOImpl;

import edu.gatech.cs6310.groceryexpress.DAO.ArchivedOrderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ArchivedOrderDAOImpl implements ArchivedOrderDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Boolean addArchivedOrder(Integer timePeriod) {
        String sql = "INSERT INTO Archived_order (archive_order_id, drone_id, customer_id, close_date, create_date, order_name, status, archived_date)\n" +
                "SELECT order_id, drone_id, customer_id, close_date, create_date, order_name, status, CURRENT_DATE() AS archived_date FROM `Order` WHERE DATEDIFF(CURRENT_DATE(), close_date) > ?";
        Object[] args = {timePeriod};
        return jdbcTemplate.update(sql,args) > 0;
    }

    @Override
    public Boolean addArchivedOrder2Item(Integer timePeriod) {
        String sql = "INSERT INTO Archived_order_2_item (archived_order_id, archived_item_id, quantity, unit_price)\n" +
                "SELECT Order_2_item.order_id, item_id, quantity, unit_price FROM Order_2_item \n" +
                "WHERE EXISTS(SELECT order_id FROM `Order` WHERE DATEDIFF(CURRENT_DATE(), close_date) > ?)";
        Object[] args = {timePeriod};
        return jdbcTemplate.update(sql, args) > 0;
    }
/*
    @Override
    public List<Map<String, Object>> findAllArchivedOrder() {
        String sql = "SELECT * FROM Archived_order";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> findAllArchivedOrder2Item() {
        String sql = "SELECT * FROM Archived_order_2_item";
        return jdbcTemplate.queryForList(sql);
    }
*/
    @Override
    public Boolean deleteArchivedOrder() {
        String sql = "DELETE FROM Archived_order";
        return jdbcTemplate.update(sql) > 0;
    }

    @Override
    public Boolean deleteArchivedOrder2Item() {
        String sql = "DELETE FROM Archived_order_2_item";
        return jdbcTemplate.update(sql) > 0;
    }

    @Override
    public Boolean purgeArchivedOrder(Integer archivePeriod) {
        String sql = "DELETE FROM Archived_order WHERE DATEDIFF(CURRENT_DATE(), archived_date) > ?";
        Object[] args = {archivePeriod};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public Boolean purgeArchivedOrder2Item(Integer archivePeriod) {
        String sql = "DELETE FROM Archived_order_2_item WHERE EXISTS(SELECT archive_order_id FROM Archived_order WHERE Archived_order.archive_order_id = Archived_order_2_item.archived_order_id AND DATEDIFF(CURRENT_DATE(), archived_date) > ?)";
        Object[] args = {archivePeriod};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public List<Map<String, Object>> findArchivedOrderByDroneId(Integer droneId) {
        String sql = "SELECT * FROM Archived_order WHERE drone_id = ?";
        Object[] args = {droneId};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> findArchivedOrderItemsByOrderId(Integer orderId) {
        String sql = "SELECT item_name, quantity, unit_price * quantity AS total_price, unit_weight * quantity AS total_weight\n" +
                "FROM Archived_order_2_item LEFT JOIN Item I on Archived_order_2_item.archived_item_id = I.item_id\n" +
                "WHERE archived_order_id = ?";
        Object[] args = {orderId};
        return jdbcTemplate.queryForList(sql, args);
    }
}
