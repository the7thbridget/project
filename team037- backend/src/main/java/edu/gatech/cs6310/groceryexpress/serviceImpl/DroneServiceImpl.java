package edu.gatech.cs6310.groceryexpress.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.gatech.cs6310.groceryexpress.DAO.DroneDAO;
import edu.gatech.cs6310.groceryexpress.DAO.PilotDAO;
import edu.gatech.cs6310.groceryexpress.DAO.StoreDAO;
import edu.gatech.cs6310.groceryexpress.DAO.OrderDAO;
import edu.gatech.cs6310.groceryexpress.POJO.Drone;
import edu.gatech.cs6310.groceryexpress.common.CommonInfoHelper;
import edu.gatech.cs6310.groceryexpress.common.ErrorCode;
import edu.gatech.cs6310.groceryexpress.common.OrderStatusCode;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.common.exception.*;
import edu.gatech.cs6310.groceryexpress.common.exception.DataConnectionException;
import edu.gatech.cs6310.groceryexpress.common.exception.DroneNotFoundException;
import edu.gatech.cs6310.groceryexpress.common.exception.PilotNotFoundException;
import edu.gatech.cs6310.groceryexpress.common.exception.StoreNotFoundException;
import edu.gatech.cs6310.groceryexpress.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DroneServiceImpl implements DroneService {
    private static final String REMAINING_CAP = "remaining_cap";
    private static final String NUM_ORDER = "num_order";
    private static final String PILOT_ID = "pilot_id";
    private static final String PILOT_FIRST_NAME = "first_name";
    private static final String PILOT_LAST_NAME = "last_name";

    @Autowired
    DroneDAO droneDAO;
    @Autowired
    StoreDAO storeDAO;
    @Autowired
    PilotDAO pilotDAO;
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    CommonInfoHelper commonInfoHelper;

    @Override
    public R addDrone(Drone drone) {
        try {
            String droneIdentifier = drone.getDroneIdentifier();
            String storeName = drone.getStoreName();
            Integer maxCapacity = drone.getMaxCapacity();
            Integer maxTrip = drone.getMaxTrip();
            Integer storeId = commonInfoHelper.getStoreIdFromStoreName(storeName);
            List<Map<String, Object>> droneByStoreIdAndDroneIdentifier = droneDAO.findDroneByStoreIdAndDroneIdentifier(storeId, droneIdentifier);
            if (!droneByStoreIdAndDroneIdentifier.isEmpty()) return R.error(ErrorCode.DRONE_DUPLICATE.getErrorCode(), ErrorCode.DRONE_DUPLICATE.getErrorMsg());
            boolean ans = droneDAO.addDrone(droneIdentifier, storeId, maxCapacity, maxTrip);
            return ans ? R.ok() : R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
        } catch (StoreNotFoundException | JsonProcessingException e) {
            return R.error(ErrorCode.STORE_NOT_FOUND.getErrorCode(), ErrorCode.STORE_NOT_FOUND.getErrorMsg());
        }
    }

    @Override
    public R displayDroneByStoreName(String storeName) {
        try {
            Integer storeId = commonInfoHelper.getStoreIdFromStoreName(storeName);
            List<Map<String, Object>> res = droneDAO.findDroneByStoreId(storeId);
            for (Map<String, Object> drone : res) {
                Integer droneId = (Integer) drone.get("drone_id");
                Integer numOrders = getNumOrders(droneId);
                Integer remainingCap = findRemainingCapacity(droneId);
                drone.put("numOrders", numOrders);
                drone.put("remainingCap", remainingCap);
                Integer pilotId = (Integer)drone.get(PILOT_ID);
                String pilotName = null;
                if (pilotId != null) {
                    List<Map<String, Object>> pilot = pilotDAO.findPilotById(pilotId);
                    pilotName = (String)pilot.get(0).get(PILOT_FIRST_NAME) + "_" + (String) pilot.get(0).get(PILOT_LAST_NAME);
                }
                drone.put("flownBy", pilotName);
            }
            return R.ok().put("drones", res);
        } catch (StoreNotFoundException | JsonProcessingException e) {
            return R.error(ErrorCode.STORE_NOT_FOUND.getErrorCode(), ErrorCode.STORE_NOT_FOUND.getErrorMsg());
        }
    }

    @Override
    public R flyDrone(String storeName, String droneIdentifier, String pilotAccount) {
        try {
            Integer storeId = commonInfoHelper.getStoreIdFromStoreName(storeName);
            Integer pilotId = getPilotId(pilotAccount);
            List<Map<String, Object>> checkPilotAvailibility = pilotDAO.findPilotAvailibility(pilotId);
            if (!checkPilotAvailibility.isEmpty()) return R.error(ErrorCode.PILOT_OCCUPIED.getErrorCode(), ErrorCode.PILOT_OCCUPIED.getErrorMsg());
            boolean ans = droneDAO.flyDrone(storeId, droneIdentifier, pilotId);
            return ans ? R.ok() : R.error(ErrorCode.DRONE_NOT_FOUND.getErrorCode(), ErrorCode.DRONE_NOT_FOUND.getErrorMsg());
        } catch (PilotNotFoundException e) {
            return R.error(ErrorCode.PILOT_NOT_FOUND.getErrorCode(), ErrorCode.PILOT_NOT_FOUND.getErrorMsg());
        } catch (StoreNotFoundException e) {
            return R.error(ErrorCode.STORE_NOT_FOUND.getErrorCode(), ErrorCode.STORE_NOT_FOUND.getErrorMsg());
        } catch (JsonProcessingException e) {
            return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
        }
    }

    @Override
    public R maintainDrone(Integer drone_id) {
        try {
            List<Map<String, Object>> res = droneDAO.findNumOfOrders(drone_id, OrderStatusCode.PENDING.getOrderStatusCode());
            if (res.isEmpty()) throw new DroneNotFoundException("Drone not found!");
            boolean ans = ((Long) res.get(0).get(NUM_ORDER)).intValue()==0;
            ans = ans && droneDAO.maintainDrone(drone_id);
            return ans ? R.ok() : R.error(ErrorCode.DRONE_HAS_UNDELIVERED_ORDER.getErrorCode(), ErrorCode.DRONE_HAS_UNDELIVERED_ORDER.getErrorMsg());
        } catch (DroneNotFoundException e) {
            return R.error(ErrorCode.DRONE_NOT_FOUND.getErrorCode(), ErrorCode.DRONE_NOT_FOUND.getErrorMsg());
        }
    }

    private Integer getNumOrders(Integer droneId) {
        try {
            List<Map<String, Object>> res = droneDAO.findNumOfOrders(droneId, OrderStatusCode.PENDING.getOrderStatusCode());
            if (res.isEmpty()) throw new DroneNotFoundException("Drone not found!");
            return ((Long) res.get(0).get(NUM_ORDER)).intValue();
        } catch (DataAccessException e) {
            throw new DataConnectionException();
        }
    }

    private Integer findRemainingCapacity(Integer droneId) {
        try {
            List<Map<String, Object>> res = droneDAO.findRemainingCapacity(droneId, OrderStatusCode.PENDING.getOrderStatusCode());
            if (res.isEmpty()) throw new DroneNotFoundException("Drone not found!");
            return ((BigDecimal) res.get(0).get(REMAINING_CAP)).intValue();
        } catch (DataAccessException e) {
            throw new DataConnectionException();
        }
    }

    private Integer getStoreId(String storeName) {
        List<Map<String, Object>> res = storeDAO.findStoreIdByName(storeName);
        if (res.isEmpty()) throw new StoreNotFoundException("Store name not found!");
        return (Integer) res.get(0).get("store_id");
    }

    private Integer getPilotId(String pilotAccount) {
        List<Map<String, Object>> res = pilotDAO.findPilotIdByAccount(pilotAccount);
        if (res.isEmpty()) throw new PilotNotFoundException();
        return (Integer) res.get(0).get("pilot_id");
    }

    private Integer getPilotIdByDroneId(Integer droneId) {
        List<Map<String, Object>> pilotList = droneDAO.findPilotId(droneId);
        if (pilotList.isEmpty()) throw new PilotNotFoundException();
        Map<String, Object> pilot = pilotList.get(0);
        return ((Long) pilot.get(PILOT_ID)).intValue();
    }
}
