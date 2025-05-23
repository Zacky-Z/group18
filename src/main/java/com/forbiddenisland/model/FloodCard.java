package com.forbiddenisland.model;

/**
 * Represents a Flood card in the game.
 * 代表游戏中的一张洪水牌。
 */
public class FloodCard extends Card {
    private String islandTileName; // The name of the island tile this card corresponds to (此牌对应的岛屿板块名称)

    /**
     * Constructor for FloodCard.
     * FloodCard 的构造函数。
     * @param islandTileName The name of the island tile this flood card affects. (此洪水牌影响的岛屿板块的名称)
     */
    public FloodCard(String islandTileName) {
        super(islandTileName); // Card name is the island tile name (卡牌名称即岛屿板块名称)
        this.islandTileName = islandTileName;
    }

    /**
     * Gets the name of the island tile this card affects.
     * 获取此牌影响的岛屿板块的名称。
     * @return The name of the island tile. (岛屿板块的名称)
     */
    public String getIslandTileName() {
        return islandTileName;
    }

    /**
     * Describes the flood card.
     * 描述洪水牌。
     * @return A string describing the card. (描述卡牌的字符串)
     */
    @Override
    public String getDescription() {
        return "Flood card for tile: " + islandTileName; // 洪水牌对应板块：[板块名称]
    }
} 