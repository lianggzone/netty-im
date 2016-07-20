package com.nettyim.server.redisdao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Title: 异步任务REDIS DAO </p>
 * <p>Description: MsgListRedisDao </p>
 * <p>Create Time: 2016年3月30日           </p>
 * @author lianggz
 */
@Component
public class MsgListRedisDao {
    
    @Autowired
    private RedisBaseDao redisBaseDao;
    
    /**
     * 获取队列数量        
     * @param key
     * @return
     * @author lianggz
     */
    public Long getListSize(String key){       
        return redisBaseDao.getListSize(key);
    }
    
    /**
     * 弹出队列             
     * @param key
     * @return
     * @author lianggz
     */
    public String getMsg(String key){       
        return redisBaseDao.popListLeft(key);
    }
    
    /** 
     * 进入队列      
     * @param key
     * @param value
     */
    public void setMsg(String key,String value){
        redisBaseDao.pushListRight(key, value);
    }
    
    /** 
     * 所有keys      
     * @param pattern
     * @author lianggz
     */
    public List<String> getKeys(String preKey){
        return new ArrayList<String>(redisBaseDao.keys(preKey+"*"));        
    }
    
    /**
     * 设置zset值
     * @param key
     * @param member
     * @param score
     * @author lianggz
     */
    public void setRetryIndex(String key, String value, long score){
        redisBaseDao.addZSetValue(key, value, score);
        
    }

    /** 
     * 获取有序集 key      
     * @param key
     * @param min
     * @param max
     * @return
     * @author lianggz
     */
    public Set<String> getRetryIndex(String key,long min,long max){
        return redisBaseDao.getZSetRangeByScore(key, min, max);
    }
    
    /** 
     * 添加单个数据         
     * @param key
     * @param hashKey
     * @param value
     * @author lianggz
     */
    public void setRetryCont(String key,String hashKey,String value){
        redisBaseDao.addHashValue(key, hashKey, value);
    }
    
    /** 
     * 获取单个数据       
     * @param key
     * @param hashKey
     * @return
     * @author lianggz
     */
    public String getRetryCont(String key,String hashKey){
        return redisBaseDao.getHashValue(key, hashKey);
    }
    
    /**
     * 删除zset值
     * @param key
     * @param values
     * @author lianggz
     */
    public void deleteIndex(String key, String values){
        redisBaseDao.deleteZSetValue(key, values);
    }
    
    /** 
     * 删除单个数据         
     * @param key
     * @param hashKeys
     * @author lianggz
     */
    public void deleteCont(String key,String hashKeys){
        redisBaseDao.deleteHashValue(key, hashKeys);
    }

    /**
     * 获取队列集合      
     * @param key
     * @param start
     * @param end
     * @return
     * @author lianggz
     */
	public List<String> getMsgRange(String key,long start,long end){
		return redisBaseDao.getListMsgRange(key, start, end);
	}
	
	/**
     * 获取队列集合      
     * @param key
     * @return
     * @author lianggz
     */
	public List<String> getMsgRange(String key){		
		return redisBaseDao.getListMsgRange(key, 0, -1);
	}
}