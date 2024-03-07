package com.chaincat.message.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 系统通知消息
 *
 * @author chenhaizhuang
 */
@Data
public class SystemNotifyMessage {

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
     * 模板标题
     */
    private String templateTitle;

    /**
     * 模板第一信息
     */
    private String templateFirst;

    /**
     * 模板键值内容
     */
    private String templateKeyValue;

    /**
     * 模板备注
     */
    private String templateRemark;

    /**
     * 模板跳转地址
     */
    private String templateUrl;
}
