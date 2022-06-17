package edu.gatech.cs6310.groceryexpress.Controller;

import edu.gatech.cs6310.groceryexpress.POJO.Item;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {
    @Autowired
    ItemService itemService;

    @PostMapping("/api/item/add")
    public R addItem(@RequestBody Item item) throws Throwable {
        return itemService.addItem(item);
    }

    @GetMapping("/api/item/all")
    public R displayItemByStoreName(@RequestParam String storeName) {
        return itemService.displayItemByStoreName(storeName);
    }
}
