package com.nettyim.server.task;

import io.netty.channel.ChannelHandlerContext;

import com.nettyim.server.model.ProtocolModel;

/**
 * 任务接口
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
public interface ITask {

    /**
     * 执行处理任务	  
     * @param ctx
     * @param protocolModel
     * @throws Exception
     */
    public void excute(ChannelHandlerContext ctx, ProtocolModel protocolModel) throws Exception;
}
