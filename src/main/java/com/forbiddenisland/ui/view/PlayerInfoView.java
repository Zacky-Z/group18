package com.forbiddenisland.ui.view;

import com.forbiddenisland.core.model.Adventurer;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PlayerInfoView extends VBox {
    private Label currentPlayerLabel;
    private Label actionsRemainingLabel;
    private Label currentTileLabel;
    private Label treasureCardsLabel;
    private Label specialCardsLabel;

    public PlayerInfoView() {
        initializeComponents();
    }

    private void initializeComponents() {
        setSpacing(10);
        setPadding(new javafx.geometry.Insets(10));
        
        currentPlayerLabel = new Label("当前玩家: ");
        actionsRemainingLabel = new Label("剩余行动: ");
        currentTileLabel = new Label("当前位置: ");
        treasureCardsLabel = new Label("宝物卡牌: ");
        specialCardsLabel = new Label("特殊卡牌: ");
        
        getChildren().addAll(
            new Label("玩家信息"),
            currentPlayerLabel,
            actionsRemainingLabel,
            currentTileLabel,
            new Label("手牌:"),
            treasureCardsLabel,
            specialCardsLabel
        );
    }

    public void updatePlayerInfo(Adventurer player, int actionsRemaining) {
        currentPlayerLabel.setText("当前玩家: " + player.getName() + " (" + player.getType().getDisplayName() + ")");
        actionsRemainingLabel.setText("剩余行动: " + actionsRemaining);
        currentTileLabel.setText("当前位置: " + player.getCurrentTile().getName().getDisplayName());
        
        // 更新卡牌信息
        updateCardsInfo(player);
    }

    private void updateCardsInfo(Adventurer player) {
        StringBuilder treasureCardsText = new StringBuilder("宝物卡牌: ");
        player.getTreasureCards().forEach(card -> 
            treasureCardsText.append(card.getTreasureType().getDisplayName()).append(", ")
        );
        
        StringBuilder specialCardsText = new StringBuilder("特殊卡牌: ");
        player.getSpecialCards().forEach(card -> 
            specialCardsText.append(card.getName()).append(", ")
        );
        
        treasureCardsLabel.setText(treasureCardsText.toString());
        specialCardsLabel.setText(specialCardsText.toString());
    }
    
    // this is a wrapper method to handle multiple players
    // will display the current player (first player or one with 'isCurrent' flag if supported
    public void updatePlayerInfo(java.util.List<Adventurer> players) {
        if (players == null || players.isEmpty()) {
            // just show something basic if no players
            currentPlayerLabel.setText("No players available");
            return;
        }
        
        // just show first player info for now, we can improve later to show active player
        // 1. This is a bit of a hack - in a real system we'd keep track of current player
        // 2. I'm lazy so this is good enough
        Adventurer currentPlayer = players.get(0);
        
        // Try to find active player if players have that info
        for (Adventurer player : players) {
            if (player.isCurrentPlayer()) {
                currentPlayer = player;
                break;
            }
        }
        
        // now just use the existing method
        // TODO: get actual actions remaining from game state
        updatePlayerInfo(currentPlayer, 3);
    }
}    