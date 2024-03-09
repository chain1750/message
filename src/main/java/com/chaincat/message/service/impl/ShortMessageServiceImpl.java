package com.chaincat.message.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.DesensitizedUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chaincat.message.dao.entity.ShortMessage;
import com.chaincat.message.dao.mapper.ShortMessageMapper;
import com.chaincat.message.model.MessageSendReq;
import com.chaincat.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 短信实现
 *
 * @author chenhaizhuang
 */
@Slf4j
@Service("shortMessage")
@RequiredArgsConstructor
public class ShortMessageServiceImpl implements MessageService {

    private final ShortMessageMapper shortMessageMapper;

    @Value("${aliyun.sms.access-key-id:}")
    private String accessKeyId;

    @Value("${aliyun.sms.access-key-secret:}")
    private String accessKeySecret;

    @Value("${aliyun.sms.sign-name:}")
    private String signName;

    private Client client;

    @PostConstruct
    public void init() throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint("dysmsapi.aliyuncs.com");
        client = new Client(config);
    }


    @Override
    public void send(MessageSendReq req) throws Exception {
        String phoneNumber = req.getReceiver();
        // 短信
        ShortMessage shortMessage = shortMessageMapper.selectOne(Wrappers.<ShortMessage>lambdaQuery()
                .eq(ShortMessage::getMessageName, req.getMessageName()));
        Assert.notNull(shortMessage, "短信不存在");
        // 构建短信参数和短信内容
        String templateContent = shortMessage.getTemplateContent();
        List<String> paramKeys = getParamKeys(templateContent);
        List<String> paramValues = req.getParams();
        Assert.isTrue(paramKeys.size() == paramValues.size(), "参数不匹配");
        JSONObject paramMap = new JSONObject();
        String messageContent = getMessageContent(templateContent, paramKeys, paramValues, paramMap);
        log.info("发送短信给{}, 短信内容：{}", DesensitizedUtil.mobilePhone(phoneNumber), messageContent);
        // 发送
        SendSmsRequest request = new SendSmsRequest()
                .setPhoneNumbers(phoneNumber)
                .setSignName(signName)
                .setTemplateCode(shortMessage.getTemplateCode());
        if (CollUtil.isNotEmpty(paramMap)) {
            request.setTemplateParam(paramMap.toJSONString());
        }
        SendSmsResponse response = client.sendSms(request);
        log.info("短信发送结果：{}", JSON.toJSONString(response.getBody()));
    }

    /**
     * 获取短信参数Key
     *
     * @param templateContent 模板内容
     * @return List
     */
    private List<String> getParamKeys(String templateContent) {
        List<String> paramKeys = new ArrayList<>();
        int left = -1;
        for (int i = 0; i < templateContent.length(); i++) {
            char c = templateContent.charAt(i);
            if (c == '{') {
                left = i;
            } else if (c == '}' && left != -1) {
                String key = templateContent.substring(left, i + 1);
                paramKeys.add(key);
                left = -1;
            }
        }
        return paramKeys;
    }

    /**
     * 获取短信内容
     *
     * @param templateContent 模板内容
     * @param paramKeys       参数Key
     * @param paramValues     参数Value
     * @param paramMap        参数Map
     * @return String
     */
    private String getMessageContent(String templateContent, List<String> paramKeys, List<String> paramValues,
                                     JSONObject paramMap) {
        for (int i = 0; i < paramKeys.size(); i++) {
            String paramKey = paramKeys.get(i);
            String paramValue = paramValues.get(i);
            paramMap.put(paramKey, paramValue);
            templateContent = templateContent.replace(paramKey, paramValue);
        }
        return templateContent;
    }
}
