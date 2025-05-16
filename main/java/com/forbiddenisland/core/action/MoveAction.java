package com.forbiddenisland.core.action;

import com.forbiddenisland.core.model.Adventurer;
import com.forbiddenisland.core.model.IslandTile;
import com.forbiddenisland.core.system.GameController;
import com.forbiddenisland.enums.AdventurerType;

import java.util.HashMap;
import java.util.Map;

public class MoveAction {
    private final GameController gameController;
    // Tracks pilot power usage within the current turn
    private Map<Adventurer, Boolean> pilotPowerUsed = new HashMap<>();
    
    public MoveAction(GameController gameController) {
        this.gameController = gameController;
    }

    public boolean execute(Adventurer player, IslandTile targetTile) {
        // Verify if target tile is movable to
        if (!isValidMove(player, targetTile)) {
            return false;
        }

        // Mark pilot power as used when utilizing fly ability
        if (player.getType() == AdventurerType.PILOT && 
            !isAdjacentMove(player.getCurrentTile(), targetTile)) {
            pilotPowerUsed.put(player, true);
        }
        
        // Perform the move
        player.setCurrentTile(targetTile);
        return true;
    }

    private boolean isValidMove(Adventurer player, IslandTile targetTile) {
        // Determine if normal move or special ability move
        if (player.getType() == AdventurerType.PILOT && !hasPilotUsedPower(player)) {
            // Pilot can fly to any non-sunken tile once per turn
            return !targetTile.isSunk();
        }
        
        // Delegate to player's movement logic which handles special abilities
        return player.canMoveTo(targetTile, gameController.getIslandTiles());
    }
    
    private boolean isAdjacentMove(IslandTile from, IslandTile to) {
        if (from == null || to == null) {
            return false;
        }
        
        return (Math.abs(from.getX() - to.getX()) == 1 && from.getY() == to.getY()) ||
               (Math.abs(from.getY() - to.getY()) == 1 && from.getX() == to.getX());
    }
    
    private boolean hasPilotUsedPower(Adventurer pilot) {
        return pilotPowerUsed.getOrDefault(pilot, false);
    }
    
    // Resets pilot ability at the start of a new turn
    public void resetPilotPower(Adventurer pilot) {
        if (pilot != null && pilot.getType() == AdventurerType.PILOT) {
            pilotPowerUsed.put(pilot, false);
        }
    }
    
    // Resets all adventurer abilities when starting a new round
    public void resetAllPowers() {
        pilotPowerUsed.clear();
    }
}    