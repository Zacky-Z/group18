package com.forbiddenisland.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 * 代表游戏中的一个玩家。
 */
public class Player {
    private String name; // Name of the player (玩家姓名)
    private AdventurerRole role; // The adventurer role of the player (玩家的探险家角色)
    private Pawn pawn; // The player's pawn (玩家的棋子)
    private List<Card> hand; // The cards currently in the player's hand (玩家当前手牌)
    private List<TreasureType> collectedTreasures; // Treasures collected by the player (玩家收集到的宝藏)

    public static final int MAX_HAND_SIZE = 5; // Maximum number of cards a player can hold (玩家最多可持有的卡牌数量)

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
        if (hand.size() < MAX_HAND_SIZE) {
            hand.add(card);
        } else {
            // Handle hand limit exceeded - player must discard or play a card.
            // This logic will typically be handled in the game controller or game state.
            // 处理手牌上限超出的情况 - 玩家必须弃牌或打出一张牌。
            // 此逻辑通常会在游戏控制器或游戏状态中处理。
            System.out.println(name + "'s hand is full! Needs to discard. (" + name + " 手牌已满！需要弃牌。)");
            // For now, just add it, actual discard logic will be elsewhere
            // 目前只是添加，实际的弃牌逻辑将在别处实现
            hand.add(card); 
        }
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
        return collectedTreasures.size() == TreasureType.values().length;
    }

    /**
     * Checks if the player's hand size exceeds the maximum limit.
     * 检查玩家的手牌数量是否超过上限。
     * @return true if hand size is over limit, false otherwise. (如果手牌数量超限则为 true，否则为 false)
     */
    public boolean isHandOverLimit() {
        return hand.size() > MAX_HAND_SIZE;
    }

    // Actions (to be implemented or called from Game/Controller)
    // 行动 (将在 Game/Controller 中实现或调用)

    // public void move(IslandTile destination) { ... }
    // public void shoreUp(IslandTile tileToShoreUp) { ... }
    // public void giveTreasureCard(Player recipient, TreasureCard card) { ... }
    // public void captureTreasure(TreasureType treasureType) { ... }
} 