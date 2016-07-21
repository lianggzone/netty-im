package com.nettyim.server.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.nettyim.server.model.ProtocolModel;

/**
 * <p>Title: TCP自定义协议Codec  </p>
 * <p>Description: TcpProtocolCodec </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
@Component
@ChannelHandler.Sharable
public class TcpProtocolCodec extends MessageToMessageCodec<ByteBuf, ProtocolModel> {

    private static final Logger logger = LoggerFactory.getLogger(TcpProtocolCodec.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtocolModel protocolModel, List<Object> list) throws Exception {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        if (protocolModel.getBody() != null) {
            byteBuf.writeInt(ProtocolModel.PROTOCOL_HEADER_LENGTH + protocolModel.getBody().length);
            byteBuf.writeShort(ProtocolModel.PROTOCOL_VERSION);
            byteBuf.writeInt(protocolModel.getOperation());
            byteBuf.writeInt(protocolModel.getSeqId());
            byteBuf.writeBytes(protocolModel.getBody());
        } else {
            byteBuf.writeInt(ProtocolModel.PROTOCOL_HEADER_LENGTH);
            byteBuf.writeShort(ProtocolModel.PROTOCOL_VERSION);
            byteBuf.writeInt(protocolModel.getOperation());
            byteBuf.writeInt(protocolModel.getSeqId());
        }
        list.add(byteBuf);

        logger.debug("tcp protocol codec encode: {}", protocolModel);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        ProtocolModel protocolModel = new ProtocolModel();
        protocolModel.setPacketLen(byteBuf.readInt());
        protocolModel.setVersion(byteBuf.readShort());
        protocolModel.setOperation(byteBuf.readInt());
        protocolModel.setSeqId(byteBuf.readInt());
        if (protocolModel.getPacketLen() > ProtocolModel.PROTOCOL_HEADER_LENGTH) {
            byte[] bytes = new byte[protocolModel.getPacketLen() - ProtocolModel.PROTOCOL_HEADER_LENGTH];
            byteBuf.readBytes(bytes);
            protocolModel.setBody(bytes);
        }

        list.add(protocolModel);

        logger.debug("tcp protocol codec decode: {}", protocolModel);
    }
}
