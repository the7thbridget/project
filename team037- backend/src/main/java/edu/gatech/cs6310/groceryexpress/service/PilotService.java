package edu.gatech.cs6310.groceryexpress.service;

import edu.gatech.cs6310.groceryexpress.POJO.Pilot;
import edu.gatech.cs6310.groceryexpress.common.R;

public interface PilotService {
    R addPilot(Pilot pilot);
    R displayAllPilots();

}
