package com.forbiddenisland.core.action;

import com.forbiddenisland.core.model.Adventurer;
import com.forbiddenisland.core.model.IslandTile;
import com.forbiddenisland.core.system.GameController;

public class ShoreUpAction {
    private final GameController gameController;

    public ShoreUpAction(GameController gameController) {
        this.gameController = gameController;
    }

    public boolean execute(Adventurer player, IslandTile targetTile) {
        // 检查是否可以加固
        if (!isValidShoreUp(player, targetTile)) {
            return false;
        }

        // 执行加固
        return targetTile.shoreUp();
    }

    private boolean isValidShoreUp(Adventurer player, IslandTile targetTile) {
        // 基础加固逻辑（玩家在瓷砖上或相邻瓷砖且瓷砖被洪水淹没）
        // 特殊角色（如工程师）的加固逻辑将在子类或策略中实现
        return false;
    }
}    