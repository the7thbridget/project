package edu.gatech.cs6310.groceryexpress.serviceImpl;

import edu.gatech.cs6310.groceryexpress.DAO.SettingCriteriaDAO;
import edu.gatech.cs6310.groceryexpress.POJO.SettingCriteria;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.common.exception.DataConnectionException;
import edu.gatech.cs6310.groceryexpress.service.SettingCriteriaService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
public class SettingCriteriaServiceImpl implements SettingCriteriaService {
    @Autowired
    SettingCriteriaDAO settingCriteriaDAO;

    @Override
    public R updateSettingCriteria(SettingCriteria settingCriteria) {
        Integer droneAssignRule = settingCriteria.getDroneAssignRule();
        Integer customerRatingRule = settingCriteria.getCustomerRatingRule();
        Integer archiveDataType = settingCriteria.getArchiveDataType();
        Integer timePeriod = settingCriteria.getTimePeriod();
        Integer archivePeriod = settingCriteria.getArchivePeriod();
        try {
            //boolean res = settingCriteriaDAO.addSettingCriteria();
            boolean res = settingCriteriaDAO.updateSettingCriteria(droneAssignRule, customerRatingRule, archiveDataType, timePeriod, archivePeriod);
            return res ? R.ok("Successfully updated the setting criteria!") : R.error("Update setting criteria failed!");
        } catch (DataConnectionException e) {
            return R.error("Cannot connect to the database!");
        }
    }

}
