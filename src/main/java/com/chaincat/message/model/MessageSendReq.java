package com.chaincat.message.model;

import lombok.Data;

import java.util.List;

/**
 * 发送请求
 *
 * @author chenhaizhuang
 */
@Data
public class MessageSendReq {

    /**
     * 消息名称
     */
    private String messageName;

    /**
     * 接受人
     */
    private String receiver;

    /**
     * 参数
     */
    private List<String> params;

    /**
     * 消息ID
     */
    private Long messageId;
}
