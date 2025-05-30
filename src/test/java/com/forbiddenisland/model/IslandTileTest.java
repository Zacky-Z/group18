package com.forbiddenisland.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IslandTileTest {
    private IslandTile tile;

    @BeforeEach
    void setUp() {
        tile = new IslandTile("测试岛");
    }

    @Test
    void testInitialState() {
        assertEquals("测试岛", tile.getName());
        assertFalse(tile.isFlooded());
        assertFalse(tile.isSunk());
        assertNull(tile.getAssociatedTreasure());
    }

    @Test
    void testFloodAndShoreUp() {
        // 测试淹没逻辑
        tile.flood();
        assertTrue(tile.isFlooded());
        assertFalse(tile.isSunk());

        // 测试治水逻辑
        assertTrue(tile.shoreUp());
        assertFalse(tile.isFlooded());
        assertFalse(tile.isSunk());

        // 测试重复治水无效
        assertFalse(tile.shoreUp());
    }


    @Test
    void testNormalFloodAndShoreUp() {
        assertFalse(tile.isFlooded());     // 初始状态未淹没

        tile.flood();                      // 第一次淹没
        assertTrue(tile.isFlooded());      // 验证已淹没
        assertFalse(tile.isSunk());        // 验证未沉没

        assertTrue(tile.shoreUp());        // 治水成功
        assertFalse(tile.isFlooded());     // 验证已恢复
        assertFalse(tile.isSunk());        // 验证未沉没

        assertFalse(tile.shoreUp());       // 未淹没的板块不需要治水
    }

    @Test
    void testAssociatedTreasure() {
        tile.setAssociatedTreasure(TreasureType.THE_CRYSTAL_OF_FIRE);
        assertEquals(TreasureType.THE_CRYSTAL_OF_FIRE, tile.getAssociatedTreasure());
    }
}