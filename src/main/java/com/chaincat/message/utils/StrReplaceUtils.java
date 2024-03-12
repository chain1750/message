package com.chaincat.message.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;

import java.util.List;

/**
 * 字符串替换工具
 *
 * @author chenhaizhuang
 */
public class StrReplaceUtils {

    /**
     * 替换参数
     *
     * @param content 内容
     * @param params  参数
     * @param index   参数索引
     * @return Pair
     */
    public static Pair<Integer, String> replace(String content, List<String> params, int index) {
        if (StrUtil.isEmpty(content)) {
            return Pair.of(index, "");
        }
        while (content.contains("{}")) {
            Assert.isTrue(index < params.size(), "参数不匹配");
            content = content.replaceFirst("\\{}", params.get(index++));
        }
        return Pair.of(index, content);
    }
}
