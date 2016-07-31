package com.nettyim.server.server.handle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nettyim.server.common.constrants.ClientSession;
import com.nettyim.server.common.utils.JsonUtils;
import com.nettyim.server.entity.AuthModel;
import com.nettyim.server.enums.EventEnum;
import com.nettyim.server.model.ProtocolModel;
import com.nettyim.server.service.AuthEventService;

/**
 * 登陆认证
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
@Component
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<ProtocolModel>{  
     
	@Autowired
    private AuthEventService authEventService;
	
    @Override  
    public void channelRead0(ChannelHandlerContext ctx, ProtocolModel protocolModel) throws Exception {  
    	if (protocolModel.getCommandId() == EventEnum.AUTH_REQ.getValue()){
    		if(protocolModel.getBody() == null){
    			 ctx.close();
    		}else{
    			AuthModel authModel = JsonUtils.fromJson(protocolModel.getBody(), AuthModel.class);
    			
                String key = authModel.getShellId() + "";
                authEventService.setAuthToken(ctx.channel(), authModel);
                
                ClientSession.sessionMap.put(key, ctx);
                
                // 回写ACK
                protocolModel.setCommandId(EventEnum.AUTH_RESP.getValue());
                ctx.writeAndFlush(protocolModel);  
    		} 
        } else {  
        	ctx.fireChannelRead(protocolModel);  
        }  
    } 
      
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  
            throws Exception {  
        ctx.fireExceptionCaught(cause);  
    }
} 