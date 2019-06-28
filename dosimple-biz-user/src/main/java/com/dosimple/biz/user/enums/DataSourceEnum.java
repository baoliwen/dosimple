package com.dosimple.biz.user.enums;

/**
 * @author dosimple
 */
public enum  DataSourceEnum {
    DB1("db1"),MASTER("master"),DB2("db2"),DB3("db3");

    private String value;

    DataSourceEnum (String value){this.value=value;}

    public String getValue() {
        return value;
    }
}
