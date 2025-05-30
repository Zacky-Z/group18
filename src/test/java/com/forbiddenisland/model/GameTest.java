package com.forbiddenisland.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * 测试Game类的回合管理和行动点管理系统
 */
public class GameTest {

    private Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        // 创建一个包含两个玩家的游戏
        List<String> playerNames = Arrays.asList("玩家1", "玩家2");
        List<AdventurerRole> roles = Arrays.asList(AdventurerRole.PILOT, AdventurerRole.DIVER);
        game = new Game(playerNames, 1, roles);
        
        player1 = game.getPlayers().get(0);
        player2 = game.getPlayers().get(1);
    }

    @Test
    public void testInitialGameState() {
        // 测试游戏初始化状态
        assertEquals(player1, game.getCurrentPlayer());
        assertEquals(Game.MAX_ACTIONS_PER_TURN, game.getActionsRemainingInTurn());
        assertEquals(Game.GamePhase.ACTION_PHASE, game.getCurrentPhase());
    }

    @Test
    public void testSpendAction() {
        // 测试消耗行动点
        int initialActions = game.getActionsRemainingInTurn();
        
        // 第一次消耗行动点应该成功
        assertTrue(game.spendAction());
        assertEquals(initialActions - 1, game.getActionsRemainingInTurn());
        
        // 消耗所有行动点
        for (int i = 0; i < initialActions - 1; i++) {
            assertTrue(game.spendAction());
        }
        
        // 尝试消耗超过上限的行动点应该失败
        assertEquals(0, game.getActionsRemainingInTurn());
        assertFalse(game.spendAction());
    }

    @Test
    public void testAddActions() {
        // 测试添加行动点
        int initialActions = game.getActionsRemainingInTurn();
        
        // 添加两个行动点
        game.addActions(2);
        assertEquals(initialActions + 2, game.getActionsRemainingInTurn());
        
        // 添加负数行动点应该不生效
        game.addActions(-1);
        assertEquals(initialActions + 2, game.getActionsRemainingInTurn());
    }

    @Test
    public void testResetActions() {
        // 消耗一些行动点
        game.spendAction();
        game.spendAction();
        
        // 重置行动点
        game.resetActions();
        assertEquals(Game.MAX_ACTIONS_PER_TURN, game.getActionsRemainingInTurn());
    }

    @Test
    public void testNextTurn() {
        // 消耗一些行动点
        game.spendAction();
        
        // 获取当前阶段
        Game.GamePhase initialPhase = game.getCurrentPhase();
        
        // 切换到下一个玩家的回合
        game.nextTurn();
        
        // 验证下一个玩家是否为当前玩家
        assertEquals(player2, game.getCurrentPlayer());
        
        // 验证行动点是否重置
        assertEquals(Game.MAX_ACTIONS_PER_TURN, game.getActionsRemainingInTurn());
        
        // 验证阶段是否重置为行动阶段
        assertEquals(Game.GamePhase.ACTION_PHASE, game.getCurrentPhase());
    }

    @Test
    public void testPilotAbilityResetOnTurnChange() {
        // 设置飞行员已使用飞行能力
        player1.setPilotAbilityUsedThisTurn(true);
        assertTrue(player1.isPilotAbilityUsedThisTurn());
        
        // 切换回合
        game.nextTurn();
        
        // 回到玩家1的回合（循环两次）
        game.nextTurn();
        
        // 验证飞行员能力是否重置
        assertFalse(player1.isPilotAbilityUsedThisTurn());
    }

    @Test
    public void testPhaseChanges() {
        // 初始阶段应该是行动阶段
        assertEquals(Game.GamePhase.ACTION_PHASE, game.getCurrentPhase());
        
        // 切换到抽宝藏牌阶段
        game.setCurrentPhase(Game.GamePhase.DRAW_TREASURE_CARDS_PHASE);
        assertEquals(Game.GamePhase.DRAW_TREASURE_CARDS_PHASE, game.getCurrentPhase());
        
        // 切换到抽洪水牌阶段
        game.setCurrentPhase(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE);
        assertEquals(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE, game.getCurrentPhase());
        
        // 下一个回合应该重置为行动阶段
        game.nextTurn();
        assertEquals(Game.GamePhase.ACTION_PHASE, game.getCurrentPhase());
    }
    
    @Test
    public void testCompleteRoundRotation() {
        // 测试完整的回合轮转
        assertEquals(player1, game.getCurrentPlayer());
        
        // 切换到玩家2
        game.nextTurn();
        assertEquals(player2, game.getCurrentPlayer());
        
        // 切换回玩家1
        game.nextTurn();
        assertEquals(player1, game.getCurrentPlayer());
    }
} 