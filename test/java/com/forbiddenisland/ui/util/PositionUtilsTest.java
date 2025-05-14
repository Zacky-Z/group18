package com.forbiddenisland.test.ui.util;

import com.forbiddenisland.core.model.IslandTile;
import com.forbiddenisland.ui.util.PositionUtils;
import com.forbiddenisland.enums.TileName;
import com.forbiddenisland.enums.TileState;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PositionUtilsTest {
    
    @Test
    public void testGetAdjacentTiles() {
        // 创建测试瓷砖
        IslandTile center = new IslandTile(TileName.CRYSTAL_CAVE, 2, 2);
        IslandTile up = new IslandTile(TileName.OBSERVATORY, 2, 1);
        IslandTile down = new IslandTile(TileName.LOST_LAGOON, 2, 3);
        IslandTile left = new IslandTile(TileName.MISTY_MARSH, 1, 2);
        IslandTile right = new IslandTile(TileName.SHADOW_WOODS, 3, 2);
        
        List<IslandTile> allTiles = Arrays.asList(center, up, down, left, right);
        
        // 获取相邻瓷砖
        List<IslandTile> adjacent = PositionUtils.getAdjacentTiles(center, allTiles);
        
        // 验证结果
        assertEquals(4, adjacent.size(), "中心瓷砖应有4个相邻瓷砖");
        assertTrue(adjacent.contains(up), "相邻瓷砖应包含上方瓷砖");
        assertTrue(adjacent.contains(down), "相邻瓷砖应包含下方瓷砖");
        assertTrue(adjacent.contains(left), "相邻瓷砖应包含左方瓷砖");
        assertTrue(adjacent.contains(right), "相邻瓷砖应包含右方瓷砖");
    }
    
    @Test
    public void testAdjacentWithSunkTile() {
        // 创建测试瓷砖，其中一个沉没
        IslandTile center = new IslandTile(TileName.CRYSTAL_CAVE, 2, 2);
        IslandTile up = new IslandTile(TileName.OBSERVATORY, 2, 1);
        IslandTile down = new IslandTile(TileName.LOST_LAGOON, 2, 3);
        IslandTile left = new IslandTile(TileName.MISTY_MARSH, 1, 2);
        IslandTile right = new IslandTile(TileName.SHADOW_WOODS, 3, 2);
        
        // 沉没左方瓷砖
        left.setState(TileState.SUNK);
        
        List<IslandTile> allTiles = Arrays.asList(center, up, down, left, right);
        
        // 获取相邻瓷砖
        List<IslandTile> adjacent = PositionUtils.getAdjacentTiles(center, allTiles);
        
        // 验证结果
        assertEquals(3, adjacent.size(), "中心瓷砖应有3个相邻瓷砖（左方瓷砖沉没）");
        assertTrue(adjacent.contains(up), "相邻瓷砖应包含上方瓷砖");
        assertTrue(adjacent.contains(down), "相邻瓷砖应包含下方瓷砖");
        assertTrue(adjacent.contains(right), "相邻瓷砖应包含右方瓷砖");
        assertFalse(adjacent.contains(left), "相邻瓷砖不应包含沉没的左方瓷砖");
    }
    
    @Test
    public void testManhattanDistance() {
        IslandTile tile1 = new IslandTile(TileName.CRYSTAL_CAVE, 1, 1);
        IslandTile tile2 = new IslandTile(TileName.CORAL_PALACE, 3, 3);
        
        // 计算曼哈顿距离
        int distance = PositionUtils.manhattanDistance(tile1, tile2);
        
        // 验证结果
        assertEquals(4, distance, "曼哈顿距离计算错误");
    }
}    