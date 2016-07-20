package com.nettyim.server.task.impl;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nettyim.server.model.ProtocolModel;
import com.nettyim.server.task.ITask;

/**
 * <p>Title: 认证任务  </p>
 * <p>Description: AuthTask </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
public class AuthTask implements ITask {

    private static final Logger logger = LoggerFactory.getLogger(AuthTask.class);

    @Override
    public void excute(ProtocolModel protocolModel) throws Exception {
       logger.info("task[auth] msg:{}", protocolModel);
    }
}
