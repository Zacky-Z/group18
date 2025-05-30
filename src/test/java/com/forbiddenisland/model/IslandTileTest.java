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
    void testSinkTile() {
        tile.flood();
        tile.flood(); // 再次淹没导致沉没
        assertTrue(tile.isSunk());
        assertTrue(tile.isFlooded()); // 沉没的板块仍被视为淹没

        // 测试沉没后无法治水
        assertFalse(tile.shoreUp());
        assertTrue(tile.isSunk());
    }

    @Test
    void testAssociatedTreasure() {
        tile.setAssociatedTreasure(TreasureType.THE_CRYSTAL_OF_FIRE);
        assertEquals(TreasureType.THE_CRYSTAL_OF_FIRE, tile.getAssociatedTreasure());
    }
}