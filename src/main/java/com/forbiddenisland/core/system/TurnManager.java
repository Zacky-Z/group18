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
        // Move to the next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        // Reset turn state
        currentPhase = TurnPhase.ACTION;
        actionsRemaining = MAX_ACTIONS_PER_TURN;
    }

    public void useAction() {
        if (actionsRemaining > 0) {
            actionsRemaining--;
        }

        // Check if it's time to move to the next phase
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
            // End of turn
        }
    }

    // Other turn management methods
}