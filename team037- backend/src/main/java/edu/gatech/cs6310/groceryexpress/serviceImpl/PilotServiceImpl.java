package edu.gatech.cs6310.groceryexpress.serviceImpl;

import edu.gatech.cs6310.groceryexpress.DAO.PilotDAO;
import edu.gatech.cs6310.groceryexpress.POJO.Pilot;
import edu.gatech.cs6310.groceryexpress.common.ErrorCode;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.service.PilotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class PilotServiceImpl implements PilotService {
    @Autowired
    PilotDAO pilotDAO;

    @Override
    public R addPilot(Pilot pilot) {
        String account = pilot.getAccount();
        String firstName = pilot.getFirstName();
        String lastName = pilot.getLastName();
        String phoneNumber = pilot.getPhoneNumber();
        String taxID = pilot.getTaxID();
        String licenseID = pilot.getLicenseID();
        Integer experience = pilot.getExperience();
        List<Map<String, Object>> pilotIdByAccount = pilotDAO.findPilotIdByAccount(account);
        if (!pilotIdByAccount.isEmpty()) return R.error(ErrorCode.PILOT_ACCOUNT_DUPLICATE.getErrorCode(), ErrorCode.PILOT_ACCOUNT_DUPLICATE.getErrorMsg());
        List<Map<String, Object>> pilotByLicense = pilotDAO.findPilotByLicense(licenseID);
        if (!pilotByLicense.isEmpty()) return R.error(ErrorCode.PILOT_LICENSE_DUPLICATE.getErrorCode(), ErrorCode.PILOT_LICENSE_DUPLICATE.getErrorMsg());
        List<Map<String, Object>> pilotByTax = pilotDAO.findPilotByTaxID(taxID);
        if (!pilotByTax.isEmpty()) return R.error(ErrorCode.PILOT_TAX_DUPLICATE.getErrorCode(), ErrorCode.PILOT_TAX_DUPLICATE.getErrorMsg());
        try {
            boolean res = pilotDAO.addPilot(account, firstName, lastName, phoneNumber, taxID, licenseID, experience);
            return res ? R.ok("Successfully added a pilot!") : R.error("Adding pilot failed!");
        } catch (SQLException e) {
            return R.error(e.getMessage());
        }
    }

    @Override
    public R displayAllPilots() {
        try {
            List<Map<String, Object>> pilots = pilotDAO.findAllPilot();
            return R.ok().put("pilots", pilots);
        } catch (Exception e) {
            return R.error("Finding pilots failed!");
        }
    }
}
