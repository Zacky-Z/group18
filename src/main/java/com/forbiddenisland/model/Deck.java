package com.forbiddenisland.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Represents a deck of cards in the game.
 * @param <T> The type of cards in the deck, must extend Card.
 */
public class Deck<T extends Card> {
    private Stack<T> drawPile;    // The pile of cards to draw from
    private List<T> discardPile; // The pile of discarded cards

    /**
     * Constructor for Deck.
     * Initializes with a list of cards that form the initial draw pile.
     * @param initialCards The list of cards to start the deck with.
     */
    public Deck(List<T> initialCards) {
        this.drawPile = new Stack<>();
        this.discardPile = new ArrayList<>();
        if (initialCards != null) {
            this.drawPile.addAll(initialCards);
        }
        shuffleDrawPile();
    }

    /**
     * Shuffles the draw pile.
     */
    public void shuffleDrawPile() {
        Collections.shuffle(this.drawPile);
    }

    /**
     * Draws a card from the top of the draw pile.
     * If the draw pile is empty, it attempts to reshuffle the discard pile into the draw pile.
     * @return The card drawn, or null if no cards are available.
     */
    public T drawCard() {
        if (drawPile.isEmpty()) {
            if (discardPile.isEmpty()) {
                return null; // No cards left anywhere
            }
            reshuffleDiscardIntoDraw();
            }

        if (drawPile.isEmpty()) {
            return null;
        }
        
        return drawPile.pop();
    }

    /**
     * Adds a card to the discard pile.
     * @param card The card to discard.
     */
    public void discardCard(T card) {
        if (card != null) {
            discardPile.add(card);
        }
    }

    /**
     * Adds a list of cards to the top of the draw pile.
     * Used for "Waters Rise!" where discard pile is shuffled and placed on top.
     * @param cards The list of cards to add.
     */
    public void addCardsToDrawPileTop(List<T> cards) {
        if (cards != null) {
            // Add in reverse order to maintain the order of the list when popping from stack
            for (int i = cards.size() - 1; i >= 0; i--) {
                drawPile.push(cards.get(i));
            }
        }
    }

    /**
     * Takes all cards from the discard pile, shuffles them, and places them into the draw pile.
     * The discard pile becomes empty.
     */
    public void reshuffleDiscardIntoDraw() {
        if (!discardPile.isEmpty()) {
            List<T> tempList = new ArrayList<>(discardPile);
            Collections.shuffle(tempList);
            for (int i = 0; i < tempList.size(); i++) {
                drawPile.push(tempList.get(i));
            }
            discardPile.clear();

            System.out.println("Reshuffled " + tempList.size() + " cards from discard pile into draw pile");
        }
    }

    /**
     * Gets the current discard pile.
     * @return A list of cards in the discard pile.
     */
    public List<T> getDiscardPile() {
        return new ArrayList<>(discardPile); // Return a copy to prevent external modification
    }

    /**
     * Clears the discard pile.
     */
    public void clearDiscardPile() {
        discardPile.clear();
    }

    /**
     * Checks if the draw pile is empty.
     * @return true if the draw pile is empty, false otherwise.
     */
    public boolean isDrawPileEmpty() {
        return drawPile.isEmpty();
    }

    /**
     * Gets the number of cards remaining in the draw pile.
     * @return The number of cards in the draw pile.
     */
    public int getDrawPileSize() {
        return drawPile.size();
    }

    /**
     * Gets the number of cards in the discard pile.
     * @return The number of cards in the discard pile.
     */
    public int getDiscardPileSize() {
        return discardPile.size();
    }
} 