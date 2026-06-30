package com.example.generator.ui.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * 唯一标识生成工具。
 */
@UtilityClass
public class IdUtil {

    /**
     * 生成新的 UUID 字符串，用作数据源/配置方案的主键。
     *
     * @return 标准 UUID 字符串（含连字符）
     */
    public static String newId() {
        return UUID.randomUUID().toString();
    }
}
