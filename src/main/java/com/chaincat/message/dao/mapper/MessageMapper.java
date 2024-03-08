package com.chaincat.message.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaincat.message.dao.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息Mapper
 *
 * @author chenhaizhuang
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
