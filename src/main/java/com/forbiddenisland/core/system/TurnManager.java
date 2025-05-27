package com.forbiddenisland.core.system;

import com.forbiddenisland.core.model.Adventurer;
import com.forbiddenisland.enums.TurnPhase;

import java.util.List;

public class TurnManager {
    private List<Adventurer> players;
    private int currentPlayerIndex;
    private TurnPhase currentPhase;
    private int actionsRemaining;
    private static final int MAX_ACTIONS_PER_TURN = 3;

    public TurnManager(List<Adventurer> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
        this.currentPhase = TurnPhase.ACTION;
        this.actionsRemaining = MAX_ACTIONS_PER_TURN;
    }

    public Adventurer getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public TurnPhase getCurrentPhase() {
        return currentPhase;
    }

    public int getActionsRemaining() {
        return actionsRemaining;
    }

    public void startNextTurn() {
        // 移动到下一个玩家
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        
        // 重置回合状态
        currentPhase = TurnPhase.ACTION;
        actionsRemaining = MAX_ACTIONS_PER_TURN;
    }

    public void useAction() {
        if (actionsRemaining > 0) {
            actionsRemaining--;
        }
        
        // 检查是否需要进入下一阶段
        if (actionsRemaining == 0 && currentPhase == TurnPhase.ACTION) {
            currentPhase = TurnPhase.TREASURE_CARD_DRAW;
        }
    }

    public void completeTreasureCardDrawPhase() {
        if (currentPhase == TurnPhase.TREASURE_CARD_DRAW) {
            currentPhase = TurnPhase.FLOOD;
        }
    }

    public void completeFloodPhase() {
        if (currentPhase == TurnPhase.FLOOD) {
            // 回合结束
        }
    }

    // 其他回合管理方法
}    