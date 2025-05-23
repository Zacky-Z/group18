package com.forbiddenisland.model;

/**
 * Abstract base class for Special Action cards in the game.
 * 游戏中特殊行动卡牌的抽象基类。
 */
public abstract class SpecialActionCard extends Card {

    /**
     * Constructor for SpecialActionCard.
     * SpecialActionCard 的构造函数。
     * @param name The name of the special action card. (特殊行动卡牌的名称)
     */
    public SpecialActionCard(String name) {
        super(name);
    }

    /**
     * Abstract method to define the action of the special card.
     * 定义特殊卡牌行动的抽象方法。
     * This method would typically interact with the GameState or other relevant game components.
     * 此方法通常会与 GameState 或其他相关的游戏组件交互。
     */
    // public abstract void performAction(Game game); // Example: performAction(Game game)
    // In a real implementation, you'd pass necessary game state or controller objects.
    // 在实际实现中，您需要传递必要数量的游戏状态或控制器对象。
} 