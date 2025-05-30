package com.forbiddenisland.core.action;

import com.forbiddenisland.core.model.Adventurer;
import com.forbiddenisland.core.model.TreasureCard;
import com.forbiddenisland.core.model.IslandTile;
import com.forbiddenisland.core.system.GameController;
import com.forbiddenisland.enums.AdventurerType;
import com.forbiddenisland.enums.TileName;
import com.forbiddenisland.enums.TreasureType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GiveCardActionTest {
    private GiveCardAction giveCardAction;
    private GameController gameController;
    private Adventurer giver;
    private Adventurer receiver;
    private TreasureCard card;
    private IslandTile tile1;
    private IslandTile tile2;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
        giveCardAction = new GiveCardAction(gameController);
        
        // Create test tiles
        tile1 = new IslandTile(TileName.TEMPLE_OF_THE_SUN, 0, 0);
        tile2 = new IslandTile(TileName.TEMPLE_OF_THE_MOON, 0, 1);

        // Create test adventurers
        giver = new Adventurer("Giver", AdventurerType.EXPLORER);
        receiver = new Adventurer("Receiver", AdventurerType.DIVER);
        
        // Set initial positions
        giver.setCurrentTile(tile1);
        receiver.setCurrentTile(tile1);

        // Create a test card and give it to the giver
        card = new TreasureCard(TreasureType.THE_EARTH_STONE);
        giver.addTreasureCard(card);
    }

    @Test
    void testGiveCardSuccessful() {
        // Test giving card when players are on the same tile
        boolean result = giveCardAction.execute(giver, receiver, card);
        
        assertTrue(result, "Card transfer should be successful");
        assertFalse(giver.getTreasureCards().contains(card), "Giver should no longer have the card");
        assertTrue(receiver.getTreasureCards().contains(card), "Receiver should now have the card");
    }

    @Test
    void testGiveCardDifferentTiles() {
        // Move receiver to a different tile
        receiver.setCurrentTile(tile2);
        
        // Try to give card when players are on different tiles
        boolean result = giveCardAction.execute(giver, receiver, card);
        
        assertFalse(result, "Card transfer should fail when players are on different tiles");
        assertTrue(giver.getTreasureCards().contains(card), "Giver should still have the card");
        assertFalse(receiver.getTreasureCards().contains(card), "Receiver should not have received the card");
    }

    @Test
    void testMessengerSpecialAbility() {
        // Create a Messenger adventurer
        Adventurer messenger = new Adventurer("Messenger", AdventurerType.MESSENGER);
        messenger.setCurrentTile(tile1);
        messenger.addTreasureCard(card);
        
        // Move receiver to a different tile
        receiver.setCurrentTile(tile2);
        
        // Test Messenger's ability to give cards regardless of location
        boolean result = giveCardAction.execute(messenger, receiver, card);
        
        assertTrue(result, "Messenger should be able to give cards regardless of location");
        assertFalse(messenger.getTreasureCards().contains(card), "Messenger should no longer have the card");
        assertTrue(receiver.getTreasureCards().contains(card), "Receiver should have received the card");
    }

    @Test
    void testGiveCardInvalidCases() {
        // Test with null giver
        assertFalse(giveCardAction.execute(null, receiver, card), 
            "Should fail when giver is null");

        // Test with null receiver
        assertFalse(giveCardAction.execute(giver, null, card), 
            "Should fail when receiver is null");

        // Test with null card
        assertFalse(giveCardAction.execute(giver, receiver, null), 
            "Should fail when card is null");

        // Test with card not in giver's hand
        TreasureCard invalidCard = new TreasureCard(TreasureType.THE_CRYSTAL_OF_FIRE);
        assertFalse(giveCardAction.execute(giver, receiver, invalidCard), 
            "Should fail when giver doesn't have the card");
    }
} 