package com.nettyim.server.server.handle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nettyim.server.common.constrants.ClientSession;
import com.nettyim.server.common.constrants.CommonConstants;
import com.nettyim.server.common.utils.JsonUtils;
import com.nettyim.server.enums.EventEnum;
import com.nettyim.server.model.AuthTokenModel;
import com.nettyim.server.model.ProtocolModel;
import com.nettyim.server.redisdao.MsgListRedisDao;
import com.nettyim.server.service.AuthEventService;

/**
 * <p>Title: TcpChatServerHandler  </p>
 * <p>Description: TcpChatServerHandler </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
@Component
@ChannelHandler.Sharable
public class TcpChatServerHandler extends SimpleChannelInboundHandler<ProtocolModel> {

    private static final Logger logger = LoggerFactory.getLogger(TcpChatServerHandler.class);
  
    @Autowired
    private MsgListRedisDao msgListRedisDao;
    
    @Autowired
    private AuthEventService authEventService;
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolModel protocolModel) throws Exception {
        logger.info("服务端接收到客户端消息:{}", protocolModel);
        if(EventEnum.AUTH.getValue() == protocolModel.getOperation()){
            AuthTokenModel authTokenModel = JsonUtils.fromJson(protocolModel.getBody(), AuthTokenModel.class);

            String key = authTokenModel.getUserId() + "_" + authTokenModel.getOsType();
                       
            authEventService.setAuthToken(ctx.channel(), authTokenModel);
            ClientSession.sessionMap.put(key, ctx);
        }
        // 服务器消息处理
        msgListRedisDao.setMsg(CommonConstants.QUEUE_TASK_NAME, JsonUtils.toJson(protocolModel));
        
        //ctx.pipeline().writeAndFlush(protocolModel);
        //ctx.pipeline().close();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info(" 服务端监听到客户端退出 ，进行业务处理");
        AuthTokenModel authTokenModel = authEventService.getAuthToken(ctx.channel());
        if(authTokenModel != null){
            String key = authTokenModel.getUserId() + "_" + authTokenModel.getOsType();
            if(StringUtils.isNoneBlank(key)){
                ClientSession.sessionMap.remove(key);
            }
            authEventService.clearAuthToken(ctx.channel());
            logger.info(" 服务端监听到客户端退出 ，业务处理成功 ");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught", cause);
    }
}
