package com.forbiddenisland.core.system;

import com.forbiddenisland.core.model.Card;
import com.forbiddenisland.core.model.FloodCard;
import com.forbiddenisland.core.model.SpecialActionCard;
import com.forbiddenisland.core.model.TreasureCard;
import com.forbiddenisland.enums.SpecialCardType;
import com.forbiddenisland.enums.TileName;
import com.forbiddenisland.enums.TreasureType;

import java.util.*;

public class CardDeckManager {
    private List<TreasureCard> treasureDeck;
    private List<TreasureCard> treasureDiscardPile;
    private List<FloodCard> floodDeck;
    private List<FloodCard> floodDiscardPile;

    public CardDeckManager() {
        initializeTreasureDeck();
        initializeFloodDeck();
    }

    private void initializeTreasureDeck() {
        treasureDeck = new ArrayList<>();
        treasureDiscardPile = new ArrayList<>();

        // Add regular treasure cards
        for (TreasureType treasureType : TreasureType.values()) {
            for (int i = 0; i < 4; i++) {
                treasureDeck.add(new TreasureCard(treasureType));
            }
        }

        // Add special action cards
        treasureDeck.add(new SpecialActionCard(SpecialCardType.HELICOPTER_LIFT));
        treasureDeck.add(new SpecialActionCard(SpecialCardType.HELICOPTER_LIFT));
        treasureDeck.add(new SpecialActionCard(SpecialCardType.SANDBAGS));
        treasureDeck.add(new SpecialActionCard(SpecialCardType.SANDBAGS));
        treasureDeck.add(new SpecialActionCard(SpecialCardType.WATERS_RISE));
        treasureDeck.add(new SpecialActionCard(SpecialCardType.WATERS_RISE));

        // Shuffle the deck
        Collections.shuffle(treasureDeck);
    }

    private void initializeFloodDeck() {
        floodDeck = new ArrayList<>();
        floodDiscardPile = new ArrayList<>();

        // Create flood cards for each island tile
        for (TileName tileName : TileName.values()) {
            floodDeck.add(new FloodCard(tileName));
        }

        // Shuffle the deck
        Collections.shuffle(floodDeck);
    }

    public TreasureCard drawTreasureCard() {
        if (treasureDeck.isEmpty()) {
            reshuffleTreasureDiscardPile();
        }
        if (treasureDeck.isEmpty()) {
            return null;
        }
        Card card = treasureDeck.remove(0);
        if (card instanceof TreasureCard) {
            return (TreasureCard) card;
        } else {
            // Should not happen, but return null if not a TreasureCard
            return null;
        }
    }

    public void discardTreasureCard(TreasureCard card) {
        treasureDiscardPile.add(card);
    }

    public FloodCard drawFloodCard() {
        if (floodDeck.isEmpty()) {
            reshuffleFloodDiscardPile();
        }
        return floodDeck.isEmpty() ? null : floodDeck.remove(0);
    }

    public void discardFloodCard(FloodCard card) {
        floodDiscardPile.add(card);
    }

    public void reshuffleTreasureDiscardPile() {
        treasureDeck.addAll(treasureDiscardPile);
        treasureDiscardPile.clear();
        Collections.shuffle(treasureDeck);
    }

    public void reshuffleFloodDiscardPile() {
        floodDeck.addAll(floodDiscardPile);
        floodDiscardPile.clear();
        Collections.shuffle(floodDeck);
    }

    // Getters
    public int getTreasureDeckSize() { return treasureDeck.size(); }
    public int getTreasureDiscardPileSize() { return treasureDiscardPile.size(); }
    public int getFloodDeckSize() { return floodDeck.size(); }
    public int getFloodDiscardPileSize() { return floodDiscardPile.size(); }
}