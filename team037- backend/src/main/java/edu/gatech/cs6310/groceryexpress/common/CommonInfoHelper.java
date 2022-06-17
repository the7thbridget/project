package edu.gatech.cs6310.groceryexpress.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.gatech.cs6310.groceryexpress.DAO.CacheDAO;
import edu.gatech.cs6310.groceryexpress.DAO.StoreDAO;
import edu.gatech.cs6310.groceryexpress.common.exception.StoreNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommonInfoHelper {

    @Autowired
    CacheDAO cacheDAO;

    @Autowired
    StoreDAO storeDAO;

    public Integer getStoreIdFromStoreName(String storeName) throws JsonProcessingException {
        Map<String, Integer> storeMap = cacheDAO.getStoreMap();
        if (storeMap == null) {
            List<Map<String, Object>> res = storeDAO.findAllStore(OrderStatusCode.PENDING.getOrderStatusCode());
            Map<String, Integer> newStoreMap = new HashMap<>();
            for (Map<String, Object> row : res) {
                String name = (String) row.get("store_name");
                Integer storeId = (Integer) row.get("store_id");
                newStoreMap.put(name, storeId);
            }
            cacheDAO.refreshStoreList(newStoreMap);
            if (!newStoreMap.containsKey(storeName)) throw new StoreNotFoundException("Store not found!");
            return newStoreMap.get(storeName);
        } else {
            if (!storeMap.containsKey(storeName)) throw new StoreNotFoundException("Store not found!");
            return storeMap.get(storeName);
        }
    }

}
