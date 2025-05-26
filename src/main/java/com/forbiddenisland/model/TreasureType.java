package com.forbiddenisland.model;

/**
 * Enum representing the different types of treasures in the game.
 * 代表游戏中不同类型宝藏的枚举。
 */
public enum TreasureType {
    THE_EARTH_STONE("大地之石 (The Earth Stone)"),    // 大地之石
    THE_STATUE_OF_THE_WIND("风之雕像 (The Statue of the Wind)"), // 风之雕像
    THE_CRYSTAL_OF_FIRE("烈火水晶 (The Crystal of Fire)"),  // 烈火圣杯 (Crystal not Chalice)
    THE_OCEANS_CHALICE("海洋圣杯 (The Ocean's Chalice)");    // 海洋圣物 (Chalice not Relic)

    private final String displayName;

    TreasureType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // toString() could also be overridden if the enum constant name itself is not desired for general use.
    // @Override
    // public String toString() {
    //     return displayName;
    // }
} 