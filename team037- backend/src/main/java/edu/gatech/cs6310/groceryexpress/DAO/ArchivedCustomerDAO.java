package edu.gatech.cs6310.groceryexpress.DAO;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ArchivedCustomerDAO {
    Boolean addArchivedCustomer(Integer timePeriod);
    List<Map<String, Object>> findAllArchivedCustomer();
    Boolean deleteArchivedCustomer();
    Boolean purgeArchivedCustomer(Integer archivePeriod);
}
