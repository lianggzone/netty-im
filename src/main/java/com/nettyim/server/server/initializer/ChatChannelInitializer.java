package com.nettyim.server.server.initializer;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nettyim.server.server.codec.ChatProtocolCodec;
import com.nettyim.server.server.handle.AuthHandler;
import com.nettyim.server.server.handle.ChatHandler;

/**
 * ChatChannelInitializer
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
@SuppressWarnings("rawtypes")
@Component
public class ChatChannelInitializer extends ChannelInitializer {

    @Autowired
    private ChatProtocolCodec chatProtocolCodec;
    @Autowired
    private AuthHandler authHandler;
    @Autowired
    private ChatHandler chatHandler;

    @Override
    protected void initChannel(io.netty.channel.Channel ch) throws Exception {
    	ch.pipeline().addLast(new IdleStateHandler(30, 15, 5, TimeUnit.SECONDS));
    	
    	ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, Integer.SIZE/8, -(Integer.SIZE/8), 0));
        ch.pipeline().addLast(chatProtocolCodec);
        ch.pipeline().addLast(authHandler);
        ch.pipeline().addLast(chatHandler);
    }
}