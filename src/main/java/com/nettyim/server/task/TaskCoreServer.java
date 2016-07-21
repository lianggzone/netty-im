package com.nettyim.server.task;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.nettyim.server.common.constrants.CommonConstants;
import com.nettyim.server.common.utils.DateUtil;
import com.nettyim.server.common.utils.JsonUtils;
import com.nettyim.server.model.ProtocolHandleModel;
import com.nettyim.server.model.ProtocolModel;
import com.nettyim.server.redisdao.MsgListRedisDao;

/**
 * <p>Title: 任务核心服务类         </p>
 * <p>Description: TaskCoreServer </p>
 * <p>Create Time: 2016年7月22日           </p>
 * @author lianggz
 */
@Service
public class TaskCoreServer  {
	
    private static final Logger logger = LoggerFactory.getLogger(TaskCoreServer.class);
    
    @Autowired
    private MsgListRedisDao msgListRedisDao;
    
	/**    
     * 策略处理并返回新的策略信息      
     * @param strategy
     * @return
     * @author lianggz
     */
    public HashMap<String,Object> proStrategy(HashMap<String,Object> strategy){
        // 如果不存在策略, 添加默认策略
        if(strategy == null){
            strategy = new HashMap<String,Object>(); 
            // 重试次数
            //strategy.put("times", CommonConstants.STRATEGY_MIN_TIMES-1);  
            // 重试时间间隔 默认10秒
            //strategy.put("interval", CommonConstants.STRATEGY_MIN_INTERVAL);        
            // 到期时间，unix时间戳精确到毫秒
            strategy.put("deadline", DateUtil.getTodayEndTs());               
        }
        // 如果存在策略, 进行策略处理
        else{
            // 消息策略, 重试次数
            /*Object times = strategy.get("times");
            if(times == null){
                strategy.put("times", CommonConstants.STRATEGY_MIN_TIMES);
            }else{
                try{
                    Long tms = Long.parseLong(times.toString())-1;
                    if(tms < 0){
                        return null;
                    }
                    if(tms > CommonConstants.STRATEGY_MAX_TIMES){
                        tms = CommonConstants.STRATEGY_MAX_TIMES-1;
                    }
                    strategy.put("times", tms);
                }catch (Exception e){
                    strategy.put("times", CommonConstants.STRATEGY_MIN_TIMES-1);                  
                }
            }*/
            // 消息策略, 重试时间间隔
            /*Object intval = strategy.get("interval");
            if(intval == null){
                strategy.put("interval", CommonConstants.STRATEGY_MIN_INTERVAL);
            }else{
                try{
                    Long interval = Long.parseLong(intval.toString());
                    if(interval > CommonConstants.STRATEGY_MAX_INTERVAL || interval < CommonConstants.STRATEGY_MIN_INTERVAL){
                        interval = CommonConstants.STRATEGY_MIN_INTERVAL;
                    }       
                    strategy.put("interval", interval);
                }catch (Exception e){
                    strategy.put("interval", CommonConstants.STRATEGY_MIN_INTERVAL);  
                }
            }*/
            // 消息策略, 到期时间
            Object dl = strategy.get("deadline");
            if(dl == null){
                strategy.put("deadline", DateUtil.getTodayEndTs());
            }else{
                try{
                    Long deadLine = Long.parseLong(dl.toString());
                    Long ts = new Date().getTime();
                    Long lastTime = ts + CommonConstants.STRATEGY_MAX_DLI; 
                    if(deadLine > lastTime){
                        deadLine = lastTime;
                        strategy.put("deadline", deadLine);
                    }
                    if(deadLine < ts){
                        return null;
                    }
                }catch (Exception e){
                    strategy.put("deadline", DateUtil.getTodayEndTs());
                }
            }           
        }
        return strategy;
    }
    
    /**
     * 获取通用消息数据结构       
     * @param taskQueueStr 任务队列字符串
     * @param key
     * @return
     * @author lianggz
     */
    public ProtocolHandleModel getProtocolHandleModel(String taskQueueStr, String key) {
        ProtocolHandleModel protocolHandleModel = null;
        try{
            protocolHandleModel = JSON.parseObject(taskQueueStr, new TypeReference<ProtocolHandleModel>(){});            
        }catch(Exception e){
            logger.error("获取通用消息数据结构失败：{}", e);
            //TODO:记录错误日志
            // do something 
            return null;
        }
        if(protocolHandleModel == null){
            return null;
        }
        // 查看开始时间是否大于当前时间，若是，则不处理插回队列
        if(protocolHandleModel.getStrategy() != null){ 
            Long startTime = 0L;
            try{
                HashMap<String,Object> strategy = protocolHandleModel.getStrategy();
                startTime = (long)strategy.get("starttime");
            }catch(Exception e){
                startTime = 0L;
            }
            if(startTime > System.currentTimeMillis()){
                this.msgListRedisDao.setMsg(key, taskQueueStr);
                return null;
            }           
        }
        return protocolHandleModel;
    }
    
    /** 
     * 写到重做队列中      
     * @param commonMsgModel 结构体
     * @param key 主键
     * @author lianggz
     */
    /*public void addRedoTask(ProtocolHandleModel protocolHandleModel, String key){
        HashMap<String,Object> strategy = this.proStrategy(protocolHandleModel.getStrategy());
        if(strategy == null){           
            return;
        }
        protocolHandleModel.setStrategy(strategy);
        String cont = JSON.toJSONString(protocolHandleModel);
        String uuid = UUID.randomUUID().toString();
        Long ts = new Date().getTime();
        Long score = ts + Long.parseLong(strategy.get("interval").toString());
        this.msgListRedisDao.setRetryIndex(CommonConstants.QUEUE_TASK_REDO_INDEX+"-"+key, uuid, score);
        this.msgListRedisDao.setRetryCont(CommonConstants.QUEUE_TASK_REDO_CONTENT+"-"+key, uuid, cont); 
    }*/
    
    /** 
     * 写到离线通信队列中      
     * @param commonMsgModel 结构体
     * @param key 主键
     * @author lianggz
     * @throws Exception 
     */
    public void addChannelTask(ProtocolHandleModel protocolHandleModel, String key) {
        HashMap<String,Object> strategy = this.proStrategy(protocolHandleModel.getStrategy());
        if(strategy == null){           
            return;
        }
        protocolHandleModel.setStrategy(strategy);
        String cont = JSON.toJSONString(protocolHandleModel);
        ProtocolModel protocolModel = null;
        try {
            protocolModel = JsonUtils.fromJson(protocolHandleModel.getBody().getBytes(), ProtocolModel.class);
        } catch (Exception e) {
            // TODO: handle exception
        }
        String uuid = UUID.randomUUID().toString();
        // TODO: handle
        this.msgListRedisDao.setRetryCont(CommonConstants.QUEUE_TASK_CHANNEL_CONTENT, uuid, cont);
    }
}