package com.nettyim.server.task.impl;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nettyim.server.common.constrants.ClientSession;
import com.nettyim.server.common.utils.JsonUtils;
import com.nettyim.server.model.ChatMessageModel;
import com.nettyim.server.model.ProtocolModel;
import com.nettyim.server.task.ITask;

/**
 * <p>Title: 聊天消息任务  </p>
 * <p>Description: ChatMessageTask </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
public class ChatMessageTask implements ITask {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageTask.class);

    @Override
    public void excute(ProtocolModel protocolModel) throws Exception {
        logger.info("task[char-message] msg:{}", protocolModel);
        ChatMessageModel chatMessageModel = JsonUtils.fromJson(protocolModel.getBody(), ChatMessageModel.class);
        String key = chatMessageModel.getToUri() + "_" + chatMessageModel.getToOsType();
        ChannelHandlerContext ctx = ClientSession.sessionMap.get(key);
        if (ctx!=null) {
            if (ctx.channel().isWritable()) {
                logger.info("task[char-message]向设备发送数据 ");
                ctx.pipeline().writeAndFlush(protocolModel);
            } else {
                logger.info("task[char-message]设备不可写，无法发送数据,关闭连接");
                ctx.pipeline().close();
            }
        }else{
            logger.info("task[char-message]channelHandlerContext not found");
        }
        
    }
}