package edu.gatech.cs6310.groceryexpress.common;

public enum DroneAssignRuleTypeEnum implements IDroneAssignRuleType{
    CAPACITY_FIRST(1),
    TRIP_FIRST(2)
    ;

    Integer droneAssignRuleType;

    private DroneAssignRuleTypeEnum(Integer type) {
        this.droneAssignRuleType = type;
    }

    @Override
    public Integer getDroneAssignRuleType() {
        return this.droneAssignRuleType;
    }
}
