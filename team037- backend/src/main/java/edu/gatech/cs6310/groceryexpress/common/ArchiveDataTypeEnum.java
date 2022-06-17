package edu.gatech.cs6310.groceryexpress.common;

public enum ArchiveDataTypeEnum implements IArchiveDataType {
    CUSTOMER(1),
    ORDER(2),
    CUSTOMER_AND_ORDER(3)
    ;

    Integer archiveDataType;

    private ArchiveDataTypeEnum(Integer type) {
        this.archiveDataType = type;
    }

    @Override
    public Integer getArchiveDataType() {
        return this.archiveDataType;
    }
}
