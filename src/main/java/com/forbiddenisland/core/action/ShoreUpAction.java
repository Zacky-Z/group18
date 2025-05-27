package com.forbiddenisland.core.action;

import com.forbiddenisland.core.model.Adventurer;
import com.forbiddenisland.core.model.IslandTile;
import com.forbiddenisland.core.system.GameController;
import com.forbiddenisland.enums.AdventurerType;

import java.util.HashMap;
import java.util.Map;

public class ShoreUpAction {
    private final GameController gameController;
    // Records if engineer has already used one shore up in single action
    private Map<Adventurer, Boolean> engineerShoreUpUsed = new HashMap<>();

    public ShoreUpAction(GameController gameController) {
        this.gameController = gameController;
    }

    public boolean execute(Adventurer player, IslandTile targetTile) {
        // Validate shore up possibility
        if (!isValidShoreUp(player, targetTile)) {
            return false;
        }

        // Execute shore up
        boolean success = targetTile.shoreUp();
        
        // For Engineer, mark first shore-up as completed
        if (success && player.getType() == AdventurerType.ENGINEER) {
            engineerShoreUpUsed.put(player, true);
        }
        
        return success;
    }

    // Verifies if this is engineer's second shore up in a single action
    public boolean canShoreUpAgain(Adventurer engineer) {
        return engineer.getType() == AdventurerType.ENGINEER && 
               engineerShoreUpUsed.getOrDefault(engineer, false);
    }
    
    // Clears engineer ability tracking
    public void resetEngineerAbility(Adventurer engineer) {
        if (engineer != null && engineer.getType() == AdventurerType.ENGINEER) {
            engineerShoreUpUsed.remove(engineer);
        }
    }
    
    // Resets all engineer abilities at the beginning of a new round
    public void resetAllEngineerAbilities() {
        engineerShoreUpUsed.clear();
    }

    private boolean isValidShoreUp(Adventurer player, IslandTile targetTile) {
        // Utilizes player's shore up logic which accounts for special abilities
        return player.canShoreUp(targetTile, gameController.getIslandTiles());
    }
}    