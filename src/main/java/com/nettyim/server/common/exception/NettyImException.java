package com.nettyim.server.common.exception;

/**
 * NettyImException
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
@SuppressWarnings("serial")
public abstract class NettyImException extends Exception {
 
    public NettyImException(String message) {
        super(message);
    }

    public NettyImException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
