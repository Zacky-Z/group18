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
        // 基础赠卡逻辑（玩家在同一瓷砖上且卡牌类型匹配）
        // 特殊角色（如信差）的赠卡逻辑将在子类或策略中实现
        return false;
    }
}    