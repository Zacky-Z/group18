package com.forbiddenisland.model;

/**
 * Represents a "Helicopter Lift" card in the game.
 * 代表游戏中的一张"直升机升空"牌。
 */
public class HelicopterLiftCard extends SpecialActionCard {

    /**
     * Constructor for HelicopterLiftCard.
     * HelicopterLiftCard 的构造函数。
     */
    public HelicopterLiftCard() {
        super("Helicopter Lift");
    }

    /**
     * Describes the Helicopter Lift card.
     * 描述"直升机升空"牌。
     * @return A string describing the card. (描述卡牌的字符串)
     */
    @Override
    public String getDescription() {
        return "Helicopter Lift: Move any number of pawns to any tile on the board.";
    }

    // The specific action for this card (moving pawns) will be implemented
    // in the game logic when this card is played.
    // 此牌的具体行动（移动棋子）将在打出此牌时在游戏逻辑中实现。
} 