package com.forbiddenisland.enums;

public enum TileName {
    CRYSTAL_CAVE("水晶洞穴"),
    CORAL_PALACE("珊瑚宫殿"),
    TEMPLE_OF_THE_SUN("太阳神庙"),
    TEMPLE_OF_THE_MOON("月亮神庙"),
    IRON_ANVIL_ROCK("铁砧岩"),
    CLIFF_OF_AGES("岁月悬崖"),
    OBSERVATORY("天文台"),
    PHANTOM_ROCK("幻影岩"),
    BREAKERS_BRIDGE("碎浪桥"),
    DUNES_OF_DECEPTION("欺骗沙丘"),
    HOWLING_GARDEN("呼啸花园"),
    LOST_LAGOON("失落泻湖"),
    MISTY_MARSH("迷雾沼泽"),
    SHADOW_WOODS("暗影森林"),
    WHISPERING_GARDEN("低语花园"),
    CAVE_OF_EMBERS("余烬洞穴"),
    TWILIGHT_HOLLOW("黄昏山谷"),
    TIDAL_PALACE("潮汐宫殿"),
    WATCHTOWER("瞭望塔"),
    BAY_OF_SILVER("银湾"),
    GOLD_GATE("金门");

    private final String displayName;

    TileName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}    