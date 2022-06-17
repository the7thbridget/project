package edu.gatech.cs6310.groceryexpress.service;

import edu.gatech.cs6310.groceryexpress.POJO.Item;
import edu.gatech.cs6310.groceryexpress.common.R;

public interface ItemService {
    R addItem(Item item) throws Throwable;
    R displayItemByStoreName(String storeName);

}
