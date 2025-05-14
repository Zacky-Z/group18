package com.forbiddenisland.enums;

public enum SpecialCardType {
    HELICOPTER_LIFT("直升机救援", "允许从任意位置飞到任意位置"),
    SANDBAGS("沙袋", "加固任意位置的瓷砖"),
    WATERS_RISE("水位上升", "增加水位并重新洗牌洪水牌堆");

    private final String displayName;
    private final String description;

    SpecialCardType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}    