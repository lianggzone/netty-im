package com.nettyim.server.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 群聊消息模型
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
public class GroupChatModel {
    
    @JsonProperty("from_shell_id")
    private Long fromShellId;

    @JsonProperty("content")
    private String content;

    public Long getFromShellId() {
		return fromShellId;
	}

	public void setFromShellId(Long fromShellId) {
		this.fromShellId = fromShellId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ChatMessage{fromShellId:").append(this.fromShellId).append(", ");
        sb.append("content:").append(this.content).append("}");
        return sb.toString();
    }
}
