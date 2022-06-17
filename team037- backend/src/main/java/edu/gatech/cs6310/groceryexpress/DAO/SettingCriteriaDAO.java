package edu.gatech.cs6310.groceryexpress.DAO;

import java.util.List;
import java.util.Map;

public interface SettingCriteriaDAO {
    //Boolean addSettingCriteria();
    Boolean updateSettingCriteria(Integer droneAssignRule, Integer customerRatingRule, Integer archiveDataType, Integer timePeriod, Integer archivePeriod);
    List<Map<String, Object>> findDroneAssignRule();
    List<Map<String, Object>> findCustomerRatingRule();
    List<Map<String, Object>> findArchiveDataType();
    List<Map<String, Object>> findTimePeriod();
    List<Map<String, Object>> findArchivePeriod();
    //Boolean updateCustomerCreditThresholdOrderNumber(Integer rating, Integer threshold);
    //Boolean updateCustomerCreditThresholdOrderValue(Integer rating, Integer threshold);
}
