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
        // Initialize card decks
        this.cardDeckManager = new CardDeckManager();
        // TODO: Initialize other game components (island, water meter, etc.)
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
                    cardDeckManager.reshuffleFloodDiscardPile(); // Reshuffle flood discard pile into flood deck
                } else {
                    // Add special action card to player's hand
                    currentPlayer.addSpecialCard(specialCard);
                }
            }
        }
    }

    public void drawFloodCards() {
        // Draw flood cards according to the current flood rate
        if (cardDeckManager == null || waterMeter == null || islandTiles == null) {
            throw new IllegalStateException("CardDeckManager, WaterMeter, and islandTiles must be initialized");
        }
        int floodCount = waterMeter.getFloodRate();
        for (int i = 0; i < floodCount; i++) {
            FloodCard floodCard = cardDeckManager.drawFloodCard();
            if (floodCard == null) {
                // No more flood cards to draw
                continue;
            }
            // Find the corresponding IslandTile by tile name
            for (IslandTile tile : islandTiles) {
                if (tile.getName() == floodCard.getTileName()) {
                    tile.flood();
                    break;
                }
            }
            // Discard the flood card
            cardDeckManager.discardFloodCard(floodCard);
        }
    }

    // Getters
    public List<Adventurer> getPlayers() { return players; }
    public List<IslandTile> getIslandTiles() { return islandTiles; }
    public boolean isGameOver() { return gameOver; }
    public boolean isGameWon() { return gameWon; }
    
    // Additional getters and helpers
    public TurnManager getTurnManager() { return turnManager; }
    
    public Adventurer getCurrentPlayer() {
        if (turnManager != null) {
            return turnManager.getCurrentPlayer();
        }
        return null;
    }
    
    public void useAction() {
        if (turnManager != null) {
            turnManager.useAction();
        }
    }
    
    /**
     * Check if all treasures have been captured by the players
     * @return true if all treasures have been captured
     */
    public boolean areAllTreasuresCaptured() {
        // tODO: replace this with actual game state tracking
        // this is a stub for now - in real implementation 
        // would need to track which treasures are captured
        
        // step1: get all treasuretypes
        com.forbiddenisland.enums.TreasureType[] allTreasures = com.forbiddenisland.enums.TreasureType.values();
        
        // step2: check if all players have captured all treasures
        for (com.forbiddenisland.enums.TreasureType treasureType : allTreasures) {
            boolean treasureCaptured = false;
            
            // check if any player has this treasure
            for (Adventurer player : players) {
                for (Treasure t : player.getCapturedFigurines()) {
                    if (t.getType() == treasureType) {
                        treasureCaptured = true;
                        break;
                    }
                }
                if (treasureCaptured) break;
            }
            
            // if no one has captured this treasure, return false
            if (!treasureCaptured) {
                return false;
            }
        }
        
        // if we get here, all treasures are captured
        return true;
    }
}    