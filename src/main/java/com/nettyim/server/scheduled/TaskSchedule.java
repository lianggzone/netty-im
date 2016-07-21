package com.nettyim.server.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nettyim.server.common.constrants.CommonConstants;
import com.nettyim.server.task.TaskServer;

/**
 * <p>Title: 任务调度器         </p>
 * <p>Description: TaskSchedule </p>
 * <p>Create Time: 2016年3月30日           </p>
 * @author lianggz
 */
@Component
public class TaskSchedule{
	
	@Autowired
	private TaskServer taskServer;
	
	/**
     * 定时处理任务【每10秒执行一次队列任务】
     * @author lianggz
     */
    @Scheduled(cron="0/10 * * * * ?")
    public void execTask(){
        this.taskServer.execTask(CommonConstants.QUEUE_TASK_CONTENT);      
    }
    
    /**
     * 重做失败的任务【每15秒执行一次队列任务】
     * @author lianggz
     */
    /*@Scheduled(cron="0/15 * * * * ?")
    public void redoTask(){
        this.taskServer.redoTask();      
    }*/
}