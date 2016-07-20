package com.nettyim.server.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nettyim.server.common.constrants.CommonConstants;
import com.nettyim.server.common.utils.JsonUtils;
import com.nettyim.server.model.ProtocolModel;
import com.nettyim.server.redisdao.MsgListRedisDao;

/**
 * <p>Title: 任务业务服务类         </p>
 * <p>Description: TaskServer </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
@Service
public class TaskServer {
	
    private static final Logger logger = LoggerFactory.getLogger(TaskServer.class);
    
    @Autowired
    private MsgListRedisDao msgListRedisDao;
	
	/**
     * 执行任务(根据队列名前缀，获取对应的队列，每个队列取10条数据去处理)
     * @param prekey
     * @author lianggz
     */
    public void execTask(String preKey){
        List<String> fkeys = this.msgListRedisDao.getKeys(preKey);
        if(fkeys.isEmpty()){
            return;
        }
        for(String key : fkeys){            
            for(int i=0; i<CommonConstants.TASK_QUEUE_NUM; i++){
                String taskQueueStr = this.msgListRedisDao.getMsg(key);
                if(taskQueueStr == null){                              
                    break;
                }
                this.execTaskRunner(taskQueueStr);
            }
        }
    }
    
    private void execTaskRunner(String taskQueueStr){
        try {
            ProtocolModel protocolModel = JsonUtils.fromJson(taskQueueStr.getBytes(), ProtocolModel.class);
            Integer operation = protocolModel.getOperation();
            if (operation != null) {
                ITask task = TaskManager.getInstance().getTask(operation);
                if (task != null) {
                    logger.info(" messageReceived : {}", protocolModel);
                    task.excute(protocolModel);
                } else {
                    logger.warn("not found operation_id: " + protocolModel.getOperation());
                }
            }else{
                logger.warn("not found operation_id");
            }
        } catch (Exception e) {
            logger.error("execTaskRunner error:{}", e);
        }
        
    }
}