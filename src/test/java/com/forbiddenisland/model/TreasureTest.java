package com.forbiddenisland.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TreasureTest {
    @Test
    void testTreasureTypes() {
        // 验证宝藏类型
        assertEquals(4, TreasureType.values().length);

        // 验证宝藏类型的属性
        assertEquals("火之结晶", TreasureType.THE_CRYSTAL_OF_FIRE.getDisplayName());
        assertEquals("风之雕像", TreasureType.THE_STATUE_OF_THE_WIND.getDisplayName());
        assertEquals("大地之石", TreasureType.THE_EARTH_STONE.getDisplayName());
        assertEquals("海洋圣杯", TreasureType.THE_OCEANS_CHALICE.getDisplayName());
    }
}