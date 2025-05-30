package com.forbiddenisland.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class DeckTest {
    private Deck<Card> deck;
    private List<Card> initialCards;

    // Test implementation of Card for testing purposes
    private static class TestCard extends Card {
        public TestCard(String name) {
            super(name);
        }

        @Override
        public String getDescription() {
            return "Test card: " + getName();
        }
    }

    @BeforeEach
    void setUp() {
        initialCards = new ArrayList<>();
        // Add some test cards
        for (int i = 1; i <= 5; i++) {
            initialCards.add(new TestCard("Card " + i));
        }
        deck = new Deck<>(initialCards);
    }

    @Test
    void testDrawCard() {
        // Draw all cards and verify they're different
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < initialCards.size(); i++) {
            Card card = deck.drawCard();
            assertNotNull(card, "Card should not be null");
            drawnCards.add(card);
        }
        
        assertEquals(initialCards.size(), drawnCards.size(), 
            "Should draw same number of cards as initially added");
        
        // Try to draw when deck is empty
        Card emptyDraw = deck.drawCard();
        assertNull(emptyDraw, "Drawing from empty deck should return null");
    }

    @Test
    void testDiscardCard() {
        Card card = deck.drawCard();
        assertNotNull(card, "Should be able to draw a card");
        
        deck.discardCard(card);
        // After discarding, we should be able to draw it again after reshuffling
        // Draw all remaining cards first
        while (deck.drawCard() != null) {
            // Keep drawing
        }
        
        // Now draw the discarded card
        Card redrawnCard = deck.drawCard();
        assertNotNull(redrawnCard, "Should be able to draw discarded card after reshuffle");
    }

    @Test
    void testShuffleDrawPile() {
        // Draw all cards to get their initial order
        List<String> initialOrder = new ArrayList<>();
        Card card;
        while ((card = deck.drawCard()) != null) {
            initialOrder.add(card.getName());
        }
        
        // Reset the deck with the same cards
        deck = new Deck<>(initialCards);
        
        // Draw all cards again to get new order
        List<String> newOrder = new ArrayList<>();
        while ((card = deck.drawCard()) != null) {
            newOrder.add(card.getName());
        }
        
        // Note: There's a very small chance this could fail randomly if the shuffle
        // happens to produce the same order
        assertFalse(initialOrder.equals(newOrder), 
            "Cards should be in different order after shuffle");
    }

    @Test
    void testAddCardsToDrawPileTop() {
        List<Card> newCards = new ArrayList<>();
        newCards.add(new TestCard("Top Card 1"));
        newCards.add(new TestCard("Top Card 2"));
        
        deck.addCardsToDrawPileTop(newCards);
        
        // The next two cards drawn should be the ones we just added, in order
        Card firstDraw = deck.drawCard();
        Card secondDraw = deck.drawCard();
        
        assertEquals("Top Card 1", firstDraw.getName(), 
            "First card drawn should be first card added to top");
        assertEquals("Top Card 2", secondDraw.getName(), 
            "Second card drawn should be second card added to top");
    }
} 