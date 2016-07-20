package com.nettyim.server.common.exception;

/**
 * <p>Title: NotAuthException  </p>
 * <p>Description: NotAuthException </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
public class NotAuthException extends NettyImException {
    
    public NotAuthException() {
        super("未登录认证");
    }
}
