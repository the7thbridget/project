package edu.gatech.cs6310.groceryexpress.POJO;

import lombok.Data;

@Data
public class SettingCriteria {
    private Integer droneAssignRule;
    private Integer customerRatingRule;
    private Integer archiveDataType;
    private Integer timePeriod;
    private Integer archivePeriod;
    // Threshold for Customer Rating
    /*
    private Integer rating_by_order_num_1_2;
    private Integer rating_by_order_num_2_3;
    private Integer rating_by_order_num_3_4;
    private Integer rating_by_order_num_4_5;
    private Integer rating_by_order_value_1_2;
    private Integer rating_by_order_value_2_3;
    private Integer rating_by_order_value_3_4;
    private Integer rating_by_order_value_4_5;
     */

}
