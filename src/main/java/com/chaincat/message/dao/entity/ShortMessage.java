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
     * 消息名称
     */
    private String messageName;

    /**
     * 模板Code
     */
    private String templateCode;

    /**
     * 模板内容
     */
    private String templateContent;
}
