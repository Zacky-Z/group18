package com.forbiddenisland.core.action;

import com.forbiddenisland.core.model.Adventurer;
import com.forbiddenisland.core.model.TreasureCard;
import com.forbiddenisland.core.system.GameController;

public class GiveCardAction {
    private final GameController gameController;

    public GiveCardAction(GameController gameController) {
        this.gameController = gameController;
    }

    public boolean execute(Adventurer giver, Adventurer receiver, TreasureCard card) {
        // Check if the card transfer is valid
        if (!isValidCardTransfer(giver, receiver, card)) {
            return false;
        }

        // Execute the card transfer
        giver.removeTreasureCard(card);
        receiver.addTreasureCard(card);
        return true;
    }

    private boolean isValidCardTransfer(Adventurer giver, Adventurer receiver, TreasureCard card) {
        // Basic validation checks
        if (giver == null || receiver == null || card == null) {
            return false;
        }

        // Players must be on valid tiles
        if (giver.getCurrentTile() == null || receiver.getCurrentTile() == null) {
            return false;
        }

        // Check if giver has the card
        if (!giver.getTreasureCards().contains(card)) {
            return false;
        }

        // Handle Messenger's special ability - can give cards to players on any tile
        if (giver.getType() == com.forbiddenisland.enums.AdventurerType.MESSENGER) {
            return true; // Messenger can give cards to any player regardless of location
        }

        // Regular case - players must be on the same tile
        return giver.getCurrentTile().equals(receiver.getCurrentTile());
    }
}