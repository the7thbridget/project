package edu.gatech.cs6310.groceryexpress.DAOImpl;

import edu.gatech.cs6310.groceryexpress.DAO.PilotDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;

@Repository
public class PilotDAOImpl implements PilotDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Boolean addPilot(String account, String firstName, String lastName, String phoneNumber, String taxID, String licenseID, Integer experience) throws SQLException {
        String sql = "INSERT INTO Pilot (account, first_name, last_name, phone_number, tax_id, license_id, experience) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] args = {account, firstName, lastName, phoneNumber, taxID, licenseID, experience};
        try {
            return jdbcTemplate.update(sql, args) > 0;
        } catch (DataAccessException e) {
            SQLException exception = (SQLException) e.getCause();
            throw exception;
        }
    }

    @Override
    public List<Map<String, Object>> findAllPilot() {
        String sql = "SELECT * FROM Pilot ORDER BY account ASC";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> findPilotAvailibility(Integer pilotId) {
        String sql = "SELECT pilot_id FROM Drone WHERE pilot_id = ?";
        Object[] args = {pilotId};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public Boolean updateExperience(Integer pilotId) {
        String sql = "UPDATE Pilot SET experience = experience + 1 WHERE pilot_id = ?";
        Object[] args = {pilotId};
        return jdbcTemplate.update(sql, args) > 0;
    }

    @Override
    public List<Map<String, Object>> findPilotIdByAccount(String pilotAccount) {
        String sql = "SELECT pilot_id FROM Pilot WHERE account = ?";
        Object[] args = {pilotAccount};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> findPilotById(Integer id) {
        String sql = "SELECT * FROM Pilot WHERE pilot_id = ?";
        Object[] args = {id};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> findPilotByLicense(String licenseId) {
        String sql = "SELECT * FROM Pilot WHERE license_id = ?";
        Object[] args = {licenseId};
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> findPilotByTaxID(String taxId) {
        String sql = "SELECT * FROM Pilot WHERE tax_id = ?";
        Object[] args = {taxId};
        return jdbcTemplate.queryForList(sql, args);
    }
}
