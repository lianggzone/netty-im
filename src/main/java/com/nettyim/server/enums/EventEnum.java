package com.nettyim.server.enums;

public enum EventEnum {

    /** 连接验证 */
    AUTH(1),
    /** 消息转发*/
    MSG(2);

    private Integer value;

    public Integer getValue() {
        return value;
    }

    EventEnum(Integer value) {
        this.value = value;
    }
}
