package edu.gatech.cs6310.groceryexpress.serviceImpl;

import edu.gatech.cs6310.groceryexpress.DAO.*;
import edu.gatech.cs6310.groceryexpress.common.ErrorCode;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.service.ArchiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class ArchiveServiceImpl implements ArchiveService {
    private static final String ARCHIVE_DATA_TYPE = "archive_data_type";
    private static final String TIME_PERIOD = "time_period";
    private static final String ARCHIVE_PERIOD = "archive_period";
    private static final Integer DAYS_IN_MONTH = 30;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SettingCriteriaDAO settingCriteriaDAO;

    @Autowired
    ArchivedCustomerDAO archivedCustomerDAO;

    @Autowired
    CustomerDAO customerDAO;

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    ArchivedOrderDAO archivedOrderDAO;

    @Override
    @Transactional
    @Scheduled(cron = "0 0 1 ? * *")
    public R archiveDataBasedOnCriteria() {
        logger.info("This is a scheduled job...");
        try {
            boolean res = new Boolean(null);
            boolean res1 = new Boolean(null);
            boolean res2 = new Boolean(null);
            boolean res3 = new Boolean(null);
            Integer archiveDataType = getArchiveDataType();
            Integer timePeriod = getTimePeriod() * DAYS_IN_MONTH;
            Integer archivePeriod = getArchivePeriod() * DAYS_IN_MONTH;
            switch (archiveDataType) {
                case 1:
                    res = this.archiveCustomer(timePeriod);
                    res1 = this.purgeArchivedCustomer(archivePeriod);
                    return res && res1 ? R.ok() : R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
                case 2:
                    res = this.archiveOrder(timePeriod);
                    res1 = this.purgeArchivedOrder(archivePeriod);
                    return res && res1 ? R.ok() : R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
                case 3:
                    res = this.archiveCustomer(timePeriod);
                    res1 = this.purgeArchivedCustomer(archivePeriod);
                    res2 = this.archiveOrder(timePeriod);
                    res3 = this.purgeArchivedOrder(archivePeriod);
                    return res && res1 && res2 && res3 ? R.ok() : R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
            }
            return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
        } catch (DataAccessException e) {
            return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), e.getMessage());
        }
    }

    @Override
    @Transactional
    public Boolean archiveCustomer(Integer timePeriod) {
        boolean res = archivedCustomerDAO.addArchivedCustomer(timePeriod);
        boolean res2 = customerDAO.deleteActiveCustomer(timePeriod);
        return res && res2;
    }

    /*
    @Override
    public R displayArchivedCustomer() {
        try {
            List<Map<String, Object>> res = archivedCustomerDAO.findAllArchivedCustomer();
            return R.ok().put("archivedCustomers", res);
        } catch (DataAccessException e) {
            return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), "Unable to fetch archived customers!");
        }
    }

     */

    @Override
    @Transactional
    public R retrieveArchivedCustomer() {
         try {
             boolean res = customerDAO.retrieveCustomer();
             boolean res2 = archivedCustomerDAO.deleteArchivedCustomer();
             return res && res2 ? R.ok() : R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
         } catch (DataAccessException e) {
             return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), e.getMessage());
         }
    }


    @Override
    @Transactional
    public Boolean archiveOrder(Integer timePeriod) {
        try {
            archivedOrderDAO.addArchivedOrder(timePeriod);
            archivedOrderDAO.addArchivedOrder2Item(timePeriod);
            orderDAO.deleteOrder2Item(timePeriod);
            orderDAO.deleteActiveOrder(timePeriod);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    /*
    @Override
    public R displayArchivedOrder() {
        try {
            List<Map<String, Object>> res = archivedOrderDAO.findAllArchivedOrder();
            return R.ok().put("archivedOrders", res);
        } catch (DataAccessException e) {
            return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), "Unable to fetch archived orders!");
        }
    }
     */

    @Override
    public R retrieveArchivedOrder() {
        try {
            boolean res = orderDAO.retrieveOrder();
            boolean res2 = orderDAO.retrieveOrder2Item();
            boolean res3 = archivedOrderDAO.deleteArchivedOrder2Item();
            boolean res4 = archivedOrderDAO.deleteArchivedOrder();
            return res && res2 && res3 && res4 ? R.ok() : R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
        } catch (DataAccessException e) {
            return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), e.getMessage());
        }
    }

    @Override
    public Boolean purgeArchivedCustomer(Integer archivePeriod) {
        boolean res = archivedCustomerDAO.purgeArchivedCustomer(archivePeriod);
        return res;
    }

    @Override
    public Boolean purgeArchivedOrder(Integer archivePeriod) {
        try {
            archivedOrderDAO.purgeArchivedOrder2Item(archivePeriod);
            archivedOrderDAO.purgeArchivedOrder(archivePeriod);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    private Integer getArchiveDataType() {
        List<Map<String, Object>> res = settingCriteriaDAO.findArchiveDataType();
        return (Integer) res.get(0).get(ARCHIVE_DATA_TYPE);
    }

    private Integer getTimePeriod() {
        List<Map<String, Object>> res = settingCriteriaDAO.findTimePeriod();
        return (Integer) res.get(0).get(TIME_PERIOD);
    }

    private Integer getArchivePeriod() {
        List<Map<String, Object>> res = settingCriteriaDAO.findArchivePeriod();
        return (Integer) res.get(0).get(ARCHIVE_PERIOD);
    }

}
