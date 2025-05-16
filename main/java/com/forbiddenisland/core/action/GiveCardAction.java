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
        // 检查是否可以赠卡
        if (!isValidCardTransfer(giver, receiver, card)) {
            return false;
        }

        // 执行赠卡
        giver.removeTreasureCard(card);
        receiver.addTreasureCard(card);
        return true;
    }

    private boolean isValidCardTransfer(Adventurer giver, Adventurer receiver, TreasureCard card) {
        // Both players must be on the same tile and the giver must have the card
        if (giver == null || receiver == null || card == null) {
            return false;
        }
        if (giver.getCurrentTile() == null || receiver.getCurrentTile() == null) {
            return false;
        }
        if (!giver.getCurrentTile().equals(receiver.getCurrentTile())) {
            return false;
        }
        if (!giver.getTreasureCards().contains(card)) {
            return false;
        }
        return true;
    }
}    