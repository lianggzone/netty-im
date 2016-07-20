package com.nettyim.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>Title: 聊天消息模型  </p>
 * <p>Description: AuthTokenModel </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
public class ChatMessageModel {
    
    @JsonProperty("from_uri")
    private Long fromUri;
    
    @JsonProperty("from_os_type")
    private Long fromOsType;
    
    @JsonProperty("to_uri")
    private Long toUri;
    
    @JsonProperty("to_os_type")
    private Long toOsType;
    
    @JsonProperty("content")
    private String content;

    public Long getFromUri() {
        return fromUri;
    }

    public void setFromUri(Long fromUri) {
        this.fromUri = fromUri;
    }

    public Long getToUri() {
        return toUri;
    }

    public void setToUri(Long toUri) {
        this.toUri = toUri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public Long getFromOsType() {
        return fromOsType;
    }

    public void setFromOsType(Long fromOsType) {
        this.fromOsType = fromOsType;
    }

    public Long getToOsType() {
        return toOsType;
    }

    public void setToOsType(Long toOsType) {
        this.toOsType = toOsType;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ChatMessage{fromUri:").append(this.fromUri).append(", ");
        sb.append("fromOsType:").append(this.fromOsType).append(", ");
        sb.append("toUri:").append(this.toUri).append(", ");
        sb.append("toOsType:").append(this.toOsType).append(", ");
        sb.append("content:").append(this.content).append("}");
        return sb.toString();
    }
}
