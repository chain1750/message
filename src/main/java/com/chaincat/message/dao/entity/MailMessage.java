package com.chaincat.message.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 邮件消息
 *
 * @author chenhaizhuang
 */
@Data
public class MailMessage {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息名称
     */
    private String messageName;

    /**
     * 模板标题
     */
    private String templateTitle;

    /**
     * 模板文本
     */
    private String templateText;
}
