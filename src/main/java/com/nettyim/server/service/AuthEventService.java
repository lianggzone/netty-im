package com.nettyim.server.service;

import io.netty.channel.Channel;

import org.springframework.stereotype.Component;

import com.nettyim.server.common.constrants.AttributeKeyConstrants;
import com.nettyim.server.common.exception.NotAuthException;
import com.nettyim.server.model.AuthTokenModel;

/**
 * <p>Title: AuthEventService  </p>
 * <p>Description: AuthEventService </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
@Component
public class AuthEventService {

    /**
     * 设置AuthTokenModel     
     * @param ch
     * @return
     */
    public AuthTokenModel getAuthToken(Channel ch) {
        return ch.attr(AttributeKeyConstrants.KEY_CLIENT_ID).get();
    }

    /**
     * 获取AuthTokenModel   
     * @param ch
     * @param token
     */
    public void setAuthToken(Channel ch, AuthTokenModel token) {
        ch.attr(AttributeKeyConstrants.KEY_CLIENT_ID).set(token);
    }
    
    /**
     * 清空AuthTokenModel	    
     * @param ch
     * @param token
     */
    public void clearAuthToken(Channel ch) {
        if (getAuthToken(ch) != null) {
            ch.attr(AttributeKeyConstrants.KEY_CLIENT_ID).remove();
        }
    }

    /**
     * 检查AuthTokenModel	    
     * @param ch
     * @throws NotAuthException
     */
    public void checkAuth(Channel ch) throws NotAuthException {
        if (getAuthToken(ch) == null) {
            throw new NotAuthException();
        }
    }

}