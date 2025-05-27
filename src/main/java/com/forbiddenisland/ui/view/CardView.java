package com.forbiddenisland.ui.view;

import com.forbiddenisland.core.model.Card;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class CardView extends HBox {
    private VBox playerCardsBox;
    private VBox treasureDeckBox;
    private VBox floodDeckBox;

    public CardView() {
        initializeComponents();
    }

    private void initializeComponents() {
        setSpacing(20);
        setPadding(new javafx.geometry.Insets(10));
        
        playerCardsBox = new VBox(10);
        playerCardsBox.setPrefWidth(600);
        playerCardsBox.getChildren().add(new Label("Player Cards"));
        
        treasureDeckBox = new VBox(10);
        treasureDeckBox.getChildren().add(new Label("Treasure Deck"));
        
        floodDeckBox = new VBox(10);
        floodDeckBox.getChildren().add(new Label("Flood Deck"));
        
        getChildren().addAll(playerCardsBox, treasureDeckBox, floodDeckBox);
    }

    public void updatePlayerCards(List<Card> cards) {
        playerCardsBox.getChildren().clear();
        playerCardsBox.getChildren().add(new Label("Player Cards"));
        
        for (Card card : cards) {
            ImageView cardImageView = new ImageView(); // Should load card image in a real project
            cardImageView.setFitHeight(120);
            cardImageView.setFitWidth(80);
            cardImageView.setPreserveRatio(true);
            
            VBox cardBox = new VBox(5);
            cardBox.getChildren().addAll(
                cardImageView,
                new Label(card.getName())
            );
            
            playerCardsBox.getChildren().add(cardBox);
        }
    }

    public void updatePlayerHandsView(List<com.forbiddenisland.core.model.Adventurer> players) {
        playerCardsBox.getChildren().clear();
        playerCardsBox.getChildren().add(new Label("Player Cards"));
        
        if (players == null || players.isEmpty()) {
            playerCardsBox.getChildren().add(new Label("No players found"));
            return;
        }
        
        for (com.forbiddenisland.core.model.Adventurer player : players) {
            VBox playerSection = new VBox(5);
            playerSection.getChildren().add(new Label(player.getName() + "'s Cards:"));
            
            HBox cardsRow = new HBox(10);
            
            List<com.forbiddenisland.core.model.TreasureCard> treasureCards = player.getTreasureCards();
            if (treasureCards != null) {
                for (com.forbiddenisland.core.model.TreasureCard card : treasureCards) {
                    ImageView cardImage = new ImageView();
                    cardImage.setFitHeight(100);
                    cardImage.setFitWidth(70);
                    
                    VBox cardBox = new VBox(3);
                    cardBox.getChildren().addAll(
                        cardImage,
                        new Label(card.getName())
                    );
                    
                    cardsRow.getChildren().add(cardBox);
                }
            }
            
            List<com.forbiddenisland.core.model.SpecialActionCard> specialCards = player.getSpecialCards();
            if (specialCards != null) {
                for (com.forbiddenisland.core.model.SpecialActionCard card : specialCards) {
                    ImageView cardImage = new ImageView();
                    cardImage.setFitHeight(100);
                    cardImage.setFitWidth(70);
                    
                    VBox cardBox = new VBox(3);
                    cardBox.getChildren().addAll(
                        cardImage,
                        new Label(card.getName())
                    );
                    
                    cardsRow.getChildren().add(cardBox);
                }
            }
            
            playerSection.getChildren().add(cardsRow);
            playerCardsBox.getChildren().add(playerSection);
        }
    }

    public void updateTreasureDeck(int deckSize, int discardSize) {
        treasureDeckBox.getChildren().clear();
        treasureDeckBox.getChildren().addAll(
            new Label("Treasure Deck"),
            new Label("Remaining: " + deckSize),
            new Label("Discard Pile: " + discardSize)
        );
    }

    public void updateFloodDeck(int deckSize, int discardSize) {
        floodDeckBox.getChildren().clear();
        floodDeckBox.getChildren().addAll(
            new Label("Flood Deck"),
            new Label("Remaining: " + deckSize),
            new Label("Discard Pile: " + discardSize)
        );
    }
}    