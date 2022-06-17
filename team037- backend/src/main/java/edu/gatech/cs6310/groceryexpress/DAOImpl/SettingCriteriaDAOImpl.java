package edu.gatech.cs6310.groceryexpress.DAOImpl;

import edu.gatech.cs6310.groceryexpress.DAO.SettingCriteriaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SettingCriteriaDAOImpl implements SettingCriteriaDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /*
    @Override
    public Boolean addSettingCriteria() {
        String sql = "INSERT INTO Setting_criteria (row_num) VALUE (1)";
        return jdbcTemplate.update(sql) > 0;
    }

     */

    @Override
    public Boolean updateSettingCriteria(Integer droneAssignRule, Integer customerRatingRule, Integer archiveDataType, Integer timePeriod, Integer archivePeriod) {
        String sql = "UPDATE Setting_criteria SET drone_assign_rule = ?, customer_rating_rule = ?, archive_data_type = ?, time_period = ?, archive_period = ? WHERE row_num = 1";
        Object[] args = {droneAssignRule, customerRatingRule, archiveDataType, timePeriod, archivePeriod};
        return jdbcTemplate.update(sql,args) > 0;
    }

    @Override
    public List<Map<String, Object>> findDroneAssignRule() {
        String sql = "SELECT drone_assign_rule FROM Setting_criteria LIMIT 1";
        Object[] args = {};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> findCustomerRatingRule() {
        String sql = "SELECT customer_rating_rule FROM Setting_criteria LIMIT 1";
        Object[] args = {};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> findArchiveDataType() {
        String sql = "SELECT archive_data_type FROM Setting_criteria";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> findTimePeriod() {
        String sql = "SELECT time_period FROM Setting_criteria";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> findArchivePeriod() {
        String sql = "SELECT archive_period FROM Setting_criteria";
        return jdbcTemplate.queryForList(sql);
    }

    /*
        @Override
    public Boolean updateCustomerCreditThresholdOrderNumber(Integer rating, Integer threshold) {
        String sql = "UPDATE Rating_orderNum SET upper_bound = ? WHERE rating = ? \n" +
                "UPDATE Rating_orderNum SET lower_bound = (? + 1) WHERE rating = (? + 1)";
        Object[] args = {threshold, rating, threshold, rating};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public Boolean updateCustomerCreditThresholdOrderValue(Integer rating, Integer threshold) {
        String sql = "UPDATE Rating_orderValue SET upper_bound = ? WHERE rating = ? \n" +
                "UPDATE Rating_orderValue SET lower_bound = (? + 1) WHERE rating = (? + 1)";
        Object[] args = {threshold, rating, threshold, rating};
        return jdbcTemplate.update(sql, args) > 0;
    }
     */
}
