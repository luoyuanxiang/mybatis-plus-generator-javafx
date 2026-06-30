package com.example.generator.ui.util;

import com.example.generator.ui.dto.StrategyConfigDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class StringListUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private StringListUtil() {
    }

    public static ObjectMapper mapper() {
        return MAPPER;
    }

    public static List<String> splitCsv(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public static String joinCsv(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        return String.join(",", values);
    }

    public static void applyCsvToList(String csv, List<String> target) {
        target.clear();
        target.addAll(splitCsv(csv));
    }
}
