package com.forbiddenisland.model;
import java.io.Serializable;
/**
 * Represents the outcome of drawing a single flood card.
 * 代表抽取一张洪水牌的结果。
 */
public class FloodActionReport {
    private final String floodCardName;
    private final String affectedTileName;
    private final String outcomeDescription; // e.g., "flooded", "sunk", "already sunk", "tile not found"
                                        // 例如："已淹没"，"已沉没"，"早已沉没"，"板块未找到"

    public FloodActionReport(String floodCardName, String affectedTileName, String outcomeDescription) {
        this.floodCardName = floodCardName;
        this.affectedTileName = affectedTileName;
        this.outcomeDescription = outcomeDescription;
    }

    public String getFloodCardName() {
        return floodCardName;
    }

    public String getAffectedTileName() {
        return affectedTileName;
    }

    public String getOutcomeDescription() {
        return outcomeDescription;
    }

    @Override
    public String toString() {
        return "Flood Card: " + floodCardName + ", Tile: " + affectedTileName + ", Outcome: " + outcomeDescription;
    }
} 