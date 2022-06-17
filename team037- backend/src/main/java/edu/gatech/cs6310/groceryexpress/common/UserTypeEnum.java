package edu.gatech.cs6310.groceryexpress.common;

public enum UserTypeEnum implements IUserType{

    ADMIN(1),
    EMPLOYEE(2),
    CUSTOMER(3)
    ;

    Integer userType;

    private UserTypeEnum(Integer type) {
        this.userType = type;
    }

    @Override
    public Integer getUserTypeCode() {
        return userType;
    }
}
