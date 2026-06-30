package com.example.generator.ui.dto;

import java.util.ArrayList;
import java.util.List;

public class GenerationResult {

    private final boolean success;
    private final String message;
    private final List<String> generatedFiles;
    private final List<String> skippedFiles;

    public GenerationResult(boolean success, String message, List<String> generatedFiles, List<String> skippedFiles) {
        this.success = success;
        this.message = message;
        this.generatedFiles = generatedFiles == null ? List.of() : new ArrayList<>(generatedFiles);
        this.skippedFiles = skippedFiles == null ? List.of() : new ArrayList<>(skippedFiles);
    }

    public static GenerationResult success(List<String> files, List<String> skipped) {
        String message = skipped == null || skipped.isEmpty()
                ? "Code generation completed."
                : "Code generation completed with " + skipped.size()
                + " skipped file(s). Enable file override in strategy settings to overwrite.";
        return new GenerationResult(true, message, files, skipped);
    }

    public static GenerationResult failure(String message) {
        return new GenerationResult(false, message, List.of(), List.of());
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getGeneratedFiles() {
        return generatedFiles;
    }

    public List<String> getSkippedFiles() {
        return skippedFiles;
    }

    public boolean hasSkippedFiles() {
        return !skippedFiles.isEmpty();
    }
}
