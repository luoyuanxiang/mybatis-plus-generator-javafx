package com.example.generator.ui.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成执行结果，供 UI 展示成功/失败消息及文件列表。
 *
 * @param success        是否生成成功。
 * @param message        结果摘要消息，成功或失败原因。
 * @param generatedFiles 已生成/处理的文件或表记录列表。
 * @param skippedFiles   因未开启覆盖而跳过的已存在文件列表。
 */
public record GenerationResult(boolean success, String message, List<String> generatedFiles,
                               List<String> skippedFiles) {

    /**
     * 构造生成结果。
     *
     * @param success        是否成功
     * @param message        摘要消息
     * @param generatedFiles 生成记录，可为 null
     * @param skippedFiles   跳过记录，可为 null
     */
    public GenerationResult(boolean success, String message, List<String> generatedFiles, List<String> skippedFiles) {
        this.success = success;
        this.message = message;
        this.generatedFiles = generatedFiles == null ? List.of() : new ArrayList<>(generatedFiles);
        this.skippedFiles = skippedFiles == null ? List.of() : new ArrayList<>(skippedFiles);
    }

    /**
     * 构建成功结果。
     *
     * @param files   生成记录
     * @param skipped 跳过记录
     * @return 成功结果实例
     */
    public static GenerationResult success(List<String> files, List<String> skipped) {
        String message = skipped == null || skipped.isEmpty()
                ? "Code generation completed."
                : "Code generation completed with " + skipped.size()
                  + " skipped file(s). Enable file override in strategy settings to overwrite.";
        return new GenerationResult(true, message, files, skipped);
    }

    /**
     * 构建失败结果。
     *
     * @param message 失败原因
     * @return 失败结果实例
     */
    public static GenerationResult failure(String message) {
        return new GenerationResult(false, message, List.of(), List.of());
    }

    /**
     * 是否存在被跳过的文件。
     */
    public boolean hasSkippedFiles() {
        return !skippedFiles.isEmpty();
    }
}
