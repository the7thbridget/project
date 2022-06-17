package edu.gatech.cs6310.groceryexpress.Controller;

import edu.gatech.cs6310.groceryexpress.POJO.Store;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController {
    @Autowired
    StoreService storeService;

    @PostMapping("/api/store/add")
    public R addStore(@RequestBody Store store) throws Throwable {
        return storeService.addStore(store);
    }

    @GetMapping("/api/store/all")
    public R displayAllStores() {
        return storeService.displayAllStores();
    }


}
