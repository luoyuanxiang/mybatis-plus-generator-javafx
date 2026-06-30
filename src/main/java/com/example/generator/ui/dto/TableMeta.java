package com.example.generator.ui.dto;

public class TableMeta {

    private final String name;
    private final String comment;
    private final String type;

    public TableMeta(String name, String comment, String type) {
        this.name = name;
        this.comment = comment;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getType() {
        return type;
    }
}
