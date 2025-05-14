package com.forbiddenisland.core.model;

import com.forbiddenisland.enums.AdventurerType;

public class Adventurer extends Player {
    private final AdventurerType type;

    public Adventurer(String name, AdventurerType type) {
        super(name);
        this.type = type;
    }

    public AdventurerType getType() {
        return type;
    }

    // 特殊能力方法将在子类或通过策略模式实现
}    