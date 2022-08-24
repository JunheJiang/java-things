package com.ljp.util;

public enum ResponseEnum implements CodeTypeEnumInte {

    SUCCESS_EN("SUCCESS", "成功"),
    FALSE_EN("ERROR", "失败"),
    NOLOGIN("NOLOGIN","未登录"),
    ERROR_PARAMETER("ERROR_PARAMETER","参数错误"),
    ;

    private String code;
    private String desc;//中文描述

    private ResponseEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        // TODO Auto-generated method stub
        return code;
    }

    @Override
    public String getDesc() {
        // TODO Auto-generated method stub
        return desc;
    }
}
