package edu.gatech.cs6310.groceryexpress.DAOImpl;

import edu.gatech.cs6310.groceryexpress.DAO.ItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ItemDAOImpl implements ItemDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Boolean addItem(Integer storeId, String itemName, Integer weight) throws Throwable {
        String sql = "INSERT INTO Item (item_name, unit_weight, store_id) VALUES (?, ?, ?)";
        Object[] args = {itemName, weight, storeId};
        try {
            return jdbcTemplate.update(sql, args) > 0;
        } catch (DataAccessException e) {
            throw e.getCause();
        }

    }

    @Override
    public List<Map<String, Object>> findItemByStoreId(Integer storeId) {
        String sql = "SELECT item_name, unit_weight FROM Item WHERE store_id = ? ORDER BY item_name ASC";
        Object[] args = {storeId};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> findItemByStoreIdAndItemName(Integer storeId, String itemName) {
        String sql = "SELECT * FROM Item Join Store S on Item.store_id = S.store_id WHERE S.store_id = ? AND item_name = ?";
        Object[] args = {storeId, itemName};
        return jdbcTemplate.queryForList(sql, args);
    }
}
