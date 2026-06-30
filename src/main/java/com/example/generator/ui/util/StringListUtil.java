package com.example.generator.ui.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字符串列表与 JSON 序列化工具。
 * <p>
 * 提供 UI 文本框（逗号分隔）与 {@code List<String>} 之间的转换，
 * 以及全局共享的 Jackson {@link ObjectMapper} 实例。
 * </p>
 */
@UtilityClass
public class StringListUtil {

    /**
     * 全局 ObjectMapper，注册 JSR-310 模块并禁用日期时间戳格式。
     * 用于数据源/配置方案的 JSON 读写。
     */
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /** @return 共享 Jackson ObjectMapper 实例 */
    public static ObjectMapper mapper() {
        return MAPPER;
    }

    /**
     * 将逗号分隔字符串拆分为列表，自动 trim 并过滤空项。
     *
     * @param value 逗号分隔文本，可为 null
     * @return 非 null 列表，空输入返回 {@code List.of()}
     */
    public static List<String> splitCsv(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * 将列表合并为逗号分隔字符串。
     *
     * @param values 字符串列表，可为 null
     * @return 逗号连接结果，空列表返回空字符串
     */
    public static String joinCsv(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        return String.join(",", values);
    }

    /**
     * 用 CSV 解析结果替换目标列表内容（先 clear 再 addAll）。
     *
     * @param csv    逗号分隔文本
     * @param target 待更新的可变列表
     */
    public static void applyCsvToList(String csv, List<String> target) {
        target.clear();
        target.addAll(splitCsv(csv));
    }
}
