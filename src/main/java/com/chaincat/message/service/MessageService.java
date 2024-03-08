package com.chaincat.message.service;

import com.chaincat.message.model.MessageSendReq;

/**
 * 消息Service
 *
 * @author chenhaizhuang
 */
public interface MessageService {

    /**
     * 发送
     *
     * @param req 请求
     */
    void send(MessageSendReq req) throws Exception;
}
