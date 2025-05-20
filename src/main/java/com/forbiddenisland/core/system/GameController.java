package com.forbiddenisland.core.system;

import com.forbiddenisland.core.model.*;
import com.forbiddenisland.enums.DifficultyLevel;
import com.forbiddenisland.enums.SpecialCardType;
import com.forbiddenisland.enums.TileName;
import com.forbiddenisland.enums.TreasureType;

import java.util.List;
import java.util.ArrayList;

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
        // 初始化基本组件
        this.players = new ArrayList<>();
        this.islandTiles = new ArrayList<>();
        this.cardDeckManager = new CardDeckManager();
        this.difficulty = DifficultyLevel.NORMAL; // 默认难度
        this.waterMeter = new WaterMeter(difficulty);
        this.gameOver = false;
        this.gameWon = false;
        
        // 初始化岛屿
        initializeIsland();
    }

    private void initializeIsland() {
        // 创建岛屿瓦片
        islandTiles.add(new IslandTile(TileName.CRYSTAL_CAVE, 0, 0));
        islandTiles.add(new IslandTile(TileName.CORAL_PALACE, 1, 0));
        islandTiles.add(new IslandTile(TileName.TEMPLE_OF_THE_SUN, 2, 0));
        islandTiles.add(new IslandTile(TileName.TEMPLE_OF_THE_MOON, 0, 1));
        islandTiles.add(new IslandTile(TileName.IRON_ANVIL_ROCK, 1, 1));
        islandTiles.add(new IslandTile(TileName.CLIFF_OF_AGES, 2, 1));
        // ... 添加更多瓦片
    }

    public void startGame(List<Adventurer> players, DifficultyLevel difficulty) {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("Players list cannot be null or empty");
        }
        this.players = players;
        this.difficulty = difficulty;
        initializeGame();
    }

    private void initializeGame() {
        // 设置初始水位
        this.waterMeter = new WaterMeter(difficulty);
        
        // 初始化玩家位置
        for (Adventurer player : players) {
            player.setCurrentTile(findFoolsLanding());
        }
        
        // 初始化回合管理器
        this.turnManager = new TurnManager(players);
        
        // 重新创建卡牌管理器以确保初始化
        this.cardDeckManager = new CardDeckManager();
        
        // 重置游戏状态
        gameOver = false;
        gameWon = false;
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

    // Check if all treasures have been captured by the players
    // Returns true if all treasures have been captured
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

    // Getters
    public List<Adventurer> getPlayers() { return players; }
    public List<IslandTile> getIslandTiles() { return islandTiles; }
    
    // check if the game is over (either won or lost)
    public boolean isGameOver() {
        // First check for win condition
        if (isGameWon()) {
            gameOver = true;
            return true;
        }
        
        // Then check for loss conditions
        if (isFoolsLandingSunk() ||
            areAllTreasureSitesSunk() ||
            isWaterTooHigh() ||
            isPlayerDrowned()) {
            
            gameOver = true;
            gameWon = false;
            return true;
        }
        
        return gameOver;
    }
    
    // check if players have won the game
    public boolean isGameWon() {
        // Win condition: all treasures captured, all players at Fools' Landing, 
        // and helicopter lift card played
        
        // avoid recalculating if already determined
        if (gameOver && gameWon) {
            return true;
        }
        
        // first, all treasures must be captured
        if (!areAllTreasuresCaptured()) {
            return false;
        }
        
        // next, all players must be at Fools' Landing
        IslandTile foolsLanding = findFoolsLanding();
        if (foolsLanding == null || foolsLanding.isSunk()) {
            return false; // Fools' Landing sunk, can't win
        }
        
        // check if all players are at Fools' Landing
        for (Adventurer player : players) {
            if (!foolsLanding.equals(player.getCurrentTile())) {
                return false; // not all players at Fools' Landing
            }
        }
        
        // TODO: check if helicopter lift card was played
        // For now we'll assume this is true if other conditions are met
        
        // all conditions met, set game state to won
        gameWon = true;
        gameOver = true;
        return true;
    }
    
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
    
    // get the water meter to check water level
    public WaterMeter getWaterMeter() {
        return waterMeter;
    }
    
    // helper method to find Fools' Landing tile
    private IslandTile findFoolsLanding() {
        if (islandTiles == null) return null;
        
        // Find the Fools' Landing tile by name
        for (IslandTile tile : islandTiles) {
            if ("Fools' Landing".equals(tile.getName())) {
                return tile;
            }
        }
        return null;
    }
    
    // check if Fools' Landing has sunk (game over condition)
    private boolean isFoolsLandingSunk() {
        IslandTile foolsLanding = findFoolsLanding();
        return foolsLanding == null || foolsLanding.isSunk();
    }
    
    // check if all treasure sites for at least one treasure have sunk
    private boolean areAllTreasureSitesSunk() {
        // map to track treasure types -> list of sites that have sunk
        java.util.Map<com.forbiddenisland.enums.TreasureType, Integer> treasureSitesRemaining = 
                new java.util.HashMap<>();
        
        // initialize all treasure types with 0 sites
        for (com.forbiddenisland.enums.TreasureType type : com.forbiddenisland.enums.TreasureType.values()) {
            treasureSitesRemaining.put(type, 0);
        }
        
        // count non-sunken sites for each treasure
        for (IslandTile tile : islandTiles) {
            if (!tile.isSunk() && tile.getTreasure() != null) {
                com.forbiddenisland.enums.TreasureType tileType = tile.getTreasure();
                treasureSitesRemaining.put(tileType, treasureSitesRemaining.get(tileType) + 1);
            }
        }
        
        // check if any treasure has 0 sites remaining
        for (Integer siteCount : treasureSitesRemaining.values()) {
            if (siteCount == 0) {
                return true; // all sites for at least one treasure have sunk
            }
        }
        
        return false;
    }
    
    // check if water level is at maximum (game over condition)
    private boolean isWaterTooHigh() {
        return waterMeter != null && waterMeter.isAtMaxLevel();
    }
    
    // check if any player is on a sunken tile with no escape
    private boolean isPlayerDrowned() {
        if (players == null || islandTiles == null) return false;
        
        for (Adventurer player : players) {
            IslandTile currentTile = player.getCurrentTile();
            
            // If player's tile is sunk and they can't escape, they've drowned
            if (currentTile != null && currentTile.isSunk() && !canEscape(player, currentTile)) {
                return true;
            }
        }
        
        return false;
    }
    
    // check if a player can escape from their current (sunken) tile
    private boolean canEscape(Adventurer player, IslandTile fromTile) {
        // Special case for diver who can move through sunken tiles
        if (player.getType() == com.forbiddenisland.enums.AdventurerType.DIVER) {
            // Diver can always escape unless all tiles are sunk
            return !areAllTilesSunk();
        }
        
        // For other players, check if there are adjacent non-sunken tiles
        java.util.List<IslandTile> adjacentTiles = getAdjacentTiles(fromTile);
        for (IslandTile tile : adjacentTiles) {
            if (!tile.isSunk()) {
                return true; // found adjacent non-sunken tile
            }
        }
        
        return false;
    }
    
    // check if all tiles are sunk (extreme case)
    private boolean areAllTilesSunk() {
        if (islandTiles == null) return false;
        
        for (IslandTile tile : islandTiles) {
            if (!tile.isSunk()) {
                return false; // found at least one non-sunken tile
            }
        }
        
        return true;
    }
    
    // get adjacent tiles to a given tile
    private java.util.List<IslandTile> getAdjacentTiles(IslandTile tile) {
        java.util.List<IslandTile> adjacentTiles = new java.util.ArrayList<>();
        
        if (tile == null || islandTiles == null) {
            return adjacentTiles;
        }
        
        int x = tile.getX();
        int y = tile.getY();
        
        // Check all four directions
        for (IslandTile candidate : islandTiles) {
            int cx = candidate.getX();
            int cy = candidate.getY();
            
            // Tiles are adjacent if they differ by 1 in exactly one dimension
            if ((Math.abs(x - cx) == 1 && y == cy) || (x == cx && Math.abs(y - cy) == 1)) {
                adjacentTiles.add(candidate);
            }
        }
        
        return adjacentTiles;
    }

    // 添加获取当前回合阶段的方法
    public com.forbiddenisland.enums.TurnPhase getCurrentPhase() {
        if (turnManager != null) {
            return turnManager.getCurrentPhase();
        }
        return com.forbiddenisland.enums.TurnPhase.ACTION; // 默认返回行动阶段
    }
}    