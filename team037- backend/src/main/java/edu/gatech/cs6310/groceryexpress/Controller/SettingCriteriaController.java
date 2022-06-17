package edu.gatech.cs6310.groceryexpress.Controller;

import edu.gatech.cs6310.groceryexpress.POJO.SettingCriteria;
import edu.gatech.cs6310.groceryexpress.service.SettingCriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import edu.gatech.cs6310.groceryexpress.common.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingCriteriaController {

    @Autowired
    SettingCriteriaService settingCriteriaService;

    @PostMapping("/api/setting/updateSetting")
    public R updateSettingCriteria(@RequestBody SettingCriteria settingCriteria) {
        return settingCriteriaService.updateSettingCriteria(settingCriteria);
    }

}
