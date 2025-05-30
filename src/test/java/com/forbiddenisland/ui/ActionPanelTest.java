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
 * Test class for ActionPanel functionality.
 * Tests the basic movement logic using simple test methods.
 */
public class ActionPanelTest {

    /**
     * Tests the basic movement logic.
     * Since ActionPanel depends on JavaFX components, we only test the core logic here.
     */
    @Test
    public void testMovementLogic() {
        // Create test tiles
        IslandTile startTile = new IslandTile("Start Tile");
        IslandTile destinationTile = new IslandTile("Destination Tile");
        
        // Create player
        Player player = new Player("Test Player");
        player.assignRoleAndPawn(AdventurerRole.EXPLORER, startTile, "GREEN");
        
        // Verify initial player position
        assertEquals(startTile, player.getCurrentLocation());
        
        // Simulate movement
        player.moveTo(destinationTile);
        
        // Verify player position after movement
        assertEquals(destinationTile, player.getCurrentLocation());
    }

    /**
     * Tests the Explorer's diagonal movement ability.
     */
    @Test
    public void testExplorerDiagonalMovement() {
        // Create a simple game instance
        Game game = new Game(Arrays.asList("Test Player"), 1, 
                             Arrays.asList(AdventurerRole.EXPLORER));
        
        // Get player
        Player player = game.getCurrentPlayer();
        
        // Verify player is Explorer
        assertEquals(AdventurerRole.EXPLORER, player.getRole());
        
        // Verify Explorer can move diagonally
        assertTrue(player.getRole().canMoveDiagonally());
    }
    
    /**
     * Tests the Pilot's flight ability.
     */
    @Test
    public void testPilotFlightAbility() {
        // Create a simple game instance
        Game game = new Game(Arrays.asList("Test Player"), 1, 
                             Arrays.asList(AdventurerRole.PILOT));
        
        // Get player
        Player player = game.getCurrentPlayer();
        
        // Verify player is Pilot
        assertEquals(AdventurerRole.PILOT, player.getRole());
        
        // Verify Pilot can fly to any tile
        assertTrue(player.getRole().canFlyToAnyTile());
        
        // Verify Pilot ability is initially unused
        assertFalse(player.isPilotAbilityUsedThisTurn());
        
        // Simulate using flight ability
        player.setPilotAbilityUsedThisTurn(true);
        assertTrue(player.isPilotAbilityUsedThisTurn());
        
        // Simulate turn end, reset ability
        player.resetTurnBasedAbilities();
        assertFalse(player.isPilotAbilityUsedThisTurn());
    }
    
    /**
     * Tests the action point consumption mechanism.
     */
    @Test
    public void testActionPointConsumption() {
        // Create a simple game instance
        Game game = new Game(Arrays.asList("Test Player"), 1);
        
        // Get initial action points
        int initialActions = game.getActionsRemainingInTurn();
        assertEquals(Game.MAX_ACTIONS_PER_TURN, initialActions);
        
        // Consume one action point
        assertTrue(game.spendAction());
        assertEquals(initialActions - 1, game.getActionsRemainingInTurn());
        
        // Consume all action points
        while (game.getActionsRemainingInTurn() > 0) {
            assertTrue(game.spendAction());
        }
        
        // Verify no more action points can be consumed
        assertFalse(game.spendAction());
    }
    
    /**
     * Tests game phase changes.
     */
    @Test
    public void testGamePhaseChanges() {
        // Create a simple game instance
        Game game = new Game(Arrays.asList("Test Player"), 1);
        
        // Should initially be in action phase
        assertEquals(Game.GamePhase.ACTION_PHASE, game.getCurrentPhase());
        
        // Switch to draw treasure cards phase
        game.setCurrentPhase(Game.GamePhase.DRAW_TREASURE_CARDS_PHASE);
        assertEquals(Game.GamePhase.DRAW_TREASURE_CARDS_PHASE, game.getCurrentPhase());
        
        // Switch to draw flood cards phase
        game.setCurrentPhase(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE);
        assertEquals(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE, game.getCurrentPhase());
    }
} 