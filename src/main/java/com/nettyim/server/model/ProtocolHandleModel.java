package com.nettyim.server.model;
import java.util.HashMap;

/**
 * <p>Title: 通用消息处理数据结构         </p>
 * <p>Description: ProtocolHandleModel </p>
 * <p>Create Time: 2016年7月22日           </p>
 * @author lianggz
 */
public class ProtocolHandleModel {
    
    
	private String body;                       // 消息体
	private HashMap<String,Object> addition;   // 业务的附加数据消息，需解析该字段并获取所需的数据进行处理。
	private HashMap<String,Object> strategy;   // 消息重发策略
	private String loglevel;                   // 记录文件日志的级别。NORMAL:不管最终处理成功或失败，都记录文件日志;ERROR:最终处理失败才记录文件日志。默认为ERROR级别
    
	
	
	public ProtocolHandleModel() {}
    public ProtocolHandleModel(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public HashMap<String, Object> getAddition() {
        return addition;
    }
    public void setAddition(HashMap<String, Object> addition) {
        this.addition = addition;
    }
    public HashMap<String, Object> getStrategy() {
        return strategy;
    }
    public void setStrategy(HashMap<String, Object> strategy) {
        this.strategy = strategy;
    }
    public String getLoglevel() {
        return loglevel;
    }
    public void setLoglevel(String loglevel) {
        this.loglevel = loglevel;
    }
}
