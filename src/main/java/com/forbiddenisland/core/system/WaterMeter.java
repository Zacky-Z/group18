package com.forbiddenisland.core.system;

import com.forbiddenisland.enums.DifficultyLevel;

public class WaterMeter {
    private int waterLevel;
    private final int maxWaterLevel;
    private int floodRate;

    public WaterMeter(DifficultyLevel difficulty) {
        this.waterLevel = difficulty.getInitialWaterLevel();
        this.maxWaterLevel = 10;
        setFloodRateBasedOnDifficulty(difficulty);
    }

    private void setFloodRateBasedOnDifficulty(DifficultyLevel difficulty) {
        // Set flood rate based on difficulty
        switch (difficulty) {
            case NOVICE:
                floodRate = 2;
                break;
            case NORMAL:
                floodRate = 3;
                break;
            case ELITE:
                floodRate = 3;
                break;
            case LEGENDARY:
                floodRate = 4;
                break;
            default:
                floodRate = 3;
        }
    }

    public void raiseWaterLevel() {
        if (waterLevel < maxWaterLevel) {
            waterLevel++;
            updateFloodRate();
        }
    }

    private void updateFloodRate() {
        // Update flood rate based on water level
        if (waterLevel >= 8) {
            floodRate = 5;
        } else if (waterLevel >= 6) {
            floodRate = 4;
        } else if (waterLevel >= 3) {
            floodRate = 3;
        } else {
            floodRate = 2;
        }
    }

    // Getters
    public int getWaterLevel() { return waterLevel; }
    public int getFloodRate() { return floodRate; }
    public boolean isAtMaxLevel() { return waterLevel >= maxWaterLevel; }

    // Method to get the current water level
    public int getCurrentLevel() { return waterLevel; }
}