package com.nettyim.server.task.impl;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.nettyim.server.common.constrants.ClientSession;
import com.nettyim.server.common.utils.JsonUtils;
import com.nettyim.server.entity.ChatModel;
import com.nettyim.server.enums.EventEnum;
import com.nettyim.server.model.ProtocolModel;
import com.nettyim.server.task.ITask;

/**
 * 单聊消息任务 
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
public class ChatTask implements ITask {

    private static final Logger logger = LoggerFactory.getLogger(ChatTask.class);

    @Override
    public void excute(ChannelHandlerContext ctx, ProtocolModel protocolModel) throws Exception {
        logger.info("task[char-message] msg:{}", protocolModel);
        
        ChatModel chatModel = JsonUtils.fromJson(protocolModel.getBody(), ChatModel.class);
        String key = chatModel.getToShellId() + "";
        
        // 回写ACK
        ProtocolModel reqProtocolModel = new ProtocolModel();
        BeanUtils.copyProperties(protocolModel, reqProtocolModel);
        reqProtocolModel.setCommandId(EventEnum.CHAT_RESP.getValue());
        ctx.pipeline().writeAndFlush(reqProtocolModel);
        
        // 发送消息
        ChannelHandlerContext subCtx = ClientSession.sessionMap.get(key);
        if (subCtx != null) {
            if (subCtx.channel().isWritable()) {
                logger.info("task[char-message]向设备发送数据 ");
                subCtx.pipeline().writeAndFlush(protocolModel);
            } else {
                logger.info("task[char-message]设备不可写,无法发送数据,关闭连接");
                subCtx.pipeline().close();
            }
        }else{
            //throw new NotChannelException();
        }
    }
}