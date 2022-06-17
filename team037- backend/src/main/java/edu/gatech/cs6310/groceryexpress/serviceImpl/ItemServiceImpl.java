package edu.gatech.cs6310.groceryexpress.serviceImpl;


import com.fasterxml.jackson.core.JsonProcessingException;
import edu.gatech.cs6310.groceryexpress.DAO.ItemDAO;
import edu.gatech.cs6310.groceryexpress.DAO.StoreDAO;
import edu.gatech.cs6310.groceryexpress.POJO.Item;
import edu.gatech.cs6310.groceryexpress.common.CommonInfoHelper;
import edu.gatech.cs6310.groceryexpress.common.ErrorCode;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.common.exception.StoreNotFoundException;
import edu.gatech.cs6310.groceryexpress.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    StoreDAO storeDAO;

    @Autowired
    ItemDAO itemDAO;

    @Autowired
    CommonInfoHelper commonInfoHelper;

    @Override
    @Transactional
    public R addItem(Item item) throws Throwable {
        String itemName = item.getItemName();
        Integer weight = item.getWeight();
        String storeName = item.getStoreName();
        try {
            Integer storeId = commonInfoHelper.getStoreIdFromStoreName(storeName);
            List<Map<String, Object>> itemByStoreIdAndItemName = itemDAO.findItemByStoreIdAndItemName(storeId, itemName);
            if (!itemByStoreIdAndItemName.isEmpty())
                return R.error(ErrorCode.ITEM_DUPLICATE.getErrorCode(), ErrorCode.ITEM_DUPLICATE.getErrorMsg());
            boolean res = itemDAO.addItem(storeId, itemName, weight);
            return res ? R.ok("Successfully inserted an item!") : R.error("Failed to insert an item!");
        } catch (StoreNotFoundException e) {
            return R.error(ErrorCode.STORE_NOT_FOUND.getErrorCode(), ErrorCode.STORE_NOT_FOUND.getErrorMsg());
        } catch (Exception e) {
            return R.error("Failed to insert an item, " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public R displayItemByStoreName(String storeName) {
        try {
            Integer storeId = commonInfoHelper.getStoreIdFromStoreName(storeName);
            List<Map<String, Object>> items = itemDAO.findItemByStoreId(storeId);
            return R.ok().put("items", items);
        } catch (StoreNotFoundException e) {
            return R.error(ErrorCode.STORE_NOT_FOUND.getErrorCode(), ErrorCode.STORE_NOT_FOUND.getErrorMsg());
        } catch (JsonProcessingException e) {
            return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
        }
    }
}
