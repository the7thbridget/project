package edu.gatech.cs6310.groceryexpress.DAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PilotDAO {
    Boolean addPilot(String account, String firstName, String lastName, String phoneNumber, String taxID, String licenseID, Integer experience) throws SQLException;
    List<Map<String, Object>> findAllPilot();
    Boolean updateExperience(Integer pilotId);
    List<Map<String, Object>> findPilotIdByAccount(String pilotAccount);
    List<Map<String, Object>> findPilotById(Integer id);
    List<Map<String, Object>> findPilotByLicense(String licenseId);
    List<Map<String, Object>> findPilotByTaxID(String taxId);
    List<Map<String, Object>> findPilotAvailibility(Integer pilotId);
}
