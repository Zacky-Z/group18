package com.forbiddenisland.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpecialActionCardTest {

    @Test
    void testHelicopterLiftCard() {
        SpecialActionCard card = new HelicopterLiftCard();
        assertEquals("Helicopter Lift", card.getName(), 
            "Card name should be 'Helicopter Lift'");
        assertEquals("Helicopter Lift: Move any number of pawns to any tile on the board.", card.getDescription(),
            "Description should match expected value");
    }

    @Test
    void testSandbagsCard() {
        SpecialActionCard card = new SandbagsCard();
        assertEquals("Sandbag", card.getName(),
            "Card name should be 'Sandbag'");
        assertEquals("Sandbags: Use this card to shore up a tile on the island, preventing it from flooding.", card.getDescription(),
            "Description should match expected value");
    }

    @Test
    void testWatersRiseCard() {
        SpecialActionCard card = new WatersRiseCard();
        assertEquals("Waters Rise!", card.getName(),
            "Card name should be 'Water rise!'");
        assertEquals("Waters Rise! - Raise the water level by one step and shuffle the flood cards.", card.getDescription(),
            "Description should match expected value");
    }

    @Test
    void testDifferentCardDescriptions() {
        SpecialActionCard helicopterCard = new HelicopterLiftCard();
        SpecialActionCard sandbagsCard = new SandbagsCard();
        
        assertNotEquals(helicopterCard.getDescription(), sandbagsCard.getDescription(), 
            "Different special action cards should have different descriptions");
    }
} 