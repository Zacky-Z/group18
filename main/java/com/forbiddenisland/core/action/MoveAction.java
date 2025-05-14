package com.forbiddenisland.core.action;

import com.forbiddenisland.core.model.Adventurer;
import com.forbiddenisland.core.model.IslandTile;
import com.forbiddenisland.core.system.GameController;

public class MoveAction {
    private final GameController gameController;

    public MoveAction(GameController gameController) {
        this.gameController = gameController;
    }

    public boolean execute(Adventurer player, IslandTile targetTile) {
        // 检查目标瓷砖是否可移动到
        if (!isValidMove(player, targetTile)) {
            return false;
        }

        // 执行移动
        player.setCurrentTile(targetTile);
        return true;
    }

    private boolean isValidMove(Adventurer player, IslandTile targetTile) {
        // 基础移动逻辑（相邻瓷砖且未沉没）
        // 特殊角色（如飞行员）的移动逻辑将在子类或策略中实现
        return false;
    }
}    