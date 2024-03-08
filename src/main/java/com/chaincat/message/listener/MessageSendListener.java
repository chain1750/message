package com.chaincat.message.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.chaincat.message.model.MessageSendReq;
import com.chaincat.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 消息发送监听器
 *
 * @author chenhaizhuang
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "messageSend", consumerGroup = "${environment}")
@RequiredArgsConstructor
public class MessageSendListener implements RocketMQListener<String> {

    private final MessageService messageService;

    @Override
    public void onMessage(String s) {
        try {
            MessageSendReq req = JSON.parseObject(s, MessageSendReq.class);
            Assert.isTrue(StrUtil.isNotEmpty(req.getMessageCode()), "消息Code不能为空");
            Assert.isTrue(StrUtil.isNotEmpty(req.getReceiver()), "接受人不能为空");
            if (CollUtil.isEmpty(req.getParams())) {
                req.setParams(new ArrayList<>());
            }
            messageService.send(req);
        } catch (Exception e) {
            log.error("消费失败：{}", e.getMessage(), e);
        }
    }
}
