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

        // 检查是否可以获取宝物
        if (!canCaptureTreasure(player, treasureType)) {
            return false;
        }

        // 执行获取宝物
        // 移除所需卡牌
        removeRequiredCards(player, treasureType);
        
        // 标记宝物为已获取
        markTreasureAsCaptured(treasureType);
        
        return true;
    }

    private boolean canCaptureTreasure(Adventurer player, TreasureType treasureType) {
        if (treasureType == null) {
            return false;
        }

        // 检查瓷砖状态
        IslandTile currentTile = player.getCurrentTile();
        if (currentTile.isFlooded() || currentTile.isSunk()) {
            return false;
        }

        // 检查玩家是否有足够的卡牌
        return hasRequiredCards(player, treasureType);
    }

    private boolean hasRequiredCards(Adventurer player, TreasureType treasureType) {
        // 基础需要4张相同类型的卡牌
        // 特殊角色（如探险家）的逻辑将在子类或策略中实现
        return false;
    }

    private void removeRequiredCards(Adventurer player, TreasureType treasureType) {
        // 从玩家手中移除所需的卡牌
    }

    private void markTreasureAsCaptured(TreasureType treasureType) {
        // 在游戏控制器中标记宝物为已获取
    }
}    