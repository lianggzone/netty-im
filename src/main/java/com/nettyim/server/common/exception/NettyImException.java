package com.nettyim.server.common.exception;

/**
 * <p>Title: NettyImException  </p>
 * <p>Description: NettyImException </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
public abstract class NettyImException extends Exception {
 
    public NettyImException(String message) {
        super(message);
    }

    public NettyImException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
