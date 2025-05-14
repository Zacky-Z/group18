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
        playerCardsBox.getChildren().add(new Label("玩家手牌"));
        
        treasureDeckBox = new VBox(10);
        treasureDeckBox.getChildren().add(new Label("宝物牌堆"));
        
        floodDeckBox = new VBox(10);
        floodDeckBox.getChildren().add(new Label("洪水牌堆"));
        
        getChildren().addAll(playerCardsBox, treasureDeckBox, floodDeckBox);
    }

    public void updatePlayerCards(List<Card> cards) {
        playerCardsBox.getChildren().clear();
        playerCardsBox.getChildren().add(new Label("玩家手牌"));
        
        for (Card card : cards) {
            ImageView cardImageView = new ImageView(); // 实际项目中应加载卡牌图片
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

    public void updateTreasureDeck(int deckSize, int discardSize) {
        treasureDeckBox.getChildren().clear();
        treasureDeckBox.getChildren().addAll(
            new Label("宝物牌堆"),
            new Label("剩余: " + deckSize),
            new Label("弃牌堆: " + discardSize)
        );
    }

    public void updateFloodDeck(int deckSize, int discardSize) {
        floodDeckBox.getChildren().clear();
        floodDeckBox.getChildren().addAll(
            new Label("洪水牌堆"),
            new Label("剩余: " + deckSize),
            new Label("弃牌堆: " + discardSize)
        );
    }
}    