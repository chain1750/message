package com.chaincat.message.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Pair;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chaincat.message.dao.entity.MailMessage;
import com.chaincat.message.dao.mapper.MailMessageMapper;
import com.chaincat.message.model.MessageSendReq;
import com.chaincat.message.service.MessageService;
import com.chaincat.message.utils.StrReplaceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * 邮件消息实现
 *
 * @author chenhaizhuang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailMessageServiceImpl implements MessageService {

    private final MailMessageMapper mailMessageMapper;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Override
    public void send(MessageSendReq req) throws Exception {
        String email = req.getReceiver();
        // 邮件消息
        MailMessage mailMessage = mailMessageMapper.selectOne(Wrappers.<MailMessage>lambdaQuery()
                .eq(MailMessage::getMessageName, req.getMessageName()));
        Assert.notNull(mailMessage, "邮件消息不存在");
        // 构建系统消息参数
        List<String> params = req.getParams();
        int index = 0;
        Pair<Integer, String> titlePair = StrReplaceUtils.replace(mailMessage.getTemplateTitle(), params, index);
        Pair<Integer, String> textPair = StrReplaceUtils.replace(mailMessage.getTemplateText(), params, titlePair.getKey());
        log.info("发送邮件消息给{}, 消息内容：\n{}\n{}", email,
                titlePair.getValue(),
                textPair.getValue());
        // 发送
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(email);
        helper.setSubject(titlePair.getValue());
        helper.setText(textPair.getValue(), true);
        for (int i = textPair.getKey(); i < params.size() - 1; i += 2) {
            String fileName = params.get(i);
            String url = params.get(i + 1);
            byte[] bytes = HttpUtil.downloadBytes(url);
            helper.addAttachment(fileName, new ByteArrayResource(bytes));
        }
        mailSender.send(message);
        log.info("邮件消息发送结果：{}", message.getMessageID());
    }
}
