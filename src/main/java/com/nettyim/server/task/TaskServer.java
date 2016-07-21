package com.nettyim.server.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nettyim.server.common.constrants.CommonConstants;
import com.nettyim.server.common.exception.NotChannelException;
import com.nettyim.server.common.utils.JsonUtils;
import com.nettyim.server.model.ProtocolHandleModel;
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
    
    @Autowired
    private TaskCoreServer taskCoreServer;
	
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
                this.execTaskRunner(taskQueueStr, true, key);
            }
        }
    }
    
    /**
     * 处理任务
     * @param taskQueueStr 队列中数据
     * @param isRedo 是否要重做
     * @param key 队列名
     * @return
     * @author lianggz
     */
    private ProtocolHandleModel execTaskRunner(String taskQueueStr, boolean isRedo, String key){
        ProtocolHandleModel protocolHandleModel = this.taskCoreServer.getProtocolHandleModel(taskQueueStr, key);
        try { 
            ProtocolModel protocolModel = JsonUtils.fromJson(protocolHandleModel.getBody().getBytes(), ProtocolModel.class);
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
                logger.error("not found operation_id");
            }
            return protocolHandleModel;
        } catch (NotChannelException e){
            logger.error("execTaskRunner error:{}", e);
            this.taskCoreServer.addChannelTask(protocolHandleModel, key);
            return null; 
        } catch (Exception e) {
            logger.error("execTaskRunner error:{}", e);
            /*if(isRedo){
                this.taskCoreServer.addRedoTask(protocolHandleModel, key);
            }*/
            return null; 
        } 
    }
    
    /**
     * 重做失败的任务
     * @author lianggz
     */
    /*public void redoTask(){
        Long ts = new Date().getTime();     
        // 获取重做队列列表
        List<String> redoIndList = this.msgListRedisDao.getKeys(CommonConstants.QUEUE_TASK_REDO_INDEX + "-");
        if(redoIndList == null || redoIndList.isEmpty()){
            return;
        }
        // 重做任务
        for(String redoKey : redoIndList){
            String[] keys = redoKey.split("-");
            if(keys.length < 2){
                continue;
            }
            String key = keys[1];
            String prekey = "-" + key;  
            
            Set<String> ids = this.msgListRedisDao.getRetryIndex(CommonConstants.QUEUE_TASK_REDO_INDEX + prekey, 0L, ts);
            Iterator<String> it = ids.iterator();
            while(it.hasNext()){
                String hkey = it.next();
                String taskQueueStr = this.msgListRedisDao.getRetryCont(CommonConstants.QUEUE_TASK_REDO_CONTENT + prekey, hkey);
                ProtocolHandleModel protocolHandleModel  = null;
                try{
                    protocolHandleModel = JSON.parseObject(taskQueueStr, new TypeReference<ProtocolHandleModel>(){});            
                }catch(Exception e){
                    this.msgListRedisDao.deleteIndex(CommonConstants.QUEUE_TASK_REDO_INDEX + prekey, hkey);
                    this.msgListRedisDao.deleteCont(CommonConstants.QUEUE_TASK_REDO_CONTENT + prekey, hkey);
                    // TODO:记录错误日志
                    // do something 
                    logger.error("redo task Fail in redo processes:" + taskQueueStr);
                    continue;
                }
                HashMap<String,Object> strategy = this.taskCoreServer.proStrategy(protocolHandleModel.getStrategy());
                if(strategy == null){
                    this.msgListRedisDao.deleteIndex(CommonConstants.QUEUE_TASK_REDO_INDEX + prekey, hkey);
                    this.msgListRedisDao.deleteCont(CommonConstants.QUEUE_TASK_REDO_CONTENT + prekey, hkey);
                    // TODO:记录错误日志
                    // do something 
                    logger.error("redo task Fail in redo processes:" + taskQueueStr);
                    continue;
                }
                ProtocolHandleModel ret = this.execTaskRunner(taskQueueStr, false, key);
                if(ret != null){
                    this.msgListRedisDao.deleteIndex(CommonConstants.QUEUE_TASK_REDO_INDEX + prekey, hkey);
                    this.msgListRedisDao.deleteCont(CommonConstants.QUEUE_TASK_REDO_CONTENT + prekey, hkey);                    
                    String loglevel = protocolHandleModel.getLoglevel();
                    if(loglevel!=null && loglevel.equals("NORMAL")){
                        logger.error("redo task Fail in redo processes:" + taskQueueStr);
                    }
                    continue;
                }
                protocolHandleModel.setStrategy(strategy);
                String cont = JSON.toJSONString(protocolHandleModel);
                Long score = ts + Long.parseLong(strategy.get("interval").toString());
                this.msgListRedisDao.setRetryIndex(CommonConstants.QUEUE_TASK_REDO_INDEX + prekey, hkey, score);
                this.msgListRedisDao.setRetryCont(CommonConstants.QUEUE_TASK_REDO_CONTENT + prekey, hkey, cont);
           }
        }       
        return;
    }*/
}