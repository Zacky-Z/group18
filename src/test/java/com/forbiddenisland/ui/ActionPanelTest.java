package com.forbiddenisland.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import com.forbiddenisland.model.AdventurerRole;
import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.IslandTile;
import com.forbiddenisland.model.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 测试ActionPanel类的功能
 * 使用简单的测试方法验证移动功能的逻辑
 */
public class ActionPanelTest {

    /**
     * 测试移动功能的基本逻辑
     * 由于ActionPanel依赖于JavaFX组件，这里只测试核心逻辑
     */
    @Test
    public void testMovementLogic() {
        // 创建测试用的板块
        IslandTile startTile = new IslandTile("起始板块");
        IslandTile destinationTile = new IslandTile("目标板块");
        
        // 创建玩家
        Player player = new Player("测试玩家");
        player.assignRoleAndPawn(AdventurerRole.EXPLORER, startTile, "GREEN");
        
        // 验证玩家初始位置
        assertEquals(startTile, player.getCurrentLocation());
        
        // 模拟移动
        player.moveTo(destinationTile);
        
        // 验证玩家移动后的位置
        assertEquals(destinationTile, player.getCurrentLocation());
    }

    /**
     * 测试探险家的斜向移动能力
     */
    @Test
    public void testExplorerDiagonalMovement() {
        // 创建一个简单的游戏实例
        Game game = new Game(Arrays.asList("测试玩家"), 1, 
                             Arrays.asList(AdventurerRole.EXPLORER));
        
        // 获取玩家
        Player player = game.getCurrentPlayer();
        
        // 验证玩家是探险家
        assertEquals(AdventurerRole.EXPLORER, player.getRole());
        
        // 验证探险家可以斜向移动
        assertTrue(player.getRole().canMoveDiagonally());
    }
    
    /**
     * 测试飞行员的飞行能力
     */
    @Test
    public void testPilotFlightAbility() {
        // 创建一个简单的游戏实例
        Game game = new Game(Arrays.asList("测试玩家"), 1, 
                             Arrays.asList(AdventurerRole.PILOT));
        
        // 获取玩家
        Player player = game.getCurrentPlayer();
        
        // 验证玩家是飞行员
        assertEquals(AdventurerRole.PILOT, player.getRole());
        
        // 验证飞行员可以飞到任何板块
        assertTrue(player.getRole().canFlyToAnyTile());
        
        // 验证飞行员能力初始未使用
        assertFalse(player.isPilotAbilityUsedThisTurn());
        
        // 模拟使用飞行能力
        player.setPilotAbilityUsedThisTurn(true);
        assertTrue(player.isPilotAbilityUsedThisTurn());
        
        // 模拟回合结束，重置能力
        player.resetTurnBasedAbilities();
        assertFalse(player.isPilotAbilityUsedThisTurn());
    }
    
    /**
     * 测试行动点消耗机制
     */
    @Test
    public void testActionPointConsumption() {
        // 创建一个简单的游戏实例
        Game game = new Game(Arrays.asList("测试玩家"), 1);
        
        // 获取初始行动点
        int initialActions = game.getActionsRemainingInTurn();
        assertEquals(Game.MAX_ACTIONS_PER_TURN, initialActions);
        
        // 消耗一个行动点
        assertTrue(game.spendAction());
        assertEquals(initialActions - 1, game.getActionsRemainingInTurn());
        
        // 消耗所有行动点
        while (game.getActionsRemainingInTurn() > 0) {
            assertTrue(game.spendAction());
        }
        
        // 验证无法再消耗行动点
        assertFalse(game.spendAction());
    }
    
    /**
     * 测试游戏阶段切换
     */
    @Test
    public void testGamePhaseChanges() {
        // 创建一个简单的游戏实例
        Game game = new Game(Arrays.asList("测试玩家"), 1);
        
        // 初始应该是行动阶段
        assertEquals(Game.GamePhase.ACTION_PHASE, game.getCurrentPhase());
        
        // 切换到抽宝藏牌阶段
        game.setCurrentPhase(Game.GamePhase.DRAW_TREASURE_CARDS_PHASE);
        assertEquals(Game.GamePhase.DRAW_TREASURE_CARDS_PHASE, game.getCurrentPhase());
        
        // 切换到抽洪水牌阶段
        game.setCurrentPhase(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE);
        assertEquals(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE, game.getCurrentPhase());
    }
} 