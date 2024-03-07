package com.chaincat.message.service;

/**
 * 消息Service
 *
 * @author chenhaizhuang
 */
public interface MessageService {

    /**
     * 发送
     *
     * @param param 参数
     */
    void send(String param) throws Exception;
}
