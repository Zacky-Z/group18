package com.forbiddenisland.model;

/**
 * Enum representing the different adventurer roles and their special abilities.
 * 代表不同探险家角色及其特殊能力的枚举。
 */
public enum AdventurerRole {
    // Each role will have a description of its special ability.
    // 每个角色都会有其特殊能力的描述。

    EXPLORER("Explorer (探险家): Can move and shore up diagonally. (可以斜向移动和填补板块)"),
    PILOT("Pilot (飞行员): Once per turn, can fly to any tile on the island for 1 action. (每回合一次，可以花费1个行动飞到岛上任何板块)"),
    NAVIGATOR("Navigator (领航员): Can move another player up to 2 adjacent tiles for 1 action. (可以花费1个行动将另一名玩家移动最多2个相邻板块)"),
    DIVER("Diver (潜水员): Can move through one or more adjacent missing and/or flooded tiles for 1 action. (可以花费1个行动穿过一个或多个相邻的已移除和/或已淹没的板块)"),
    ENGINEER("Engineer (工程师): Can shore up 2 tiles for 1 action. (可以花费1个行动填补2个板块)"),
    MESSENGER("Messenger (信使): Can give Treasure cards to another player anywhere on the island for 1 action. (可以花费1个行动将宝藏牌给予岛上任何地方的另一名玩家)");

    private final String description;

    /**
     * Constructor for AdventurerRole.
     * AdventurerRole 的构造函数。
     * @param description The description of the adventurer's special ability. (探险家特殊能力的描述)
     */
    AdventurerRole(String description) {
        this.description = description;
    }

    /**
     * Gets the description of the adventurer's special ability.
     * 获取探险家特殊能力的描述。
     * @return The description string. (描述字符串)
     */
    public String getDescription() {
        return description;
    }

    // We might add specific methods here later to check abilities, e.g., canMoveDiagonally()
    // 稍后我们可能会在这里添加具体的方法来检查能力，例如 canMoveDiagonally()
} 