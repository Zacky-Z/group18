package com.forbiddenisland.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.io.Serializable;

/**
 * Represents a player in the game.
 * Core class for player management and movement mechanics implementation (Week 11).
 * 代表游戏中的一个玩家。
 */
public class Player implements Serializable{
    // Core player attributes
    private String name; // Name of the player (玩家姓名)
    private AdventurerRole role; // The adventurer role of the player (玩家的探险家角色)
    private Pawn pawn; // The player's pawn on the game board (玩家的棋子)
    private List<Card> hand; // The cards currently in the player's hand (玩家当前手牌)
    private List<TreasureType> collectedTreasures; // Treasures collected by the player (玩家收集到的宝藏)

    // Game constants and state tracking
    public static final int MAX_HAND_SIZE = 5; // Maximum number of cards a player can hold (玩家最多可持有的卡牌数量)
    private boolean pilotAbilityUsedThisTurn = false; // Tracks if pilot's special ability was used this turn

    /**
     * Constructor for Player.
     * Player 的构造函数。
     * @param name The name of the player. (玩家的姓名)
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.collectedTreasures = new ArrayList<>();
        // Role and Pawn will be assigned during game setup.
        // 角色和棋子将在游戏设置阶段分配。
    }

    /**
     * Gets the name of the player.
     * 获取玩家的姓名。
     * @return The player's name. (玩家的姓名)
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the adventurer role for the player and initializes their pawn.
     * 设置玩家的探险家角色并初始化其棋子。
     * @param role The AdventurerRole to assign. (要分配的探险家角色)
     * @param startingTile The IslandTile where the pawn starts. (棋子开始的岛屿板块)
     * @param pawnColor The color for the pawn. (棋子的颜色)
     */
    public void assignRoleAndPawn(AdventurerRole role, IslandTile startingTile, String pawnColor) {
        this.role = role;
        this.pawn = new Pawn(pawnColor, startingTile);
    }

    /**
     * Gets the player's adventurer role.
     * 获取玩家的探险家角色。
     * @return The AdventurerRole. (探险家角色)
     */
    public AdventurerRole getRole() {
        return role;
    }

    /**
     * Gets the player's pawn.
     * 获取玩家的棋子。
     * @return The Pawn object. (Pawn 对象)
     */
    public Pawn getPawn() {
        return pawn;
    }

    /**
     * Gets the player's current hand of cards.
     * 获取玩家当前的手牌。
     * @return A list of Card objects. (Card 对象列表)
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Adds a card to the player's hand.
     * 将一张牌加入玩家手牌。
     * @param card The card to add. (要添加的卡牌)
     */
    public void addCardToHand(Card card) {
        // Hand limit check is now managed by the Game logic after drawing cards
        // 手牌上限检查现在由抽牌后的游戏逻辑管理
            hand.add(card); 
    }

    /**
     * Removes a card from the player's hand.
     * 从玩家手牌中移除一张牌。
     * @param card The card to remove. (要移除的卡牌)
     * @return true if the card was successfully removed, false otherwise. (如果成功移除卡牌则为 true，否则为 false)
     */
    public boolean removeCardFromHand(Card card) {
        return hand.remove(card);
    }

    /**
     * Gets the list of treasures collected by the player.
     * 获取玩家收集到的宝藏列表。
     * @return A list of TreasureType. (TreasureType 列表)
     */
    public List<TreasureType> getCollectedTreasures() {
        return collectedTreasures;
    }

    /**
     * Adds a collected treasure to the player's list.
     * 将一个收集到的宝藏添加到玩家的列表中。
     * @param treasure The TreasureType collected. (收集到的 TreasureType)
     */
    public void addCollectedTreasure(TreasureType treasure) {
        if (!collectedTreasures.contains(treasure)) {
            collectedTreasures.add(treasure);
        }
    }

    /**
     * Checks if the player has collected all four treasures.
     * 检查玩家是否已收集所有四个宝藏。
     * @return true if all treasures are collected, false otherwise. (如果所有宝藏都已收集则为 true，否则为 false)
     */
    public boolean hasCollectedAllTreasures() {
        // Assuming there are 4 treasures in the game
        // 假设游戏中有4个宝藏
        return collectedTreasures.size() == 4; 
    }

    /**
     * Checks if the player's hand size exceeds the maximum limit.
     * 检查玩家的手牌数量是否超过上限。
     * @return true if hand size is over limit, false otherwise. (如果手牌数量超限则为 true，否则为 false)
     */
    public boolean isHandOverLimit() {
        return hand.size() > MAX_HAND_SIZE;
    }

    public boolean isPilotAbilityUsedThisTurn() {
        return pilotAbilityUsedThisTurn;
    }

    public void setPilotAbilityUsedThisTurn(boolean pilotAbilityUsedThisTurn) {
        this.pilotAbilityUsedThisTurn = pilotAbilityUsedThisTurn;
    }

    /**
     * Resets any abilities that are limited to once per turn.
     * 重置任何每回合只能使用一次的能力。
     */
    public void resetTurnBasedAbilities() {
        if (this.role == AdventurerRole.PILOT) {
            this.pilotAbilityUsedThisTurn = false;
            System.out.println(this.name + " (Pilot) flight ability reset. (飞行员飞行能力已重置)");
        }
    }

    /**
     * Gets the player's current location on the game board.
     * 获取玩家在游戏棋盘上的当前位置。
     * @return The IslandTile the player is currently on. (玩家当前所在的岛屿板块)
     */
    public IslandTile getCurrentLocation() {
        if (this.pawn == null) {
            System.err.println("Error: Player " + name + " has no pawn assigned. Cannot get current location.");
            return null; 
        }
        return this.pawn.getCurrentLocation();
    }

    /**
     * Moves the player's pawn to a new destination tile.
     * 将玩家的棋子移动到新的目标板块。
     * @param destination The IslandTile to move to. (要移动到的岛屿板块)
     */
    public void moveTo(IslandTile destination) {
        if (this.pawn == null) {
            System.err.println("Error: Player " + name + " has no pawn assigned. Cannot move.");
            return;
        }
        if (destination == null) {
            System.err.println("Error: Cannot move " + name + " to a null destination.");
            return;
        }
        this.pawn.setCurrentLocation(destination);
        System.out.println(name + " moved to " + destination.getName() + ". (移动到 " + destination.getName() + "。)");
    }

    /**
     * Calculates all valid moves for the player based on their role and current game state.
     * 根据玩家的角色和当前游戏状态计算所有有效移动。
     * @param game The current game instance. (当前游戏实例)
     * @return A set of IslandTile objects representing valid destinations. (代表有效目标地点的 IslandTile 对象集合)
     */
    public Set<IslandTile> getValidMoves(Game game) {
        Set<IslandTile> validMoves = new HashSet<>();
        if (this.pawn == null || this.role == null || game == null) {
            return validMoves; // Should not happen in a normal game
        }

        IslandTile currentLocation = getCurrentLocation();
        if (currentLocation == null || game.getIslandTileByName(currentLocation.getName()) == null) {
            return validMoves; // Player not on board or current location sunk
        }

        // Pilot's special ability: fly to any tile on the island (once per turn)
        if (role == AdventurerRole.PILOT && !isPilotAbilityUsedThisTurn()) {
            for (int r_idx = 0; r_idx < game.getGameBoard().length; r_idx++) {
                for (int c_idx = 0; c_idx < game.getGameBoard()[r_idx].length; c_idx++) {
                    IslandTile tile = game.getGameBoard()[r_idx][c_idx];
                    // Tile must exist on board (not null) and not be the player's current location
                    if (tile != null && tile != currentLocation) { 
                        validMoves.add(tile);
                    }
                }
            }
        }

        int[] currentCoords = game.getTileCoordinates(currentLocation);
        if (currentCoords == null) {
            return validMoves; 
        }
        int r = currentCoords[0];
        int c = currentCoords[1];

        // Standard orthogonal moves (and diagonal for Explorer)
        boolean canMoveDiagonally = (role == AdventurerRole.EXPLORER);
        List<IslandTile> adjacent = game.getValidAdjacentTiles(r, c, canMoveDiagonally);
        for (IslandTile tile : adjacent) {
            // To be a valid move destination, tile must exist on board (not null)
            if (tile != null) { 
                validMoves.add(tile);
            }
        }
        
        // Diver's special ability: move through one or more adjacent missing (null) and/or flooded tiles
        if (role == AdventurerRole.DIVER) {
            Queue<IslandTile> queue = new LinkedList<>();
            Set<IslandTile> visitedForDiver = new HashSet<>();
            
            // Add current location to visited to avoid moving to self through a path
            visitedForDiver.add(currentLocation);

            // Add initial adjacent tiles (can be null/sunk or flooded for traversal)
            // For diver, adjacent tiles are those physically next to current, even if sunk.
            // game.getValidAdjacentTiles returns non-null tiles. We need a different helper or inline logic for diver's first step.
            
            int[] dr = {-1, 1, 0, 0, -1, -1, 1, 1}; // Orthogonal + Diagonal for Diver
            int[] dc = {0, 0, -1, 1, -1, 1, -1, 1}; 

            for (int i = 0; i < dr.length; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];
                if (game.isValidBoardCoordinate(nr, nc)) { // Check if coordinate is on board grid
                    IslandTile adjacentTile = game.getGameBoard()[nr][nc]; // This can be null if sunk
                    if (adjacentTile != null) { // If it's a tile (not a hole where a tile used to be)
                         if (!visitedForDiver.contains(adjacentTile)) {
                            queue.add(adjacentTile);
                            visitedForDiver.add(adjacentTile);
                            // If this adjacent tile is not sunk (i.e. still in game.islandTileMap), it's a valid direct move.
                            if (game.getIslandTileByName(adjacentTile.getName()) != null) {
                                validMoves.add(adjacentTile);
                            }
                        }
                    } else { // adjacentTile is null (sunk), Diver can pass through this *space*
                        // Create a temporary placeholder or use coordinates to represent passing through a sunk space.
                        // For simplicity in this BFS, we only add actual IslandTile objects to queue if they are traversable (flooded or sunk but an object exists for map lookup).
                        // If gameBoard[nr][nc] is null, it means it is permanently gone. Diver cannot enter a space that is null on gameBoard directly.
                        // The rule is "move through one or more adjacent *missing* and/or flooded tiles".
                        // This implies we need to find a path *over* such tiles to a non-sunk tile.
                        // The current BFS structure assumes we are jumping between IslandTile objects.
                        // A tile that is gameBoard[nr][nc] == null IS a missing tile.
                        // Let's refine BFS to handle coordinates of such missing tiles.
                        // For now, if gameBoard[nr][nc] is null, the Diver cannot use it as a step in *this* BFS model that adds IslandTiles to queue.
                        // This part of Diver logic needs careful review against precise game rules for "missing tiles".
                        // Assuming "missing" means gameBoard[r][c] == null, and the Diver can traverse such *coordinates*.
                        // The current `game.getValidAdjacentTiles` used later in BFS only returns non-null tiles.
                    }
                }
            }

            while (!queue.isEmpty()) {
                IslandTile currentTileForDiver = queue.poll(); // This tile exists (not null from gameBoard initially)
                int[] diverTileCoords = game.getTileCoordinates(currentTileForDiver);

                // A tile is traversable by diver if it's flooded OR if it's sunk (i.e., no longer in islandTileMap but was an IslandTile object)
                // The key is that islandTileMap check for being sunk.
                boolean isTraversableByDiver = currentTileForDiver.isFlooded() || game.getIslandTileByName(currentTileForDiver.getName()) == null;

                if (diverTileCoords != null && isTraversableByDiver) {
                    // Explore neighbors of this traversable (flooded/sunk) tile
                    for (int i = 0; i < dr.length; i++) {
                        int nnr = diverTileCoords[0] + dr[i];
                        int nnc = diverTileCoords[1] + dc[i];

                        if (game.isValidBoardCoordinate(nnr, nnc)) {
                            IslandTile neighbor = game.getGameBoard()[nnr][nnc]; // Can be null if sunk spot
                            if (neighbor != null) { // If there is a tile object there
                                if (!visitedForDiver.contains(neighbor)) {
                                    visitedForDiver.add(neighbor);
                                    // If this neighbor is NOT sunk (still in islandTileMap), it's a valid destination
                                    if (game.getIslandTileByName(neighbor.getName()) != null) {
                                        validMoves.add(neighbor);
                                    }
                                    // If neighbor is traversable (flooded or sunk), add to queue to continue path
                                    if (neighbor.isFlooded() || game.getIslandTileByName(neighbor.getName()) == null) {
                                        queue.add(neighbor);
                                    }
                                }
                            } 
                            // If neighbor is null (a truly sunk spot), Diver can pass over it, 
                            // but this BFS is tile-object based. Handling movement over truly empty spots 
                            // would require a coordinate-based BFS or a different approach.
                            // For now, this BFS for Diver explores paths through tiles that are either flooded 
                            // or are IslandTile objects that represent sunk locations (but are not null in gameBoard initially for BFS step).
                        }
                    }
                }
            }
        }
        return validMoves;
    }

    // Actions (to be implemented or called from Game/Controller)
    // 行动 (将在 Game/Controller 中实现或调用)

    /**
     * Shore up (stabilize) a flooded tile.
     * 稳固（治水）一个被淹没的板块。
     * @param tileToShoreUp The tile to shore up.
     * @return true if the shore up action was successful, false otherwise.
     */
    public boolean shoreUp(IslandTile tileToShoreUp) {
        if (tileToShoreUp == null || !tileToShoreUp.isFlooded()) {
            System.out.println("Cannot shore up: tile is null or not flooded");
            return false;
        }
        
        tileToShoreUp.shoreUp();
        System.out.println(this.name + " shored up " + tileToShoreUp.getName());
        return true;
    }
    
    /**
     * Give a treasure card to another player.
     * 将宝藏卡给予另一个玩家。
     * @param recipient The player to give the card to.
     * @param card The treasure card to give.
     * @return true if the card was successfully given, false otherwise.
     */
    public boolean giveTreasureCard(Player recipient, TreasureCard card) {
        if (recipient == null || card == null) {
            System.out.println("Cannot give card: recipient or card is null");
            return false;
        }
        
        if (!this.hand.contains(card)) {
            System.out.println("Cannot give card: " + this.name + " does not have this card");
            return false;
        }
        
        this.removeCardFromHand(card);
        recipient.addCardToHand(card);
        System.out.println(this.name + " gave " + card.getName() + " to " + recipient.getName());
        return true;
    }
    
    /**
     * Capture a treasure by discarding the required treasure cards.
     * 通过弃掉所需的宝藏卡来获取宝藏。
     * @param treasureType The type of treasure to capture.
     * @param treasureDeck The treasure deck to discard the cards to.
     * @return true if the treasure was successfully captured, false otherwise.
     */
    public boolean captureTreasure(TreasureType treasureType, Deck<Card> treasureDeck) {
        if (treasureType == null || treasureDeck == null) {
            System.out.println("Cannot capture treasure: treasureType or treasureDeck is null");
            return false;
        }
        
        int requiredCards = this.role.getTreasureCardsNeededForCapture();
        List<Card> matchingCards = new ArrayList<>();
        
        // Find matching treasure cards
        for (Card card : this.hand) {
            if (card instanceof TreasureCard) {
                TreasureCard treasureCard = (TreasureCard) card;
                if (treasureCard.getTreasureType() == treasureType) {
                    matchingCards.add(card);
                }
            }
        }
        
        // Check if we have enough cards
        if (matchingCards.size() < requiredCards) {
            System.out.println(this.name + " needs " + requiredCards + " " + treasureType.getDisplayName() + 
                               " cards to capture the treasure, but only has " + matchingCards.size());
            return false;
        }
        
        // Discard the required number of cards
        for (int i = 0; i < requiredCards; i++) {
            Card card = matchingCards.get(i);
            this.removeCardFromHand(card);
            treasureDeck.discardCard(card);
        }
        
        // Add the treasure to the player's collection
        this.addCollectedTreasure(treasureType);
        System.out.println(this.name + " captured the " + treasureType.getDisplayName() + " treasure!");
        return true;
    }
} 