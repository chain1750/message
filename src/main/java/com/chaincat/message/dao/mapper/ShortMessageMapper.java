package com.chaincat.message.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaincat.message.dao.entity.ShortMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信Mapper
 *
 * @author chenhaizhuang
 */
@Mapper
public interface ShortMessageMapper extends BaseMapper<ShortMessage> {
}
