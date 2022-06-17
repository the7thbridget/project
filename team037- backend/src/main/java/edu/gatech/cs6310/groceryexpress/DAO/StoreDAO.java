package edu.gatech.cs6310.groceryexpress.DAO;

import java.util.List;
import java.util.Map;

public interface StoreDAO {
    List<Map<String, Object>> findAllStore(Integer status);
    Boolean addStore(String storeName, Integer revenue) throws Throwable;
    List<Map<String, Object>> findStoreIdByName(String storeName);
    Boolean updateRevenue(Integer storeId, Integer orderRevenue);
}
