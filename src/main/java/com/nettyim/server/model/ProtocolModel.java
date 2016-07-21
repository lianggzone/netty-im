package com.nettyim.server.model;

/**
 * <p>Title: 协议包体  </p>
 * <p>Description: ProtocolModel </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
public class ProtocolModel {

    // HEADER长度为16
    public static final short PROTOCOL_HEADER_LENGTH = Integer.SIZE/8 + Short.SIZE/8 + Integer.SIZE/8 + Integer.SIZE/8;
    public static final short PROTOCOL_HEADER_LEN = Short.SIZE/8 + Integer.SIZE/8 + Integer.SIZE/8;
    public static final short PROTOCOL_VERSION = 1;
   
    private int packetLen;
    private short version;
    private int operation;
    private int seqId;
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

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
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
        sb.append("operation:").append(this.operation).append(", ");
        sb.append("seqId:").append(this.seqId).append(", ");
        sb.append("body:").append(text).append("}");
        return sb.toString();
    }
}
