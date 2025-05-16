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

        // 添加普通宝物卡牌
        for (TreasureType treasureType : TreasureType.values()) {
            for (int i = 0; i < 4; i++) {
                treasureDeck.add(new TreasureCard(treasureType));
            }
        }

        // 添加特殊行动卡牌
        treasureDeck.add(new SpecialActionCard(SpecialCardType.HELICOPTER_LIFT));
        treasureDeck.add(new SpecialActionCard(SpecialCardType.HELICOPTER_LIFT));
        treasureDeck.add(new SpecialActionCard(SpecialCardType.SANDBAGS));
        treasureDeck.add(new SpecialActionCard(SpecialCardType.SANDBAGS));
        treasureDeck.add(new SpecialActionCard(SpecialCardType.WATERS_RISE));
        treasureDeck.add(new SpecialActionCard(SpecialCardType.WATERS_RISE));

        // 洗牌
        Collections.shuffle(treasureDeck);
    }

    private void initializeFloodDeck() {
        floodDeck = new ArrayList<>();
        floodDiscardPile = new ArrayList<>();

        // 为每个陆地瓷砖创建洪水卡牌
        for (TileName tileName : TileName.values()) {
            floodDeck.add(new FloodCard(tileName));
        }

        // 洗牌
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