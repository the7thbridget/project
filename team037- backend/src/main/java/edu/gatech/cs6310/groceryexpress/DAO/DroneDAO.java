package edu.gatech.cs6310.groceryexpress.DAO;

import java.util.List;
import java.util.Map;

public interface DroneDAO {
    Boolean addDrone(String droneIdentifier, Integer storeId, Integer maxCapacity, Integer maxTrip);
    List<Map<String, Object>> findDroneByStoreId(Integer storeId);
    Boolean flyDrone(Integer storeId, String droneIdentifier, Integer pilotId);
    List<Map<String, Object>> findNumOfOrders(Integer droneId, Integer status);
    List<Map<String, Object>> findRemainingCapacity(Integer droneId, Integer status);
    Boolean updateRemainingTrip(Integer droneId);
    Boolean maintainDrone(Integer droneId);
    Boolean updateRemainingCapacity(Integer droneId, Integer orderId, Integer status);
    List<Map<String, Object>> findPilotId(Integer droneId);

    List<Map<String, Object>> findDroneByStoreIdAndDroneIdentifier(Integer storeId, String droneIdentifier);

    List<Map<String, Object>> findDroneByDroneIdAndDroneIdentifier(Integer oldDroneId, String newDroneIdentifier);
    List<Map<String, Object>> recommendDroneByCapacity(Integer droneId, Integer orderWeight, Integer status);
    List<Map<String, Object>> recommendDroneByTrip(Integer droneId, Integer orderWeight, Integer status);


}
