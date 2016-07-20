package com.nettyim.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.nettyim.server.common.utils.JsonUtils;
import com.nettyim.server.enums.EventEnum;
import com.nettyim.server.model.AuthTokenModel;
import com.nettyim.server.model.ChatMessageModel;
import com.nettyim.server.model.ProtocolModel;
import com.nettyim.server.server.codec.TcpProtocolCodec;


public class TcpChatClientA {
    
    private static final long FROM_URI = 723181;
    private static final long FROM_OS_TYPE = 1;
    private static final long TO_URI = 981101;
    private static final long TO_OS_TYPE = 1;
    private static final String TOKEN = "fba224321452016e";
    
    private static long counter = 1;
    
    private final String host;
    private final int port;

    public TcpChatClientA(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TcpProtocolCodec());
                }
            });
            // 发起异步连接操作
            ChannelFuture future = bootstrap.connect(host, port).sync();

            // 发送消息
            // 发送认证消息
            AuthTokenModel auth = new AuthTokenModel();
            auth.setUserId(FROM_URI);
            auth.setOsType(FROM_OS_TYPE);
            auth.setToken(TOKEN);
            
            ProtocolModel protocolModel = new ProtocolModel();
            protocolModel.setVersion((short) 1);
            protocolModel.setOperation(EventEnum.AUTH.getValue());
            protocolModel.setBody(JsonUtils.toJson(auth).getBytes());
            future.channel().writeAndFlush(protocolModel);
            
            Thread.sleep(15000L);
            
            for (int i = 0; i < 10; i++) {
                // 发送聊天消息
                ChatMessageModel chatMessage = new ChatMessageModel();
                chatMessage.setFromUri(FROM_URI);
                chatMessage.setFromOsType(FROM_OS_TYPE);
                chatMessage.setToUri(TO_URI);
                chatMessage.setToOsType(TO_OS_TYPE);
                chatMessage.setContent("你好，"+TO_URI+", 我是"+FROM_URI+"。 次数：" + (counter++));

                ProtocolModel protocolModel2 = new ProtocolModel();
                protocolModel2.setVersion((short) 1);
                protocolModel2.setOperation(EventEnum.MSG.getValue());
                protocolModel2.setBody(JsonUtils.toJson(chatMessage).getBytes());
                future.channel().writeAndFlush(protocolModel2);
                
                Thread.sleep(500L);
            }
            
            
            // 等待客户端链路关闭
            future.channel().closeFuture().sync();
        } finally {
            // 退出，释放NIO线程组
            workerGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {  
        new TcpChatClientA("127.0.0.1", 9099).start();
    }
}
