package com.nettyim.server.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nettyim.server.common.constrants.Constants;
import com.nettyim.server.server.initializer.ChatChannelInitializer;

/**
 * 消息聊天服务端
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
@Component
public class ChatServer {

    private static final Logger logger = LoggerFactory.getLogger(ChatServer.class);

    @Autowired
    private ChatChannelInitializer chatChannelInitializer;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup();
    private ChannelFuture channelFuture;
    
    /**
     * 开启	     
     * @throws Exception
     */
    public void start() throws Exception {
        try {
            logger.info("starting tcp server ... Port: " + Constants.PORT);
            
            // 创建ServerBootstrap对象，它是Netty用于启动NIO服务端的辅助启动类， 目的是降低服务端的开发复杂度
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            
            // 设置参数
            // serverSocketChannel的设置，连接缓存池的大小
            // bootstrap.option(ChannelOption.SO_BACKLOG, 100000);
            // socketChannel的设置,维持连接的活跃，清楚死链接
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            // socketChannel的设置,关闭延迟发送
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            
            // 处理业务
            bootstrap.handler(new LoggingHandler(LogLevel.INFO));
            // 绑定I/O事件的处理类
            bootstrap.childHandler(chatChannelInitializer);
            // 绑定端口，同步等待成功
            bootstrap.bind(Constants.PORT).sync();
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    shutdown();
                }
            });
        }
    }

    /**
     * 重启      
     * @throws Exception
     */
    public void restart() throws Exception {
        shutdown();
        start();
    }

    /**
     * 关闭
     */
    public void shutdown() {
        // 释放线程池资源
        if (channelFuture != null) {
            channelFuture.channel().close().syncUninterruptibly();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
    }
}
