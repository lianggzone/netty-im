package com.nettyim.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>Title: 认证消息模型  </p>
 * <p>Description: AuthTokenModel </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
public class AuthTokenModel {

    @JsonProperty("user_id")
    private long userId;
    
    @JsonProperty("os_type")
    private long osType;
    
    @JsonProperty("token")
    private String token;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public long getOsType() {
        return osType;
    }

    public void setOsType(long osType) {
        this.osType = osType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("AuthToken{userId:").append(this.userId).append(", ");
        sb.append("osType:").append(this.osType).append(", ");
        sb.append("token:").append(this.token).append("}");
        return sb.toString();
    }
}
