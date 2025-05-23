package game;

public class WaterMeter {
    private final int maxLevel = 5;
    private int currentLevel = 1; // 1 = Novice

    public void rise() {
        if (currentLevel < maxLevel) {
            currentLevel++;
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getFloodCardCount() {
        switch (currentLevel) {
            case 1: return 2;
            case 2: return 3;
            case 3: return 4;
            case 4: return 5;
            case 5: return 5; // max flood rate
            default: return 2;
        }
    }

    public boolean isAtMaxLevel() {
        return currentLevel >= maxLevel;
    }
}