package com.nettyim.server.server.handle;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nettyim.server.common.constrants.ClientSession;
import com.nettyim.server.entity.AuthModel;
import com.nettyim.server.enums.EventEnum;
import com.nettyim.server.model.ProtocolModel;
import com.nettyim.server.service.AuthEventService;
import com.nettyim.server.task.ITask;
import com.nettyim.server.task.TaskManager;

/**
 * 聊天服务（单聊）
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
@Component
@ChannelHandler.Sharable
public class ChatHandler extends SimpleChannelInboundHandler<ProtocolModel> {

    private static final Logger logger = LoggerFactory.getLogger(ChatHandler.class);
  
    @Autowired
    private AuthEventService authEventService;
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolModel protocolModel) throws Exception {
        logger.info("服务端接收到客户端消息:{}", protocolModel);
        
        Integer commandId = protocolModel.getCommandId();
        if (commandId != null) {
            ITask task = TaskManager.getInstance().getTask(commandId);
            if (task != null) {
                task.excute(ctx, protocolModel);
            } else {
                logger.error("not found command_id: " + protocolModel.getCommandId());
            }
        }else{
            logger.error("not found command_id");
        }
    }
    
    @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	logger.info("链路激活");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("服务端监听到客户端退出 ，进行业务处理");

        AuthModel authModel = authEventService.getAuthToken(ctx.channel());
        if(authModel != null){
            String key = authModel.getShellId() + "";
            if(StringUtils.isNoneBlank(key)){
                ClientSession.sessionMap.remove(key);
            }
            authEventService.clearAuthToken(ctx.channel());
            logger.info("服务端监听到客户端退出 ，业务处理成功");
        }
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught", cause);
    }
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                //未进行读操作
            	logger.info("READER_IDLE");      
                ctx.close();
            } 
            else if (event.state().equals(IdleState.WRITER_IDLE)) {
                //未进行写操作
            	logger.info("WRITER_IDLE");  
            } 
            else if (event.state().equals(IdleState.ALL_IDLE)) {
                //未进行读写操作
            	logger.info("ALL_IDLE");  
                // 发送心跳消息
            	ProtocolModel protocolModel = new ProtocolModel();
            	protocolModel.setVersion(ProtocolModel.CUR_VERSION);
            	protocolModel.setCommandId(EventEnum.HEART_BEAT_REQ.getValue());

            	ChannelFuture future = ctx.writeAndFlush(protocolModel);
            	
            	future.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        if(!future.isSuccess()){
                        	ctx.channel().close();
                        }
                    }
                });
            }
        }
    }
}
