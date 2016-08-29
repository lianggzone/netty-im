package com.nettyim.server.server.codec;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nettyim.server.model.ProtocolModel;

/**
 * 自定义协议Codec
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年8月29日  v0.1</p><p>版本内容: 创建</p>
 */
@Component
@ChannelHandler.Sharable
public class WebSocketProtocolCodec extends MessageToMessageCodec<TextWebSocketFrame, ProtocolModel> {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtocolModel protocolModel, List<Object> list) throws Exception {
        ObjectNode root = jsonMapper.createObjectNode();
        
        JsonNode body = jsonMapper.readTree(protocolModel.getBody());
        
        root.put("version", protocolModel.getVersion());
        root.put("commandId", protocolModel.getCommandId());
        root.put("seqId", protocolModel.getSeqId());
        root.put("shellId", protocolModel.getShellId());
        root.set("body", body);       

        list.add(new TextWebSocketFrame(root.toString()));
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame, List<Object> list) throws Exception {
        String text = textWebSocketFrame.text();
        JsonNode root = jsonMapper.readTree(text);
        ProtocolModel protocolModel = new ProtocolModel();
        if (root.has("version")) {
        	protocolModel.setVersion(root.get("version").shortValue());
        }
        if (root.has("commandId")) {
        	protocolModel.setCommandId(root.get("commandId").asInt());
        }
        if (root.has("seqId")) {
        	protocolModel.setSeqId(root.get("seqId").asInt());
        }
        if (root.has("shellId")) {
        	protocolModel.setShellId(root.get("shellId").asLong());
        }
        if (root.has("body")) {
        	protocolModel.setBody(root.get("body").toString().getBytes());
        }
        list.add(protocolModel);
    }
}
