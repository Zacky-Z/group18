package com.forbiddenisland.model;

/**
 * Enum representing the different adventurer roles and their special abilities.
 * Core component of the special abilities system implementation.
 * 代表不同探险家角色及其特殊能力的枚举。
 */
public enum AdventurerRole {
    // Each adventurer has unique abilities that affect gameplay mechanics
    EXPLORER("Explorer: Can move and shore up diagonally", "探险家"),
    PILOT("Pilot: Can fly to any tile once per turn", "飞行员"),
    NAVIGATOR("Navigator: Can move other players up to 2 tiles for 1 action", "领航员"),
    DIVER("Diver: Can move through one or more adjacent flooded/missing tiles", "潜水员"),
    ENGINEER("Engineer: Can shore up 2 tiles for 1 action", "工程师"),
    MESSENGER("Messenger: Can give treasure cards to players anywhere", "信使"),
    ARCHAEOLOGIST("Archaeologist: Needs only 3 cards instead of 4 to capture treasures", "考古学家");

    private final String description;
    private final String chineseName;

    /**
     * Constructor for AdventurerRole.
     * Initializes a role with its special ability description and Chinese name.
     * @param description The description of the adventurer's special ability
     * @param chineseName The Chinese name of the role
     */
    AdventurerRole(String description, String chineseName) {
        this.description = description;
        this.chineseName = chineseName;
    }

    /**
     * Gets the description of the adventurer's special ability.
     * 获取探险家特殊能力的描述。
     * @return The description string. (描述字符串)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the Chinese name of the role.
     * 获取角色的中文名称。
     * @return The Chinese name. (中文名称)
     */
    public String getChineseName() {
        return chineseName;
    }

    /**
     * Checks if the role can move diagonally.
     * 检查角色是否可以斜向移动。
     * @return true if the role can move diagonally, false otherwise.
     */
    public boolean canMoveDiagonally() {
        return this == EXPLORER;
    }
    
    /**
     * Checks if the role can shore up diagonally.
     * 检查角色是否可以斜向治水。
     * @return true if the role can shore up diagonally, false otherwise.
     */
    public boolean canShoreUpDiagonally() {
        return this == EXPLORER;
    }
    
    /**
     * Gets the number of tiles that can be shored up per action.
     * 获取每个行动可以治水的板块数量。
     * @return The number of tiles that can be shored up per action.
     */
    public int getShoreUpCountPerAction() {
        return this == ENGINEER ? 2 : 1;
    }
    
    /**
     * Checks if the role can fly to any tile.
     * 检查角色是否可以飞到任何板块。
     * @return true if the role can fly to any tile, false otherwise.
     */
    public boolean canFlyToAnyTile() {
        return this == PILOT;
    }
    
    /**
     * Checks if the role can move through flooded or missing tiles.
     * 检查角色是否可以穿过已淹没或缺失的板块。
     * @return true if the role can move through flooded or missing tiles, false otherwise.
     */
    public boolean canMoveThroughFloodedOrMissingTiles() {
        return this == DIVER;
    }
    
    /**
     * Checks if the role can move other players.
     * 检查角色是否可以移动其他玩家。
     * @return true if the role can move other players, false otherwise.
     */
    public boolean canMoveOtherPlayer() {
        return this == NAVIGATOR;
    }
    
    /**
     * Checks if the role can give cards to players anywhere on the island.
     * 检查角色是否可以给予岛上任何地方的玩家卡牌。
     * @return true if the role can give cards anywhere, false otherwise.
     */
    public boolean canGiveCardAnywhere() {
        return this == MESSENGER;
    }
    
    /**
     * Gets the number of treasure cards needed to capture a treasure.
     * 获取获取宝藏所需的宝藏牌数量。
     * @return The number of treasure cards needed.
     */
    public int getTreasureCardsNeededForCapture() {
        return this == ARCHAEOLOGIST ? 3 : 4;
    }
} 