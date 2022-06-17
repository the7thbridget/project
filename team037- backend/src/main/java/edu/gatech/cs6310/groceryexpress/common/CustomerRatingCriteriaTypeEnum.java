package edu.gatech.cs6310.groceryexpress.common;

public enum CustomerRatingCriteriaTypeEnum implements ICustomerRatingCriteriaType {
    ORDER_NUMBER(1),
    ORDER_VALUE(2)
    ;

    Integer customerRatingCriteriaType;

    private CustomerRatingCriteriaTypeEnum(Integer type) {
        this.customerRatingCriteriaType = type;
    }

    @Override
    public Integer getCustomerRatingCriteriaType() {
        return this.customerRatingCriteriaType;
    }
}
