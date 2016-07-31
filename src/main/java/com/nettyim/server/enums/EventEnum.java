package com.nettyim.server.enums;

/**
 * 操作类型枚举
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
public enum EventEnum {
	
	/** 握手请求消息*/
    AUTH_REQ(1),
    /** 握手响应消息*/
    AUTH_RESP(2),
    
	/** 单聊请求消息 */
    CHAT_REQ(3),
    /** 单聊响应消息 */
    CHAT_RESP(4),
    
    /** 群聊请求消息 */
    GROUP_CHAT_REQ(5),
    /** 群聊响应消息 */
    GROUP_CHAT_RESP(6),
    
    /** 心跳请求消息*/
    HEART_BEAT_REQ(7),
    /** 心跳响应消息*/
    HEART_BEAT_RESP(8)
    
    ;

    private Integer value;

    public Integer getValue() {
        return value;
    }

    EventEnum(Integer value) {
        this.value = value;
    }
}
