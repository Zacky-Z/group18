package com.forbiddenisland.enums;

public enum AdventurerType {
    PILOT("飞行员"),
    ENGINEER("工程师"),
    MESSENGER("信差"),
    DIVER("潜水员"),
    EXPLORER("探险家"),
    NAVIGATOR("领航员");

    private final String displayName;

    AdventurerType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}    