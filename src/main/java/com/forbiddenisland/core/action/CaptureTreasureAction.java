package com.forbiddenisland.core.action;

import com.forbiddenisland.core.model.Adventurer;
import com.forbiddenisland.core.model.IslandTile;
import com.forbiddenisland.core.model.Treasure;
import com.forbiddenisland.core.model.TreasureCard;
import com.forbiddenisland.core.system.GameController;
import com.forbiddenisland.enums.TreasureType;

import java.util.List;

public class CaptureTreasureAction {
    private final GameController gameController;

    public CaptureTreasureAction(GameController gameController) {
        this.gameController = gameController;
    }

    public boolean execute(Adventurer player) {
        IslandTile currentTile = player.getCurrentTile();
        TreasureType treasureType = currentTile.getTreasure();

        // Check if the treasure can be captured
        if (!canCaptureTreasure(player, treasureType)) {
            return false;
        }

        // Execute treasure capture
        // Remove the required cards
        removeRequiredCards(player, treasureType);

        // Mark the treasure as captured
        markTreasureAsCaptured(treasureType);

        return true;
    }

    private boolean canCaptureTreasure(Adventurer player, TreasureType treasureType) {
        if (treasureType == null) {
            return false;
        }

        // Check tile status
        IslandTile currentTile = player.getCurrentTile();
        if (currentTile.isFlooded() || currentTile.isSunk()) {
            return false;
        }

        // Check if the player has enough cards
        return hasRequiredCards(player, treasureType);
    }

    private boolean hasRequiredCards(Adventurer player, TreasureType treasureType) {
        // Base requirement: 4 cards of the same type
        // Special roles (e.g., Explorer) logic will be implemented in subclasses or strategies
        return false;
    }

    private void removeRequiredCards(Adventurer player, TreasureType treasureType) {
        // Remove the required cards from the player's hand
    }

    private void markTreasureAsCaptured(TreasureType treasureType) {
        // Mark the treasure as captured in the game controller
    }
}