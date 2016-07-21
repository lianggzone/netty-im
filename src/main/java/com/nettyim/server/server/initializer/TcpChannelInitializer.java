package com.nettyim.server.server.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nettyim.server.server.codec.TcpProtocolCodec;
import com.nettyim.server.server.handle.TcpChatServerHandler;

/**
 * <p>Title: TcpChannelInitializer  </p>
 * <p>Description: TcpChannelInitializer </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
@Component
public class TcpChannelInitializer extends ChannelInitializer {

    @Autowired
    private TcpProtocolCodec tcpProtocolCodec;
    @Autowired
    private TcpChatServerHandler tcpChatServerHandler;

    @Override
    protected void initChannel(io.netty.channel.Channel ch) throws Exception {
        ch.pipeline().addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, Integer.SIZE/8, -(Integer.SIZE/8), 0));
        ch.pipeline().addLast("TcpProtocolCodec", tcpProtocolCodec);
        ch.pipeline().addLast(tcpChatServerHandler);
    }
}