package edu.gatech.cs6310.groceryexpress.DAOImpl;

import edu.gatech.cs6310.groceryexpress.DAO.ArchivedCustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ArchivedCustomerDAOImpl implements ArchivedCustomerDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Boolean addArchivedCustomer(Integer timePeriod) {
        String sql = "INSERT INTO Archived_customer (archived_customer_id, user_id, rating, credit, last_login, first_name, last_name, phone_number, archived_date)\n" +
                "SELECT active_customer_id, user_id, rating, credit, last_login, first_name, last_name, phone_number, CURRENT_DATE() AS archived_date FROM Active_customer WHERE DATEDIFF(CURRENT_DATE(), last_login) > ?";
        Object[] args = {timePeriod};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public List<Map<String, Object>> findAllArchivedCustomer() {
        String sql = "SELECT * FROM Archived_customer INNER JOIN User on Archived_customer.user_id = User.user_id ORDER BY User.username ASC";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public Boolean deleteArchivedCustomer() {
        String sql = "DELETE FROM Archived_customer";
        return jdbcTemplate.update(sql) > 0;
    }

    @Override
    public Boolean purgeArchivedCustomer(Integer archivePeriod) {
        String sql = "DELETE FROM Archived_customer WHERE DATEDIFF(CURRENT_DATE(), archived_date) > ?";
        Object[] args = {archivePeriod};
        return jdbcTemplate.update(sql, args) > 0;
    }
}
