package com.huahua.socket.enums;

public enum WebSocketServerMsgTypeEnum {
    SUCCESS_LOGIN(1,"SUCCESS_LOGIN"),
    NEW_CARE_DATA(2,"NEW_CARE_DATA"),
    PING(3,"PING"),
    PONG(4,"PONG");
    private Integer code;
    private String value;
    WebSocketServerMsgTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
