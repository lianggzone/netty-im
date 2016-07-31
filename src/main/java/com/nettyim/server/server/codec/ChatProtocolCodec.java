package com.nettyim.server.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nettyim.server.model.ProtocolModel;

/**
 * 自定义协议Codec
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
@Component
@ChannelHandler.Sharable
public class ChatProtocolCodec extends MessageToMessageCodec<ByteBuf, ProtocolModel> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtocolModel protocolModel, List<Object> list) throws Exception {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        if (protocolModel.getBody() != null) {
            byteBuf.writeInt(ProtocolModel.PROTOCOL_HEADER_LENGTH + protocolModel.getBody().length);
            byteBuf.writeShort(protocolModel.getVersion());
            byteBuf.writeInt(protocolModel.getCommandId());
            byteBuf.writeInt(protocolModel.getSeqId());
            byteBuf.writeLong(protocolModel.getShellId());
            byteBuf.writeBytes(protocolModel.getBody());
        } else {
            byteBuf.writeInt(ProtocolModel.PROTOCOL_HEADER_LENGTH);
            byteBuf.writeShort(protocolModel.getVersion());
            byteBuf.writeInt(protocolModel.getCommandId());
            byteBuf.writeInt(protocolModel.getSeqId());
            byteBuf.writeLong(protocolModel.getShellId());
        }
        list.add(byteBuf);

        //logger.debug("protocol codec encode: {}", protocolModel);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        ProtocolModel protocolModel = new ProtocolModel();
        protocolModel.setPacketLen(byteBuf.readInt());
        protocolModel.setVersion(byteBuf.readShort());
        protocolModel.setCommandId(byteBuf.readInt());
        protocolModel.setSeqId(byteBuf.readInt());
        protocolModel.setShellId(byteBuf.readLong());
        if (protocolModel.getPacketLen() > ProtocolModel.PROTOCOL_HEADER_LENGTH) {
            byte[] bytes = new byte[protocolModel.getPacketLen() - ProtocolModel.PROTOCOL_HEADER_LENGTH];
            byteBuf.readBytes(bytes);
            protocolModel.setBody(bytes);
        }

        list.add(protocolModel);

        //logger.debug("protocol codec decode: {}", protocolModel);
    }
}
