package com.chaincat.message.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 短信
 *
 * @author chenhaizhuang
 */
@Data
public class ShortMessage {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息Code
     */
    private String messageCode;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 模板内容
     */
    private String templateContent;
}
