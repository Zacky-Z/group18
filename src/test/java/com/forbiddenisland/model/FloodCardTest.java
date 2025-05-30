package com.forbiddenisland.model;

import com.forbiddenisland.enums.TileName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FloodCardTest {

    @Test
    void testFloodCardCreation() {
        String tileName = "Fools' Landing";
        FloodCard card = new FloodCard(tileName);
        
        assertEquals(tileName, card.getIslandTileName(), "Island tile name should match the provided name");
        assertEquals(tileName, card.getName(), "Card name should match the provided name");
    }

    @Test
    void testFloodCardDescription() {
        String tileName = "Fools' Landing";
        FloodCard card = new FloodCard(tileName);
        String expectedDescription = "Flood card for tile: " + tileName;
        assertEquals(expectedDescription, card.getDescription(), "Card description should match the expected format");
    }

    @Test
    void testDifferentFloodCards() {
        FloodCard card1 = new FloodCard("Fools' Landing");
        FloodCard card2 = new FloodCard("Temple of the Sun");
        
        assertNotEquals(card1.getIslandTileName(), card2.getIslandTileName(), 
            "Different flood cards should have different tile names");
        assertNotEquals(card1.getDescription(), card2.getDescription(), 
            "Different flood cards should have different descriptions");
    }
} 