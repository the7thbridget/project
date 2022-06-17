package edu.gatech.cs6310.groceryexpress.DAO;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ArchivedOrderDAO {
    Boolean addArchivedOrder(Integer timePeriod);
    Boolean addArchivedOrder2Item(Integer timePeriod);
    //List<Map<String, Object>> findAllArchivedOrder();
    //List<Map<String, Object>> findAllArchivedOrder2Item();
    Boolean deleteArchivedOrder();
    Boolean deleteArchivedOrder2Item();
    Boolean purgeArchivedOrder(Integer archivePeriod);
    Boolean purgeArchivedOrder2Item(Integer archivePeriod);
    List<Map<String, Object>> findArchivedOrderByDroneId(Integer droneId);
    List<Map<String, Object>> findArchivedOrderItemsByOrderId(Integer orderId);
}
