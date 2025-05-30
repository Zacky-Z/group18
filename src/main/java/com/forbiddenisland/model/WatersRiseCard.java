package com.forbiddenisland.model;

/**
 * Represents a "Waters Rise!" card in the game.
 * 代表游戏中的一张"洪水上涨！"牌。
 */
public class WatersRiseCard extends SpecialActionCard {

    /**
     * Constructor for WatersRiseCard.
     * WatersRiseCard 的构造函数。
     */
    public WatersRiseCard() {
        super("Waters Rise!");
    }

    /**
     * Describes the Waters Rise! card.
     * 描述"洪水上涨！"牌。
     * @return A string describing the card. (描述卡牌的字符串)
     */
    @Override
    public String getDescription() {
        return "Waters Rise! - Raise the water level by one step and shuffle the flood cards.";
    }

    // The specific action for this card (raising water level, shuffling flood cards)
    // will be implemented in the game logic when this card is drawn.
    // 此牌的具体行动（提升水位，洗混洪水牌）将在抽取此牌时在游戏逻辑中实现。
} 