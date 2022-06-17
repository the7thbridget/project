package edu.gatech.cs6310.groceryexpress.DAOImpl;

import edu.gatech.cs6310.groceryexpress.DAO.StoreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class StoreDAOImpl implements StoreDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> findAllStore(Integer status) {
        String sql = "SELECT s.store_id, store_name, revenue, COALESCE(incoming_revenue, 0) AS incoming_revenue\n" +
                "FROM Store s\n" +
                "         LEFT JOIN (SELECT d.store_id, SUM(quantity * unit_price) AS incoming_revenue\n" +
                "                    FROM Order_2_item o2i\n" +
                "                             JOIN `Order` o ON o2i.order_id = o.order_id\n" +
                "                             JOIN Drone d ON o.drone_id = d.drone_id\n" +
                "                    WHERE o.status = ?\n" +
                "                    GROUP BY 1\n" +
                ") calc_revenue\n" +
                "ON calc_revenue.store_id = s.store_id\n" +
                "ORDER BY store_name ASC;";
        Object[] args = {status};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public Boolean addStore(String storeName, Integer revenue) throws Throwable {
        String sql = "INSERT INTO Store (store_name, revenue) VALUES (?, ?)";
        Object[] args = {storeName, revenue};
        try {
            return jdbcTemplate.update(sql, args) > 0;
        } catch (DataAccessException e) {
            throw e.getCause();
        }

    }

    @Override
    public List<Map<String, Object>> findStoreIdByName(String storeName) {
        String sql = "SELECT store_id FROM Store WHERE store_name = ?";
        Object[] args = {storeName};
        List<Map<String, Object>> stores = jdbcTemplate.queryForList(sql, args);
        return stores;
    }

    @Override
    public Boolean updateRevenue(Integer storeId, Integer orderRevenue) {
        String sql = "UPDATE Store SET revenue = revenue + ? WHERE store_id = ?";
        Object[] args = {orderRevenue, storeId};
        return jdbcTemplate.update(sql, args) > 0;
    }
}
