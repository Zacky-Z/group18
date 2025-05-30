package com.forbiddenisland.model;

import com.forbiddenisland.model.TreasureType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TreasureCardTest {

    @Test
    void testTreasureCardCreation() {
        String cardName = "Earth Stone";
        TreasureType treasureType = TreasureType.THE_EARTH_STONE;
        TreasureCard card = new TreasureCard(cardName, treasureType);
        
        assertEquals(cardName, card.getName(), "Card name should match the provided name");
        assertEquals(treasureType, card.getTreasureType(), "Treasure type should match the provided type");
    }

    @Test
    void testTreasureCardDescription() {
        TreasureCard card = new TreasureCard("Earth Stone", TreasureType.THE_EARTH_STONE);
        String expectedDescription = "Treasure card:" + TreasureType.THE_EARTH_STONE.getDisplayName();
        assertEquals(expectedDescription, card.getDescription(), "Card description should match the expected format");
    }

    @Test
    void testDifferentTreasureTypes() {
        // Test card creation with different treasure types
        TreasureCard earthCard = new TreasureCard("Earth Stone", TreasureType.THE_EARTH_STONE);
        TreasureCard fireCard = new TreasureCard("Fire Crystal", TreasureType.THE_CRYSTAL_OF_FIRE);
        
        assertNotEquals(earthCard.getTreasureType(), fireCard.getTreasureType(), 
            "Different treasure cards should have different treasure types");
    }
} 