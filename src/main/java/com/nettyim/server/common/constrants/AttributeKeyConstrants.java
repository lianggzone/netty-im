package com.nettyim.server.common.constrants;

import io.netty.util.AttributeKey;

import com.nettyim.server.entity.AuthModel;

/**
 * AttributeKey常量类 
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
public class AttributeKeyConstrants {

    // shell_id
    public static final AttributeKey<AuthModel> KEY_SHELL_ID = AttributeKey.valueOf("shell_id");
}
