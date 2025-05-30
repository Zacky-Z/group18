package com.forbiddenisland.model;

/**
 * Abstract base class for all cards in the game.
 */
public abstract class Card {
    private String name; // Name of the card

    /**
     * Constructor for Card.
     * @param name The name of the card.
     */
    public Card(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the card.
     * @return The name of the card. (
     */
    public String getName() {
        return name;
    }

    /**
     * Abstract method to describe the card's type or action.
     * @return A string describing the card.
     */
    public abstract String getDescription();
} 