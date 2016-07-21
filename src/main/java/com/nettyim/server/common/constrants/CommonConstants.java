package com.nettyim.server.common.constrants;

/**
 * <p>Title: 常量类  </p>
 * <p>Description: CommonConstants </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
public class CommonConstants {

    public static final int PORT = 6099;
    
    // 策略
    /** 最大重发时间间隔,最大,一小时 */
    public static final long STRATEGY_MAX_INTERVAL = 60*60L;
    /** 最大重发时间间隔,最小,10秒 */
    public static final long STRATEGY_MIN_INTERVAL = 10L;
    /** 最大重发次数 */
    public static final long STRATEGY_MAX_TIMES = 60L;
    /** 最小重发次数 */
    public static final long STRATEGY_MIN_TIMES = 10L;
    
    /** 到期时间，最大截止时间的间隔 默认3天，即错误待重发的任务截止时间不超过3天  精确到毫秒 */
    public static final long STRATEGY_MAX_DLI = 3*24*60*60*1000L;
    
    // 队列名称
    public static final String QUEUE_TASK_CONTENT = "tcp_content";
    public static final String QUEUE_TASK_CHANNEL_CONTENT = "channel_content";
    public static final String QUEUE_TASK_REDO_INDEX = "redo_index";
    public static final String QUEUE_TASK_REDO_CONTENT = "redo_content";
    
    /** 消息队列 数量 */
    public static final int TASK_QUEUE_NUM = 10;
}
