package edu.gatech.cs6310.groceryexpress.DAOImpl;

import edu.gatech.cs6310.groceryexpress.DAO.DroneDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DroneDAOImpl implements DroneDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Boolean addDrone(String droneIdentifier, Integer storeId, Integer maxCapacity, Integer maxTrip) {
        String sql = "INSERT INTO Drone (drone_identifier, store_id, max_capacity, max_trip, remaining_trip) VALUES (?, ?, ?, ?, ?)";
        Object[] args = {droneIdentifier, storeId, maxCapacity, maxTrip, maxTrip};
        // try {
        return jdbcTemplate.update(sql, args) > 0;
        // } catch (DataAccessException e) {
        //     SQLException exception = (SQLException) e.getCause();
        //     throw exception;
        // }

    }

    @Override
    public List<Map<String, Object>> findDroneByStoreId(Integer storeId) {
        String sql = "SELECT * FROM Drone WHERE store_id = ? ORDER BY drone_identifier ASC";
        Object[] args = {storeId};
        List<Map<String, Object>> drones = jdbcTemplate.queryForList(sql, args);
        return drones;
    }

    @Override
    public List<Map<String, Object>> findDroneByStoreIdAndDroneIdentifier(Integer storeId, String droneIdentifier) {
        String sql = "SELECT * FROM Drone WHERE store_id = ? AND drone_identifier = ?";
        Object[] args = {storeId, droneIdentifier};
        return jdbcTemplate.queryForList(sql, args);
    }


    @Override
    public Boolean flyDrone(Integer storeId, String droneIdentifier, Integer pilotId) {
        String sql = "UPDATE Drone SET pilot_id = ? WHERE store_id = ? AND drone_identifier = ?";
        Object[] args = {pilotId, storeId, droneIdentifier};

        return jdbcTemplate.update(sql, args) > 0;

    }

    @Override
    public List<Map<String, Object>> findNumOfOrders(Integer droneId, Integer status) {
        String sql = "SELECT COUNT(*) AS num_order FROM `Order` WHERE drone_id = ? AND status = ?";
        Object[] args = {droneId, status};
        // try {
        return jdbcTemplate.queryForList(sql, args);
        // } catch (DataAccessException e) {
        //     throw e.getCause();
        // }

    }

    @Override
    public List<Map<String, Object>> findRemainingCapacity(Integer droneId, Integer status) {
        String sql = "SELECT (max_capacity - IFNULL((SELECT SUM(quantity * unit_weight) As total_cap\n" +
                "                        FROM `Order` o\n" +
                "                                 JOIN Order_2_item o2i ON o.order_id = o2i.order_id\n" +
                "                                 JOIN Item i ON o2i.item_id = i.item_id\n" +
                "                        WHERE drone_id = ?\n" +
                "                          AND `status` = ?), 0)) AS remaining_cap\n" +
                "FROM Drone\n" +
                "WHERE drone_id = ?";
        Object[] args = {droneId, status, droneId};
        // try {
        return jdbcTemplate.queryForList(sql, args);
        // } catch (DataAccessException e) {
        //     throw e.getCause();
        // }

    }

    @Override
    public Boolean updateRemainingTrip(Integer droneId) {
        String sql = "UPDATE Drone SET remaining_trip = remaining_trip - 1 WHERE drone_id = ? AND remaining_trip >= 1";
        Object[] args = {droneId};
        // try {
        return jdbcTemplate.update(sql, args) > 0;
        // } catch (DataAccessException e) {
        //     throw e.getCause();
        // }

    }

    @Override
    public Boolean updateRemainingCapacity(Integer droneId, Integer orderId, Integer status) {
        String sql = "SELECT (max_capacity - (SELECT SUM(quantity * unit_weight) FROM Order o JOIN Order_2_item o2i ON .order_id = o2i.order_id JOIN Item i ON o2i.item_id = i.item_id WHERE drone_id = ? AND `status` = ?)) FROM Drone WHERE drone_id = ?";
        Object[] args = {droneId, status, droneId};
        // try {
        return jdbcTemplate.update(sql, args) > 0;
        // } catch (DataAccessException e) {
        //     throw e.getCause();
        // }

    }

    @Override
    public Boolean maintainDrone(Integer droneId) {
        String sql = "UPDATE Drone SET remaining_trip = max_trip WHERE drone_id = ?";
        Object[] args = {droneId};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public List<Map<String, Object>> findPilotId(Integer droneId) {
        String sql = "SELECT pilot_id FROM Drone WHERE drone_id = ? ";
        Object[] args = {droneId};
        // try {
        return jdbcTemplate.queryForList(sql, args);
        // } catch (DataAccessException e) {
        //     throw e.getCause();
        // }
    }

    @Override
    public List<Map<String, Object>> findDroneByDroneIdAndDroneIdentifier(Integer oldDroneId, String newDroneIdentifier) {
        String sql = "SELECT drone_id, drone_identifier, store_id, max_capacity, pilot_id, remaining_trip FROM Drone WHERE store_id = (SELECT store_id from Drone where drone_id = ?) AND drone_identifier = ?";
        Object[] args = {oldDroneId, newDroneIdentifier};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> recommendDroneByCapacity(Integer oldDroneId, Integer orderWeight, Integer status) {
        String sql = "WITH all_store_drone_id AS (SELECT drone_id, max_capacity, pilot_id\n" +
                "                            FROM Drone\n" +
                "                            WHERE store_id = (SELECT store_id FROM Drone WHERE drone_id = ?)\n" +
                "),\n" +
                "     order_cap AS (SELECT asdi.drone_id, COALESCE(SUM(quantity * unit_weight), 0) As current_load\n" +
                "                   FROM (SELECT order_id, drone_id FROM `Order` WHERE `status` = ?) o\n" +
                "                            RIGHT JOIN all_store_drone_id asdi ON o.drone_id = asdi.drone_id\n" +
                "                            LEFT JOIN Order_2_item o2i ON o.order_id = o2i.order_id\n" +
                "                            LEFT JOIN Item i ON o2i.item_id = i.item_id\n" +
                "                   GROUP BY 1),\n" +
                "     calculate_remaining_cap AS (\n" +
                "         SELECT asdi.drone_id, pilot_id, (max_capacity - current_load) AS remaining_cap\n" +
                "         FROM order_cap o\n" +
                "                  RIGHT JOIN all_store_drone_id asdi ON o.drone_id = asdi.drone_id\n" +
                "         ORDER BY 3 DESC)\n" +
                "SELECT drone_id, remaining_cap, pilot_id\n" +
                "FROM calculate_remaining_cap\n" +
                "WHERE remaining_cap >= ?\n" +
                "  AND pilot_id IS NOT NULL\n" +
                "  AND drone_id != ?\n" +
                "LIMIT 1";

        Object[] args = {oldDroneId, status, orderWeight, oldDroneId};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> recommendDroneByTrip(Integer oldDroneId, Integer orderWeight, Integer status) {
        String sql = "WITH all_store_drone_id AS (SELECT drone_id, max_capacity, pilot_id, remaining_trip\n" +
                "                            FROM Drone\n" +
                "                            WHERE store_id = (SELECT store_id FROM Drone WHERE drone_id = ?)),\n" +
                "     order_cap AS (SELECT asdi.drone_id, COALESCE(SUM(quantity * unit_weight), 0) As current_load\n" +
                "                   FROM (SELECT order_id, drone_id FROM `Order` WHERE `status` = ?) o\n" +
                "                            RIGHT JOIN all_store_drone_id asdi ON o.drone_id = asdi.drone_id\n" +
                "                            LEFT JOIN Order_2_item o2i ON o.order_id = o2i.order_id\n" +
                "                            LEFT JOIN Item i ON o2i.item_id = i.item_id\n" +
                "                   GROUP BY 1),\n" +
                "     calculate_remaining_cap AS (\n" +
                "         SELECT asdi.drone_id, pilot_id, (max_capacity - current_load) AS remaining_cap, remaining_trip\n" +
                "         FROM order_cap o\n" +
                "                  RIGHT JOIN all_store_drone_id asdi ON o.drone_id = asdi.drone_id\n" +
                "         ORDER BY 3 DESC)\n" +
                "SELECT drone_id, remaining_trip, remaining_cap, pilot_id\n" +
                "FROM calculate_remaining_cap\n" +
                "WHERE remaining_cap >= ?\n" +
                "  AND pilot_id IS NOT NULL\n" +
                "  AND remaining_trip >= 1\n" +
                "  AND drone_id != ?\n" +
                "ORDER BY 2 DESC\n" +
                "LIMIT 1;";

        Object[] args = {oldDroneId, status, orderWeight, oldDroneId};
        return jdbcTemplate.queryForList(sql, args);
    }



}
