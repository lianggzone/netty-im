package com.nettyim.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.nettyim.server.common.constrants.Constants;
import com.nettyim.server.common.utils.JsonUtils;
import com.nettyim.server.entity.AuthModel;
import com.nettyim.server.entity.ChatModel;
import com.nettyim.server.entity.GroupChatModel;
import com.nettyim.server.enums.EventEnum;
import com.nettyim.server.model.ProtocolModel;
import com.nettyim.server.server.codec.ChatProtocolCodec;


public class ClientB {
    
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);  
	
	private static final String TOKEN = "1234567890";
	
    private static final long FROM_SHELL_ID = 852963;
    private static final long TO_SHELL_ID = 723181;
    
    
    private static long counter = 1;
    
    private final String host;
    private final int port;

    public ClientB(String host, int port) {
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
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, Integer.SIZE/8, -(Integer.SIZE/8), 0));
                    ch.pipeline().addLast(new ChatProtocolCodec());
                }
            });
            // 发起异步连接操作
            ChannelFuture future = bootstrap.connect(host, port).sync();

            // 发送消息
            // 发送认证消息
            AuthModel auth = new AuthModel();
            auth.setShellId(FROM_SHELL_ID);
            auth.setToken(TOKEN);
            
            ProtocolModel protocolModel = new ProtocolModel();
            protocolModel.setVersion((short) 1);
            protocolModel.setCommandId(EventEnum.AUTH_REQ.getValue());
            protocolModel.setShellId(FROM_SHELL_ID);
            protocolModel.setBody(JsonUtils.toJson(auth).getBytes());
            future.channel().writeAndFlush(protocolModel);
            
            TimeUnit.SECONDS.sleep(15);  
            
            // 发送单聊消息
            ProtocolModel protocolModel2 = null;
            for (int i = 0; i < 10; i++) {
                // 发送聊天消息
                ChatModel chatModel = new ChatModel();
                chatModel.setFromShellId(FROM_SHELL_ID);
                chatModel.setToShellId(TO_SHELL_ID);
                chatModel.setContent("你好，"+TO_SHELL_ID+", 我是"+FROM_SHELL_ID+"。 次数：" + (counter++));

                protocolModel2 = new ProtocolModel();
                protocolModel2.setVersion((short) 1);
                protocolModel2.setCommandId(EventEnum.CHAT_REQ.getValue());
                protocolModel2.setBody(JsonUtils.toJson(chatModel).getBytes());
                future.channel().writeAndFlush(protocolModel2);
            }
            
            // 发送群聊消息
            GroupChatModel groupChatModel = new GroupChatModel();
            groupChatModel.setFromShellId(FROM_SHELL_ID);
            groupChatModel.setContent("大家好, 我是"+FROM_SHELL_ID+"。 ");
            ProtocolModel protocolModel3 = new ProtocolModel();
            protocolModel3.setVersion((short) 1);
            protocolModel3.setCommandId(EventEnum.GROUP_CHAT_REQ.getValue());
            protocolModel3.setShellId(FROM_SHELL_ID);
            protocolModel3.setBody(JsonUtils.toJson(groupChatModel).getBytes());
            future.channel().writeAndFlush(protocolModel3);
            
            // 等待客户端链路关闭
            future.channel().closeFuture().sync();
        } finally {  
        	// 退出，释放NIO线程组
            workerGroup.shutdownGracefully().sync();
        	// 所有资源释放完成后，清空资源，再次发起重连操作
            executor.execute(new Runnable() {   
                @Override  
                public void run() {  
                    try {  
                        TimeUnit.SECONDS.sleep(5);  
                        start();  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    }  
                }  
            });  
        }  
    }

    public static void main(String[] args) throws Exception {  
        new ClientB("127.0.0.1", Constants.TCP_PORT).start();
    }
}
