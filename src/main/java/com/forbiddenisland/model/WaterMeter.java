package com.forbiddenisland.model;

/**
 * Represents the Water Level Meter in the game.
 * 代表游戏中的水位计。
 */
public class WaterMeter {
    // Water levels could be: Novice, Normal, Elite, Legendary, Skull & Crossbones (game over)
    // 水位可以是：新手，普通，精英，传奇，骷髅头与交叉骨（游戏结束）
    // For simplicity, we can represent these as integer values 1-5, where 5 is game over.
    // 为简单起见，我们可以将它们表示为整数值 1-5，其中 5 表示游戏结束。
    // The actual number of flood cards drawn will correspond to these levels.
    // 实际抽取的洪水牌数量将对应这些级别。

    private int currentWaterLevel;
    private final int maxWaterLevel; // The level at which the game is lost (游戏失败的水位)
    private final int[] floodCardsToDraw; // Array to map water level to number of flood cards
                                      // 用于将水位映射到洪水牌数量的数组

    /**
     * Constructor for WaterMeter.
     * WaterMeter 的构造函数。
     * @param startingLevel The initial water level (1-4, typically). (初始水位（通常为1-4）)
     * @param maxLevel The maximum water level indicating game loss. (指示游戏失败的最高水位)
     * @param cardsPerLevel An array indicating how many flood cards to draw at each level.
     *                      一个数组，指示每个级别要抽取多少张洪水牌。
     *                      Example: {2, 2, 3, 3, 4, 4, 5, 5, 6, 100} for 10 levels where 10 means game over (100 cards drawn effectively means game over)
     *                      例如：对于10个级别，{2, 2, 3, 3, 4, 4, 5, 5, 6, 100}，其中10表示游戏结束（抽取100张牌实际上意味着游戏结束）
     */
    public WaterMeter(int startingLevel, int maxLevel, int[] cardsPerLevel) {
        this.currentWaterLevel = startingLevel;
        this.maxWaterLevel = maxLevel;
        this.floodCardsToDraw = cardsPerLevel; // Should have enough entries for all levels up to maxLevel
                                            // 应有足够的条目对应所有级别，直至最高级别
    }

    /**
     * Gets the current water level.
     * 获取当前水位。
     * @return The current water level. (当前水位)
     */
    public int getCurrentWaterLevel() {
        return currentWaterLevel;
    }

    /**
     * Increases the water level by one.
     * 将水位提高一级。
     */
    public void increaseWaterLevel() {
        if (currentWaterLevel < maxWaterLevel) {
            currentWaterLevel++;
        }
        // If it reaches maxWaterLevel, game over condition is met.
        // 如果达到最高水位，则满足游戏结束条件。
    }

    /**
     * Gets the number of flood cards to draw at the current water level.
     * 获取当前水位下需要抽取的洪水牌数量。
     * @return The number of flood cards to draw. (需要抽取的洪水牌数量)
     */
    public int getNumberOfFloodCardsToDraw() {
        // Ensure level is within bounds of the array
        // 确保级别在数组范围内
        if (currentWaterLevel > 0 && currentWaterLevel <= floodCardsToDraw.length) {
            return floodCardsToDraw[currentWaterLevel - 1]; // -1 because levels are 1-indexed, array is 0-indexed
                                                        // -1 因为级别是1索引的，数组是0索引的
        } else if (currentWaterLevel > floodCardsToDraw.length) {
            // If water level exceeds defined card draw amounts (e.g. Skull and Crossbones level)
            // 如果水位超过定义的抽牌数量（例如骷髅和交叉骨级别）
            return floodCardsToDraw[floodCardsToDraw.length -1]; // Return the highest defined draw number
                                                                // 返回定义的最高抽牌数量
        }
        return 0; // Should not happen in normal play if configured correctly (如果配置正确，正常游戏中不应发生)
    }

    /**
     * Checks if the water level has reached the game over state (Skull and Crossbones).
     * 检查水位是否已达到游戏结束状态（骷髅头与交叉骨）。
     * @return true if the water level is at maximum, false otherwise. (如果水位达到最高则为 true，否则为 false)
     */
    public boolean hasReachedMaxLevel() {
        return currentWaterLevel >= maxWaterLevel;
    }

    /**
     * Gets the label for the current water level (e.g., Novice, Normal, etc.).
     * This is a placeholder; a more robust mapping might be needed.
     * 获取当前水位的标签（例如，新手，普通等）。
     * 这是一个占位符；可能需要更强大的映射。
     * @return A string label for the water level. (水位的字符串标签)
     */
    public String getWaterLevelLabel() {
        // This mapping needs to be aligned with the game rules for difficulty levels.
        // 此映射需要与难度级别的游戏规则保持一致。
        // Example: 1=Novice, 2=Normal, 3=Elite, 4=Legendary, 5=GAME OVER
        // 例如：1=新手, 2=普通, 3=精英, 4=传奇, 5=游戏结束
        if (currentWaterLevel >= maxWaterLevel) {
            return "Skull & Crossbones (GAME OVER / 游戏结束)";
        }
        // This is a simplified mapping. Actual game might have more levels.
        // 这是一个简化的映射。实际游戏可能有更多级别。
        // The rules state: Novice, Normal, Elite, Legendary. Let's assume these are levels 1-4.
        // 规则规定：新手、普通、精英、传奇。我们假设这些是1-4级。
        // The number of flood cards drawn is what really matters, based on `floodCardsToDraw` array.
        // 真正重要的是根据 `floodCardsToDraw` 数组抽取的洪水牌数量。
        switch (currentWaterLevel) {
            case 1: return "Novice (新手)";
            case 2: return "Normal (普通)";
            case 3: return "Elite (精英)";
            case 4: return "Legendary (传奇)";
            default:
                // If levels go beyond 4 but are not yet maxLevel
                // 如果级别超过4但尚未达到最高级别
                if (currentWaterLevel < maxWaterLevel) {
                    return "Level " + currentWaterLevel + " (等级 " + currentWaterLevel + ")";
                }
                return "Unknown Level (未知等级)";
        }
    }
} 