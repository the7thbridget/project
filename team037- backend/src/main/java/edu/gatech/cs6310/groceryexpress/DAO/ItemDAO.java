package edu.gatech.cs6310.groceryexpress.DAO;

import java.util.List;
import java.util.Map;

public interface ItemDAO {
    Boolean addItem(Integer storeId, String itemName, Integer weight) throws Throwable;
    List<Map<String, Object>> findItemByStoreId(Integer storeId);
    List<Map<String, Object>> findItemByStoreIdAndItemName(Integer storeId, String itemName);
}
