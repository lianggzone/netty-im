package com.nettyim.server.common.constrants;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: ClientSession  </p>
 * <p>Description: ClientSession </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
public class ClientSession {
    public static Map<String, ChannelHandlerContext> sessionMap = new HashMap<String, ChannelHandlerContext>();
}
