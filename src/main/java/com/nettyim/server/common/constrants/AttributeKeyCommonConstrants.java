package com.nettyim.server.common.constrants;

import io.netty.util.AttributeKey;

import com.nettyim.server.model.AuthTokenModel;

/**
 * <p>Title: AttributeKey常量类  </p>
 * <p>Description: AttributeKeyCommonConstrants </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
public class AttributeKeyCommonConstrants {

    // client_id
    public static final AttributeKey<AuthTokenModel> KEY_CLIENT_ID = AttributeKey.valueOf("client_id");
}
