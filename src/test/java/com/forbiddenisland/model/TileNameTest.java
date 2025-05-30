package com.forbiddenisland.model;

import com.forbiddenisland.enums.TileName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TileNameTest {
    @Test
    void testTileNames() {
        // 验证岛屿板块名称
        assertEquals(21, TileName.values().length);

        // 验证特定板块的显示名称
        assertEquals("太阳神庙", TileName.TEMPLE_OF_THE_SUN.getDisplayName());
        assertEquals("月亮神庙", TileName.TEMPLE_OF_THE_MOON.getDisplayName());
    }
}