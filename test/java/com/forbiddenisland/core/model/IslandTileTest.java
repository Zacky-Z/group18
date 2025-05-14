package com.forbiddenisland.test.core.model;

import com.forbiddenisland.core.model.IslandTile;
import com.forbiddenisland.enums.TileName;
import com.forbiddenisland.enums.TileState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IslandTileTest {
    
    @Test
    public void testInitialState() {
        IslandTile tile = new IslandTile(TileName.CRYSTAL_CAVE, 2, 2);
        assertEquals(TileState.NORMAL, tile.getState());
        assertFalse(tile.isSunk());
        assertFalse(tile.isFlooded());
    }
    
    @Test
    public void testFlooding() {
        IslandTile tile = new IslandTile(TileName.CRYSTAL_CAVE, 2, 2);
        
        // 第一次洪水
        tile.flood();
        assertEquals(TileState.FLOODED, tile.getState());
        assertFalse(tile.isSunk());
        assertTrue(tile.isFlooded());
        
        // 第二次洪水
        tile.flood();
        assertEquals(TileState.SUNK, tile.getState());
        assertTrue(tile.isSunk());
        assertFalse(tile.isFlooded());
    }
    
    @Test
    public void testShoreUp() {
        IslandTile tile = new IslandTile(TileName.CRYSTAL_CAVE, 2, 2);
        
        // 正常状态下无法加固
        assertFalse(tile.shoreUp());
        assertEquals(TileState.NORMAL, tile.getState());
        
        // 洪水状态下可以加固
        tile.flood();
        assertTrue(tile.shoreUp());
        assertEquals(TileState.NORMAL, tile.getState());
        
        // 沉没状态下无法加固
        tile.flood();
        tile.flood();
        assertFalse(tile.shoreUp());
        assertEquals(TileState.SUNK, tile.getState());
    }
}    