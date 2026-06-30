package com.example.generator.ui.dto;

public class ColumnMeta {

    private final String name;
    private final String type;
    private final int size;
    private final boolean nullable;
    private final boolean primaryKey;
    private final String comment;
    private final String defaultValue;

    public ColumnMeta(String name, String type, int size, boolean nullable, boolean primaryKey,
                      String comment, String defaultValue) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.nullable = nullable;
        this.primaryKey = primaryKey;
        this.comment = comment;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public boolean isNullable() {
        return nullable;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public String getComment() {
        return comment;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
