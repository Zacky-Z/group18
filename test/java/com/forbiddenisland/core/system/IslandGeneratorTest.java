package com.forbiddenisland.test.core.system;

import com.forbiddenisland.core.model.IslandTile;
import com.forbiddenisland.core.system.IslandGenerator;
import com.forbiddenisland.enums.TileName;
import com.forbiddenisland.enums.TreasureType;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class IslandGeneratorTest {
    
    @Test
    public void testGenerateStandardMap() {
        IslandGenerator generator = new IslandGenerator();
        List<IslandTile> tiles = generator.generateStandardMap();
        
        // 验证地图大小
        assertEquals(16, tiles.size(), "标准地图应有16个瓷砖");
        
        // 验证每个瓷砖的唯一性
        long uniqueCount = tiles.stream()
                               .map(IslandTile::getName)
                               .distinct()
                               .count();
        assertEquals(16, uniqueCount, "所有瓷砖名称应唯一");
        
        // 验证宝物瓷砖存在
        for (TreasureType treasure : TreasureType.values()) {
            TileName associatedTile = treasure.getAssociatedTile();
            long count = tiles.stream()
                             .filter(t -> t.getName() == associatedTile)
                             .count();
            assertEquals(1, count, "每个宝物关联的瓷砖应存在且唯一");
        }
        
        // 验证直升机平台存在
        long helipadCount = tiles.stream()
                                .filter(t -> t.hasHelipad())
                                .count();
        assertEquals(1, helipadCount, "应有一个直升机平台");
    }
    
    @Test
    public void testTilePositions() {
        IslandGenerator generator = new IslandGenerator();
        List<IslandTile> tiles = generator.generateStandardMap();
        
        // 验证瓷砖位置在有效范围内
        for (IslandTile tile : tiles) {
            assertTrue(tile.getX() >= 1 && tile.getX() <= 4, "X坐标应在1-4之间");
            assertTrue(tile.getY() >= 1 && tile.getY() <= 4, "Y坐标应在1-4之间");
        }
    }
}    