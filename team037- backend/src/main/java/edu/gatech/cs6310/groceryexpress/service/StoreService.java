package edu.gatech.cs6310.groceryexpress.service;

import edu.gatech.cs6310.groceryexpress.POJO.Store;
import edu.gatech.cs6310.groceryexpress.common.R;

public interface StoreService {
    R displayAllStores();
    R addStore(Store store) throws Throwable;
}
