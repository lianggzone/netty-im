package com.nettyim.server.redisdao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Title: 异步消息REDIS DAO </p>
 * <p>Description: AsynMsgRedisDao </p>
 * <p>Create Time: 2016年3月30日           </p>
 * @author lianggz
 */
@Component
public class AsynMsgRedisDao {
    
    @Autowired
    private RedisBaseDao redisBaseDao;
    
    /**	
     * 添加单个数据         
     * @param key
     * @param hashKey
     * @param value
     * @author lianggz
     */
    public void setAsynMsg(String key, String hashKey, String value){
        redisBaseDao.addHashValue(key, hashKey, value);
    }
    
    /**
     * 获取单个数据      
     * @param key
     * @return
     * @author lianggz
     */
    public Map<String,String> getAsynMsg(String key){
        return redisBaseDao.getHashMap(key);
    }

    /**
     * 删除单个数据            
     * @param key
     * @param hashKey
     * @author lianggz
     */
	public void delAsynMsg(String key,String hashKey){
	    redisBaseDao.deleteHashValue(key, hashKey);
	}
}
