package com.nettyim.server.model;

/** 
 * 协议包
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
public class ProtocolModel {

    /** 协议包长度 */
    public static final short PROTOCOL_HEADER_LENGTH = Integer.SIZE/8 + Short.SIZE/8 + Integer.SIZE/8 + Integer.SIZE/8 + Long.SIZE/8;
    
    public static final short CUR_VERSION = 1;
    
    // 协议包长
    private int packetLen;
    // 协议版本
    private short version;
    // 操作类型
    private int commandId;
    // 序列号
    private int seqId;
    // 设备编号
    private long shellId;
    // 协议包体
    private byte[] body;
    
    public int getPacketLen() {
		return packetLen;
	}

	public void setPacketLen(int packetLen) {
		this.packetLen = packetLen;
	}

	public short getVersion() {
		return version;
	}

	public void setVersion(short version) {
		this.version = version;
	}

	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public long getShellId() {
		return shellId;
	}

	public void setShellId(long shellId) {
		this.shellId = shellId;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	@Override
    public String toString() {
        String text = body == null ? "null" : new String(body);

        StringBuffer sb = new StringBuffer();
        sb.append("Protocol{packetLen:").append(this.packetLen).append(", ");
        sb.append("version:").append(this.version).append(", ");
        sb.append("commandId:").append(this.commandId).append(", ");
        sb.append("seqId:").append(this.seqId).append(", ");
        sb.append("shellId:").append(this.shellId).append(", ");
        sb.append("body:").append(text).append("}");
        return sb.toString();
    }
}