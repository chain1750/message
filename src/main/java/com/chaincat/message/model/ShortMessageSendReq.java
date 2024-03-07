package com.chaincat.message.model;

import lombok.Data;

import java.util.List;

/**
 * 短信发送请求
 *
 * @author chenhaizhuang
 */
@Data
public class ShortMessageSendReq {

    /**
     * 消息Code
     */
    private String messageCode;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 参数
     */
    private List<String> params;
}
