package com.forbiddenisland.core.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private IslandTile currentTile;
    private List<TreasureCard> treasureCards;
    private List<SpecialActionCard> specialCards;

    public Player(String name) {
        this.name = name;
        this.treasureCards = new ArrayList<>();
        this.specialCards = new ArrayList<>();
    }

    // Getters and setters
    public String getName() { return name; }
    public IslandTile getCurrentTile() { return currentTile; }
    public void setCurrentTile(IslandTile currentTile) { this.currentTile = currentTile; }
    public List<TreasureCard> getTreasureCards() { return treasureCards; }
    public List<SpecialActionCard> getSpecialCards() { return specialCards; }

    // Business methods
    public void addTreasureCard(TreasureCard card) {
        treasureCards.add(card);
    }

    public void addSpecialCard(SpecialActionCard card) {
        specialCards.add(card);
    }

    public void removeTreasureCard(TreasureCard card) {
        treasureCards.remove(card);
    }

    public void removeSpecialCard(SpecialActionCard card) {
        specialCards.remove(card);
    }
}    