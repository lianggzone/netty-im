package com.nettyim.server.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nettyim.server.common.constrants.CommonConstants;
import com.nettyim.server.server.initializer.TcpChannelInitializer;

/**
 * <p>Title: TCP服务端  </p>
 * <p>Description: TcpImServer </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
@Component
public class TcpChatServer {

    private static final Logger logger = LoggerFactory.getLogger(TcpChatServer.class);

    @Autowired
    private TcpChannelInitializer tcpChannelInitializer;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup();
    private ChannelFuture channelFuture;
    
    /**
     * 开启	     
     * @throws Exception
     */
    public void start() throws Exception {
        try {
            logger.info("starting tcp server ... Port: " + CommonConstants.PORT);
            
            // 创建ServerBootstrap对象，它是Netty用于启动NIO服务端的辅助启动类， 目的是降低服务端的开发复杂度
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.handler(new LoggingHandler(LogLevel.INFO));
            // 绑定I/O事件的处理类
            bootstrap.childHandler(tcpChannelInitializer);
            // 绑定端口，同步等待成功
            ChannelFuture channelFuture = bootstrap.bind(CommonConstants.PORT).sync();
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
