package com.forbiddenisland.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    
    // Test class to verify basic Card functionality
    private static class TestCard extends Card {
        public TestCard(String name) {
            super(name);
        }

        @Override
        public String getDescription() {
            return "Test card description";
        }
    }

    @Test
    void testCardCreation() {
        String cardName = "Test Card";
        Card card = new TestCard(cardName);
        assertEquals(cardName, card.getName(), "Card name should match the provided name");
    }

    @Test
    void testCardDescription() {
        Card card = new TestCard("Test Card");
        assertEquals("Test card description", card.getDescription(), "Card description should match the expected value");
    }
} 