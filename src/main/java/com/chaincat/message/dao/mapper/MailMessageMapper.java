package com.chaincat.message.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaincat.message.dao.entity.MailMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件消息Mapper
 *
 * @author chenhaizhuang
 */
@Mapper
public interface MailMessageMapper extends BaseMapper<MailMessage> {
}
