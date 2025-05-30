package com.forbiddenisland.model;

/**
 * Represents a "Sandbags" card in the game.
 * 代表游戏中的一张"沙袋"牌。
 */
public class SandbagsCard extends SpecialActionCard {

    /**
     * Constructor for SandbagsCard.
     * SandbagsCard 的构造函数。
     */
    public SandbagsCard() {
        super("Sandbag");
    }

    /**
     * Describes the Sandbags card.
     * 描述"沙袋"牌。
     * @return A string describing the card. (描述卡牌的字符串)
     */
    @Override
    public String getDescription() {
        return "Sandbags: Use this card to shore up a tile on the island, preventing it from flooding.";
    }

    // The specific action for this card (shoring up a tile) will be implemented
    // in the game logic when this card is played.
    // 此牌的具体行动（填补板块）将在打出此牌时在游戏逻辑中实现。
} 