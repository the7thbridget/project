package edu.gatech.cs6310.groceryexpress.Controller;

import edu.gatech.cs6310.groceryexpress.POJO.Order;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("/api/order/add")
    public R addOrder(@RequestBody Order order) throws Throwable {
        return orderService.addOrder(order);
    }

    @GetMapping("/api/order/all")
    public R displayOrderByStoreName(@RequestParam String storeName) {
        return orderService.displayOrderByStoreName(storeName);
    }

    @PostMapping("/api/order/request")
    public R requestItem(@RequestParam String storeName, @RequestParam String orderName, @RequestParam String itemName, @RequestParam Integer quantity, @RequestParam Integer unitPrice) {
        return orderService.requestItem(storeName, orderName, itemName, quantity, unitPrice);
    }

    @PostMapping("/api/order/purchase")
    public R purchaseOrder(@RequestParam String storeName, @RequestParam String orderName) {
        return orderService.purchaseOrder(storeName, orderName);
    }

    @PostMapping("/api/order/cancel")
    public R cancelOrder(@RequestParam String storeName, @RequestParam String orderName) {
        return orderService.cancelOrder(storeName, orderName);
    }

    @GetMapping("/api/order/showCustomerOrder")
    public R showCustomerOrder(@RequestParam String username, @RequestParam String storeName) {
        return orderService.showCustomerOrder(username, storeName);
    }

    @PostMapping("/api/order/manualReassignDrone")
    public R manualReassignDroneToOrder(@RequestParam Integer orderId, @RequestParam Integer droneId, @RequestParam String droneIdentifier) {
        return orderService.manualReassignDroneToOrder(orderId, droneId, droneIdentifier);
    }

    @PostMapping("/api/order/autoReassignDrone")
    public R autoReassignDroneToOrder(@RequestParam Integer orderId, @RequestParam Integer droneId) {
        return orderService.autoReassignDroneToOrder(orderId, droneId);
    }

}
