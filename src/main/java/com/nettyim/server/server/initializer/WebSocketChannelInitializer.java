package com.nettyim.server.server.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nettyim.server.server.codec.WebSocketProtocolCodec;
import com.nettyim.server.server.handle.AuthHandler;
import com.nettyim.server.server.handle.ChatHandler;

/**
 * WebSocketChannelInitializer
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年8月29日  v0.1</p><p>版本内容: 创建</p>
 */
@Component
public class WebSocketChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    @Autowired
    private WebSocketProtocolCodec webSocketProtocolCodec;
    @Autowired
    private AuthHandler authHandler;
    @Autowired
    private ChatHandler chatHandler;

    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 编解码 http请求
        pipeline.addLast(new HttpServerCodec());
        // 写文件内容
        pipeline.addLast(new ChunkedWriteHandler());
        // 聚合解码 HttpRequest/HttpContent/LastHttpContent到FullHttpRequest
        // 保证接收的 Http请求的完整性
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        // 处理其他的 WebSocketFrame
        pipeline.addLast(new WebSocketServerProtocolHandler("/chat"));
        // 处理 TextWebSocketFrame
        pipeline.addLast(webSocketProtocolCodec);
        pipeline.addLast(authHandler);
        pipeline.addLast(chatHandler);
    }
}
