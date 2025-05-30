package com.forbiddenisland.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class GameBoardTest {
    private Game game;

    @BeforeEach
    void setUp() {
        // 创建一个包含2个玩家的游戏实例
        game = new Game(List.of("玩家1", "玩家2"), 1);
    }

    @Test
    void testBoardInitialization() {
        // 验证游戏板初始化
        assertNotNull(game.getGameBoard());
        assertEquals(6, game.getGameBoard().length); // 假设游戏板是6x6

        // 验证初始岛屿板块数量
        int activeTiles = 0;
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                if (game.getGameBoard()[r][c] != null) {
                    activeTiles++;
                }
            }
        }
        assertEquals(24, activeTiles); // 标准游戏有24个岛屿板块
    }

    @Test
    void testStartingPositions() {
        // 验证玩家起始位置
        List<Player> players = game.getPlayers();
        assertEquals(2, players.size());

        for (Player player : players) {
            assertNotNull(player.getPawn().getCurrentLocation());
            assertTrue(player.getPawn().getCurrentLocation().isStartingTileForPlayer());
        }
    }

    @Test
    void testTreasureTiles() {
        // 验证宝藏板块的分布
        int treasureTiles = 0;
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                IslandTile tile = game.getGameBoard()[r][c];
                if (tile != null && tile.getAssociatedTreasure() != null) {
                    treasureTiles++;
                }
            }
        }
        assertEquals(8, treasureTiles); // 游戏中有4个宝藏
    }
}