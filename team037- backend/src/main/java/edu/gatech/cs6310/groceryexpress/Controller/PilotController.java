package edu.gatech.cs6310.groceryexpress.Controller;

import edu.gatech.cs6310.groceryexpress.POJO.Pilot;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.service.PilotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PilotController {
    @Autowired
    PilotService pilotService;

    @PostMapping("/api/pilot/add")
    public R addPilot(@RequestBody Pilot pilot) {
        return pilotService.addPilot(pilot);
    }

    @GetMapping("/api/pilot/all")
    public R displayAllPilots() {
        return pilotService.displayAllPilots();
    }

}
