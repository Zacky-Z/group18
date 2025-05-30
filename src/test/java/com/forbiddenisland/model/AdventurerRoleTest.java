package com.forbiddenisland.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试AdventurerRole类的特殊能力
 */
public class AdventurerRoleTest {

    @Test
    public void testExplorerAbilities() {
        // 测试探险家能力
        AdventurerRole explorer = AdventurerRole.EXPLORER;
        
        // 测试斜向移动能力
        assertTrue(explorer.canMoveDiagonally());
        
        // 测试斜向治水能力
        assertTrue(explorer.canShoreUpDiagonally());
        
        // 确认探险家每次治水只能治理一个板块
        assertEquals(1, explorer.getShoreUpCountPerAction());
        
        // 确认探险家不能飞行到任何板块
        assertFalse(explorer.canFlyToAnyTile());
        
        // 确认探险家不能穿越已淹没或已缺失的板块
        assertFalse(explorer.canMoveThroughFloodedOrMissingTiles());
        
        // 确认探险家不能移动其他玩家
        assertFalse(explorer.canMoveOtherPlayer());
        
        // 确认探险家不能在任何地方给卡
        assertFalse(explorer.canGiveCardAnywhere());
        
        // 确认探险家需要4张宝藏卡获取宝藏
        assertEquals(4, explorer.getTreasureCardsNeededForCapture());
    }

    @Test
    public void testPilotAbilities() {
        // 测试飞行员能力
        AdventurerRole pilot = AdventurerRole.PILOT;
        
        // 确认飞行员不能斜向移动
        assertFalse(pilot.canMoveDiagonally());
        
        // 确认飞行员不能斜向治水
        assertFalse(pilot.canShoreUpDiagonally());
        
        // 确认飞行员每次治水只能治理一个板块
        assertEquals(1, pilot.getShoreUpCountPerAction());
        
        // 测试飞行员的关键能力：可以飞到任何板块
        assertTrue(pilot.canFlyToAnyTile());
        
        // 确认飞行员不能穿越已淹没或已缺失的板块
        assertFalse(pilot.canMoveThroughFloodedOrMissingTiles());
        
        // 确认飞行员不能移动其他玩家
        assertFalse(pilot.canMoveOtherPlayer());
        
        // 确认飞行员不能在任何地方给卡
        assertFalse(pilot.canGiveCardAnywhere());
        
        // 确认飞行员需要4张宝藏卡获取宝藏
        assertEquals(4, pilot.getTreasureCardsNeededForCapture());
    }

    @Test
    public void testEngineerAbilities() {
        // 测试工程师能力
        AdventurerRole engineer = AdventurerRole.ENGINEER;
        
        // 确认工程师不能斜向移动
        assertFalse(engineer.canMoveDiagonally());
        
        // 确认工程师不能斜向治水
        assertFalse(engineer.canShoreUpDiagonally());
        
        // 测试工程师的关键能力：每次治水可以治理两个板块
        assertEquals(2, engineer.getShoreUpCountPerAction());
        
        // 确认工程师不能飞到任何板块
        assertFalse(engineer.canFlyToAnyTile());
        
        // 确认工程师不能穿越已淹没或已缺失的板块
        assertFalse(engineer.canMoveThroughFloodedOrMissingTiles());
        
        // 确认工程师不能移动其他玩家
        assertFalse(engineer.canMoveOtherPlayer());
        
        // 确认工程师不能在任何地方给卡
        assertFalse(engineer.canGiveCardAnywhere());
        
        // 确认工程师需要4张宝藏卡获取宝藏
        assertEquals(4, engineer.getTreasureCardsNeededForCapture());
    }

    @Test
    public void testDiverAbilities() {
        // 测试潜水员能力
        AdventurerRole diver = AdventurerRole.DIVER;
        
        // 确认潜水员不能斜向移动
        assertFalse(diver.canMoveDiagonally());
        
        // 确认潜水员不能斜向治水
        assertFalse(diver.canShoreUpDiagonally());
        
        // 确认潜水员每次治水只能治理一个板块
        assertEquals(1, diver.getShoreUpCountPerAction());
        
        // 确认潜水员不能飞到任何板块
        assertFalse(diver.canFlyToAnyTile());
        
        // 测试潜水员的关键能力：可以穿越已淹没或已缺失的板块
        assertTrue(diver.canMoveThroughFloodedOrMissingTiles());
        
        // 确认潜水员不能移动其他玩家
        assertFalse(diver.canMoveOtherPlayer());
        
        // 确认潜水员不能在任何地方给卡
        assertFalse(diver.canGiveCardAnywhere());
        
        // 确认潜水员需要4张宝藏卡获取宝藏
        assertEquals(4, diver.getTreasureCardsNeededForCapture());
    }

    @Test
    public void testNavigatorAbilities() {
        // 测试领航员能力
        AdventurerRole navigator = AdventurerRole.NAVIGATOR;
        
        // 确认领航员不能斜向移动
        assertFalse(navigator.canMoveDiagonally());
        
        // 确认领航员不能斜向治水
        assertFalse(navigator.canShoreUpDiagonally());
        
        // 确认领航员每次治水只能治理一个板块
        assertEquals(1, navigator.getShoreUpCountPerAction());
        
        // 确认领航员不能飞到任何板块
        assertFalse(navigator.canFlyToAnyTile());
        
        // 确认领航员不能穿越已淹没或已缺失的板块
        assertFalse(navigator.canMoveThroughFloodedOrMissingTiles());
        
        // 测试领航员的关键能力：可以移动其他玩家
        assertTrue(navigator.canMoveOtherPlayer());
        
        // 确认领航员不能在任何地方给卡
        assertFalse(navigator.canGiveCardAnywhere());
        
        // 确认领航员需要4张宝藏卡获取宝藏
        assertEquals(4, navigator.getTreasureCardsNeededForCapture());
    }

    @Test
    public void testMessengerAbilities() {
        // 测试信使能力
        AdventurerRole messenger = AdventurerRole.MESSENGER;
        
        // 确认信使不能斜向移动
        assertFalse(messenger.canMoveDiagonally());
        
        // 确认信使不能斜向治水
        assertFalse(messenger.canShoreUpDiagonally());
        
        // 确认信使每次治水只能治理一个板块
        assertEquals(1, messenger.getShoreUpCountPerAction());
        
        // 确认信使不能飞到任何板块
        assertFalse(messenger.canFlyToAnyTile());
        
        // 确认信使不能穿越已淹没或已缺失的板块
        assertFalse(messenger.canMoveThroughFloodedOrMissingTiles());
        
        // 确认信使不能移动其他玩家
        assertFalse(messenger.canMoveOtherPlayer());
        
        // 测试信使的关键能力：可以在任何地方给卡
        assertTrue(messenger.canGiveCardAnywhere());
        
        // 确认信使需要4张宝藏卡获取宝藏
        assertEquals(4, messenger.getTreasureCardsNeededForCapture());
    }

    @Test
    public void testArchaeologistAbilities() {
        // 测试考古学家能力
        AdventurerRole archaeologist = AdventurerRole.ARCHAEOLOGIST;
        
        // 确认考古学家不能斜向移动
        assertFalse(archaeologist.canMoveDiagonally());
        
        // 确认考古学家不能斜向治水
        assertFalse(archaeologist.canShoreUpDiagonally());
        
        // 确认考古学家每次治水只能治理一个板块
        assertEquals(1, archaeologist.getShoreUpCountPerAction());
        
        // 确认考古学家不能飞到任何板块
        assertFalse(archaeologist.canFlyToAnyTile());
        
        // 确认考古学家不能穿越已淹没或已缺失的板块
        assertFalse(archaeologist.canMoveThroughFloodedOrMissingTiles());
        
        // 确认考古学家不能移动其他玩家
        assertFalse(archaeologist.canMoveOtherPlayer());
        
        // 确认考古学家不能在任何地方给卡
        assertFalse(archaeologist.canGiveCardAnywhere());
        
        // 测试考古学家的关键能力：只需要3张宝藏卡获取宝藏
        assertEquals(3, archaeologist.getTreasureCardsNeededForCapture());
    }
    
    @Test
    public void testChineseNames() {
        // 测试所有角色的中文名称是否正确
        assertEquals("探险家", AdventurerRole.EXPLORER.getChineseName());
        assertEquals("飞行员", AdventurerRole.PILOT.getChineseName());
        assertEquals("领航员", AdventurerRole.NAVIGATOR.getChineseName());
        assertEquals("潜水员", AdventurerRole.DIVER.getChineseName());
        assertEquals("工程师", AdventurerRole.ENGINEER.getChineseName());
        assertEquals("信使", AdventurerRole.MESSENGER.getChineseName());
        assertEquals("考古学家", AdventurerRole.ARCHAEOLOGIST.getChineseName());
    }
} 