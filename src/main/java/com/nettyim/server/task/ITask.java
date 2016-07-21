package com.nettyim.server.task;

import com.nettyim.server.model.ProtocolModel;

/**
 * <p>Title: 任务接口  </p>
 * <p>Description: ITask </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
public interface ITask {

    /**
     * 执行处理任务	  
     * @param protocolModel
     * @throws Exception
     */
    public void excute(ProtocolModel protocolModel) throws Exception;
}
