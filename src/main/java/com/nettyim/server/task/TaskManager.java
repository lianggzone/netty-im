package com.nettyim.server.task;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.nettyim.server.enums.EventEnum;
import com.nettyim.server.task.impl.AuthTask;
import com.nettyim.server.task.impl.ChatMessageTask;

/**
 * <p>Title: 任务管理  </p>
 * <p>Description: ITask </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
@Component
public class TaskManager {

    private static final Logger logger = LoggerFactory.getLogger(AuthTask.class);
    
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
        // 认证任务
        instance.taskMap.put(EventEnum.AUTH.getValue(), new AuthTask());
        // 聊天消息任务
        instance.taskMap.put(EventEnum.MSG.getValue(), new ChatMessageTask());
    }
}
