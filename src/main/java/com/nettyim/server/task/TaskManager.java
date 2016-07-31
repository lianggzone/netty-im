package com.nettyim.server.task;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.nettyim.server.enums.EventEnum;
import com.nettyim.server.task.impl.ChatTask;
import com.nettyim.server.task.impl.GroupChatTask;
import com.nettyim.server.task.impl.HeatBeatTask;

/**
 * 任务管理
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
@Component
public class TaskManager {

    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);
    
    private static TaskManager instance;

    public Map<Integer, ITask> taskMap = new HashMap<Integer, ITask>();

    public static TaskManager getInstance() {
        return instance;
    }

    public ITask getTask(int operation) {
        return taskMap.get(operation);
    }

    /**
     * 初始化处理过程
     */
    @PostConstruct
    private void init() {
        logger.info("init task manager");
        instance = new TaskManager();
        // 单聊消息任务
        instance.taskMap.put(EventEnum.CHAT_REQ.getValue(), new ChatTask());
        // 群聊消息任务
        instance.taskMap.put(EventEnum.GROUP_CHAT_REQ.getValue(), new GroupChatTask());
        // 心跳任务
        instance.taskMap.put(EventEnum.HEART_BEAT_REQ.getValue(), new HeatBeatTask());
        
    }
}
