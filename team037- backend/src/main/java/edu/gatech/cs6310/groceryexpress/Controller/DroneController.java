package edu.gatech.cs6310.groceryexpress.Controller;

import edu.gatech.cs6310.groceryexpress.POJO.Drone;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DroneController {
    @Autowired
    DroneService droneService;

    @PostMapping("/api/drone/add")
    public R addDrone(@RequestBody Drone drone) throws Throwable {
        return droneService.addDrone(drone);
    }

    @GetMapping("/api/drone/all")
    public R displayDroneByStoreName(@RequestParam String storeName) {
        return droneService.displayDroneByStoreName(storeName);
    }

    @PostMapping("/api/drone/fly")
    public R flyDrone(@RequestParam String storeName, @RequestParam String droneIdentifier, @RequestParam String pilotAccount) {
        return droneService.flyDrone(storeName, droneIdentifier, pilotAccount);
    }

    @PostMapping("/api/drone/maintain")
    public R maintainDrone(@RequestParam Integer drone_id) {
        return droneService.maintainDrone(drone_id);
    }


}