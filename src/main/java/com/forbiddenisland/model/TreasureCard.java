package com.forbiddenisland.model;

/**
 * Represents a Treasure card in the game.
 * 代表游戏中的一张宝藏牌。
 */
public class TreasureCard extends Card {
    private TreasureType treasureType; // The type of treasure this card represents (此牌代表的宝藏类型)

    /**
     * Constructor for TreasureCard.
     * TreasureCard 的构造函数。
     * @param name The name of the card (e.g., "The Earth Stone Card"). (卡牌的名称，例如 "大地之石卡牌")
     * @param treasureType The type of treasure this card is for. (此牌对应的宝藏类型)
     */
    public TreasureCard(String name, TreasureType treasureType) {
        super(name);
        this.treasureType = treasureType;
    }

    /**
     * Gets the type of treasure this card represents.
     * 获取此牌代表的宝藏类型。
     * @return The TreasureType. (宝藏类型)
     */
    public TreasureType getTreasureType() {
        return treasureType;
    }

    /**
     * Describes the treasure card.
     * 描述宝藏牌。
     * @return A string describing the card. (描述卡牌的字符串)
     */
    @Override
    public String getDescription() {
        return "Treasure Card for: " + treasureType.toString(); // 宝藏牌对应：[宝藏名称]
    }
} 