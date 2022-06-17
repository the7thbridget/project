package edu.gatech.cs6310.groceryexpress.service;

import edu.gatech.cs6310.groceryexpress.POJO.Drone;
import edu.gatech.cs6310.groceryexpress.common.R;

public interface DroneService {
    R addDrone(Drone drone);
    R displayDroneByStoreName(String storeName);
    R flyDrone(String storeName, String droneIdentifier, String pilotAccount);
    R maintainDrone(Integer drone_id);
}
