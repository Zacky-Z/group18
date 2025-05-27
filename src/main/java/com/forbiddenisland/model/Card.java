package com.forbiddenisland.model;

/**
 * Abstract base class for all cards in the game.
 * 游戏中所有卡牌的抽象基类。
 */
public abstract class Card {
    private String name; // Name of the card (卡牌名称)

    /**
     * Constructor for Card.
     * Card 的构造函数。
     * @param name The name of the card. (卡牌的名称)
     */
    public Card(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the card.
     * 获取卡牌的名称。
     * @return The name of the card. (卡牌的名称)
     */
    public String getName() {
        return name;
    }

    /**
     * Abstract method to describe the card's type or action.
     * 用于描述卡牌类型或动作的抽象方法。
     * @return A string describing the card. (描述卡牌的字符串)
     */
    public abstract String getDescription();
} 