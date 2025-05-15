package com.forbiddenisland.core.system;

import com.forbiddenisland.core.model.*;
import com.forbiddenisland.enums.DifficultyLevel;
import com.forbiddenisland.enums.SpecialCardType;

import java.util.List;

public class GameController {
    private List<Adventurer> players;
    private List<IslandTile> islandTiles;
    private CardDeckManager cardDeckManager;
    private TurnManager turnManager;
    private WaterMeter waterMeter;
    private DifficultyLevel difficulty;
    private boolean gameOver;
    private boolean gameWon;

    public GameController() {
        // 初始化游戏组件
    }

    public void startGame(List<Adventurer> players, DifficultyLevel difficulty) {
        this.players = players;
        this.difficulty = difficulty;
        initializeGame();
    }

    private void initializeGame() {
        // 初始化岛屿、卡牌、水位等
    }

    // 游戏控制方法
    public void nextTurn() {
        // 处理下一回合逻辑
    }

    public void performAction(String actionType, Object... params) {
        // 执行玩家动作
    }

    public void drawTreasureCards() {
        // Draw 2 treasure cards for the current player
        if (cardDeckManager == null || turnManager == null) {
            throw new IllegalStateException("CardDeckManager and TurnManager must be initialized");
        }
        Adventurer currentPlayer = turnManager.getCurrentPlayer();
        for (int i = 0; i < 2; i++) {
            TreasureCard drawnCard = cardDeckManager.drawTreasureCard();
            if (drawnCard == null) {
                // No more cards to draw
                continue;
            }
            if (drawnCard instanceof TreasureCard) {
                // Add treasure card to player's hand
                currentPlayer.addTreasureCard(drawnCard);
            } else if (drawnCard instanceof SpecialActionCard) {
                SpecialActionCard specialCard = (SpecialActionCard) drawnCard;
                // If the card is WATERS_RISE, handle its effect immediately
                if (specialCard.getCardType() == SpecialCardType.WATERS_RISE) {
                    // Raise water level and reshuffle flood discard pile into flood deck
                    if (waterMeter != null) {
                        waterMeter.raiseWaterLevel();
                    }
                    cardDeckManager.discardTreasureCard(specialCard);
                    // TODO: Implement flood discard pile reshuffle
                } else {
                    // Add special action card to player's hand
                    currentPlayer.addSpecialCard(specialCard);
                }
            }
        }
    }

    public void drawFloodCards() {
        // 抽取洪水卡牌
    }

    // Getters
    public List<Adventurer> getPlayers() { return players; }
    public List<IslandTile> getIslandTiles() { return islandTiles; }
    public boolean isGameOver() { return gameOver; }
    public boolean isGameWon() { return gameWon; }
}    