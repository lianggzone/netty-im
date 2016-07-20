package com.nettyim.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

/**
 * <p>Title: Spring调度器         </p>
 * <p>Description: ScheduleConfig </p>
 * <p>Create Time: 2016年03月30日           </p>
 * @author lianggz
 */
@Configuration
@EnableScheduling
@ComponentScan(basePackages = { "com.nettyim.server.scheduled"})
public class ScheduleConfig {

    /**
     * 加载调度任务对象	     
     * @return
     * @author lianggz
     */
    @Bean
    public TaskScheduler getTaskScheduler() {
        return new ConcurrentTaskScheduler();
    }
}
