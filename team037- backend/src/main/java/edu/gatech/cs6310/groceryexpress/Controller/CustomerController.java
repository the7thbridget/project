package edu.gatech.cs6310.groceryexpress.Controller;

import edu.gatech.cs6310.groceryexpress.POJO.Customer;
import edu.gatech.cs6310.groceryexpress.common.R;
import edu.gatech.cs6310.groceryexpress.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;

@RestController
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping("/api/customer/add")
    public R addCustomer(@RequestBody Customer customer) throws Throwable {
        return customerService.addCustomer(customer);
    }

    @GetMapping("/api/customer/all")
    public R displayCustomerData() {
        return customerService.displayCustomerData();
    }


//    @GetMapping("/api/customer/showArchive")
//    public R showCustomerData(@RequestParam Integer archiveData) {
//        return customerService.showCustomerData(archiveData);
//    }

    @PostMapping("/api/customer/manualReassignRating")
    public R manualReassignCustomerRating(@RequestParam Integer userId, @RequestParam Integer manualRating) throws SQLException {
        return customerService.manualReassignCustomerRating(userId, manualRating) ;
    }

    @PostMapping("/api/customer/autoReassignRating")
    public R autoReassignRating(@RequestParam Integer userId) {
        return customerService.autoReassignRating(userId);
    }

}
