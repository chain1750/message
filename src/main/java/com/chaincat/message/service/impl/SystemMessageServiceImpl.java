package com.chaincat.message.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chaincat.message.dao.entity.SystemMessage;
import com.chaincat.message.dao.mapper.SystemMessageMapper;
import com.chaincat.message.model.MessageSendReq;
import com.chaincat.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统消息实现
 *
 * @author chenhaizhuang
 */
@Slf4j
@Service("systemMessage")
@RequiredArgsConstructor
public class SystemMessageServiceImpl implements MessageService {

    private final SystemMessageMapper systemMessageMapper;

    @Value("${system-message.salt}")
    private String salt;

    @Value("${system-message.send-url}")
    private String sendUrl;

    @Override
    public void send(MessageSendReq req) throws Exception {
        String userId = req.getReceiver();
        // 系统消息
        SystemMessage systemMessage = systemMessageMapper.selectOne(Wrappers.<SystemMessage>lambdaQuery()
                .eq(SystemMessage::getMessageName, req.getMessageName()));
        Assert.notNull(systemMessage, "系统消息不存在");
        // 构建系统消息参数
        List<String> params = req.getParams();
        int index = 0;
        Pair<Integer, String> titlePair = replace(systemMessage.getTemplateTitle(), params, index);
        Pair<Integer, String> firstPair = replace(systemMessage.getTemplateFirst(), params, titlePair.getKey());
        Pair<Integer, String> keyValuePair = replace(systemMessage.getTemplateKeyValue(), params, firstPair.getKey());
        Pair<Integer, String> remarkPair = replace(systemMessage.getTemplateRemark(), params, keyValuePair.getKey());
        Pair<Integer, String> urlPair = replace(systemMessage.getTemplateUrl(), params, remarkPair.getKey());
        log.info("发送系统消息给{}, 消息内容：\n{}\n{}\n{}\n{}\n{}", userId,
                titlePair.getValue(),
                firstPair.getValue(),
                keyValuePair.getValue(),
                remarkPair.getValue(),
                urlPair.getValue());
        // 发送
        JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("title", titlePair.getValue());
        requestJsonObject.put("first", firstPair.getValue());
        requestJsonObject.put("keyValue", keyValuePair.getValue());
        requestJsonObject.put("remark", remarkPair.getValue());
        requestJsonObject.put("url", urlPair.getValue());
        requestJsonObject.put("sign", getSign(requestJsonObject, salt));
        String response = HttpUtil.post(sendUrl, requestJsonObject.toJSONString());
        log.info("系统消息发送结果：{}", response);
    }

    /**
     * 替换参数
     *
     * @param content 内容
     * @param params  参数
     * @param index   参数索引
     * @return Pair
     */
    private Pair<Integer, String> replace(String content, List<String> params, int index) {
        while (content.contains("{}")) {
            Assert.isTrue(index < params.size(), "参数不匹配");
            content = content.replaceFirst("\\{}", params.get(index++));
        }
        return Pair.of(index, content);
    }

    /**
     * 获取签名
     *
     * @param requestMap 请求参数Map
     * @param salt       盐值
     * @return String
     */
    public String getSign(Map<String, Object> requestMap, String salt) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        List<String> paramsArr = new ArrayList<>();
        requestMap.forEach((key, valueObj) -> {
            String value = valueObj.toString().trim();
            if (value.startsWith("\"") && value.endsWith("\"") && value.length() > 1) {
                value = value.substring(1, value.length() - 1).trim();
            }
            if (StrUtil.isNotEmpty(value)) {
                paramsArr.add(value);
            }
        });
        paramsArr.add(salt);
        paramsArr.sort(String::compareTo);

        StringBuilder builder = new StringBuilder();
        String sep = "";
        for (String param : paramsArr) {
            builder.append(sep).append(param);
            sep = "&";
        }

        byte[] byteArray = builder.toString().getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
