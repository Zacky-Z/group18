package com.forbiddenisland.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WaterMeterTest {
    private WaterMeter waterMeter;

    @BeforeEach
    void setUp() {
        // 初始化水位计 (起始水位1, 最大水位5)
        waterMeter = new WaterMeter(1, 5, new int[]{2, 2, 3, 3, 4});
    }

    @Test
    void testInitialState() {
        assertEquals(1, waterMeter.getCurrentWaterLevel());
        assertFalse(waterMeter.hasReachedMaxLevel());
        assertEquals(2, waterMeter.getNumberOfFloodCardsToDraw());
    }

    @Test
    void testIncreaseWaterLevel() {
        // 测试水位提升
        waterMeter.increaseWaterLevel();
        assertEquals(2, waterMeter.getCurrentWaterLevel());
        assertEquals(2, waterMeter.getNumberOfFloodCardsToDraw());

        // 测试多次提升
        waterMeter.increaseWaterLevel();
        waterMeter.increaseWaterLevel();
        assertEquals(4, waterMeter.getCurrentWaterLevel());
        assertEquals(3, waterMeter.getNumberOfFloodCardsToDraw());

        // 测试达到最大水位
        waterMeter.increaseWaterLevel();
        waterMeter.increaseWaterLevel();
        assertEquals(5, waterMeter.getCurrentWaterLevel());
        assertTrue(waterMeter.hasReachedMaxLevel());
        assertEquals(4, waterMeter.getNumberOfFloodCardsToDraw());
    }

    @Test
    void testWaterLevelLabels() {
        // 验证水位标签
        assertEquals("Novice (新手)", waterMeter.getWaterLevelLabel());
        waterMeter.increaseWaterLevel();
        assertEquals("Normal (普通)", waterMeter.getWaterLevelLabel());
        waterMeter.increaseWaterLevel();
        assertEquals("Elite (精英)", waterMeter.getWaterLevelLabel());
        waterMeter.increaseWaterLevel();
        assertEquals("Legendary (传奇)", waterMeter.getWaterLevelLabel());
        waterMeter.increaseWaterLevel();
        assertEquals("Skull & Crossbones (GAME OVER / 游戏结束)", waterMeter.getWaterLevelLabel());
    }
}