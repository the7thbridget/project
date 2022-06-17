package edu.gatech.cs6310.groceryexpress.serviceImpl;

import edu.gatech.cs6310.groceryexpress.DAO.CacheDAO;
import edu.gatech.cs6310.groceryexpress.DAO.StoreDAO;
import edu.gatech.cs6310.groceryexpress.POJO.Store;
import edu.gatech.cs6310.groceryexpress.common.ErrorCode;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.gatech.cs6310.groceryexpress.common.OrderStatusCode;

import java.util.List;
import java.util.Map;

@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    StoreDAO storeDAO;

    @Autowired
    CacheDAO cacheDAO;

    @Override
    public R displayAllStores() {
        try {
            List<Map<String, Object>> allStores = storeDAO.findAllStore(OrderStatusCode.PENDING.getOrderStatusCode());
            return R.ok().put("stores", allStores);
        } catch (Exception e) {
            return R.error("Unable to find all stores!");
        }
    }

    @Override
    public R addStore(Store store) throws Throwable {
        String storeName = store.getStoreName();
        Integer revenue = store.getRevenue();
        List<Map<String, Object>> existingStores = storeDAO.findStoreIdByName(storeName);
        if (!existingStores.isEmpty()) {
            return R.error(ErrorCode.STORE_DUPLICATE.getErrorCode(), ErrorCode.STORE_DUPLICATE.getErrorMsg());
        }
        try {
            boolean res = storeDAO.addStore(storeName, revenue);
            cacheDAO.deleteStoreMap();
            return res ? R.ok("Successfully inserted a store!") : R.error("Inserting store failed!");
        } catch (Exception e) {
            return R.error("Inserting store failed, " + e.getMessage());
        }
    }
}
