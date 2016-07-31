package com.nettyim.server.common.constrants;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * ClientSession
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
public class ClientSession {
	
    public static Map<String, ChannelHandlerContext> sessionMap = new HashMap<String, ChannelHandlerContext>();
}
