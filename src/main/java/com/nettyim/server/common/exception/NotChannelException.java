package com.nettyim.server.common.exception;

/** 
 * NotChannelException
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
@SuppressWarnings("serial")
public class NotChannelException extends NettyImException {
    
    public NotChannelException() {
        super("channelHandlerContext not found");
    }
}
