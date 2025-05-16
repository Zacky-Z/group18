package com.forbiddenisland.ui.controller;

import com.forbiddenisland.core.action.GiveCardAction;
import com.forbiddenisland.core.model.*;
import com.forbiddenisland.core.system.GameController;
import com.forbiddenisland.ui.util.AssetLoader;
import com.forbiddenisland.ui.view.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class GameControllerFX {
    private final GameController gameController;
    private final Stage primaryStage;
    private MapView mapView;
    private PlayerInfoView playerInfoView;
    private CardView cardView;
    private GameMenu gameMenu;

    public GameControllerFX(GameController gameController, Stage primaryStage) {
        this.gameController = gameController;
        this.primaryStage = primaryStage;
        initializeUI();
    }

    private void initializeUI() {
        // 创建主界面布局
        BorderPane root = new BorderPane();
        
        // 初始化各个视图组件
        mapView = new MapView(gameController.getIslandTiles(), new AssetLoader());
        playerInfoView = new PlayerInfoView();
        cardView = new CardView();
        gameMenu = new GameMenu(this);
        
        // 设置布局
        root.setTop(gameMenu);
        root.setCenter(mapView);
        root.setRight(playerInfoView);
        root.setBottom(cardView);
        
        // 设置场景和舞台
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("禁闭岛");
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void startGame() {
        primaryStage.show();
    }

    // 处理各种用户操作的方法
    public void handleMoveAction(IslandTile targetTile) {
        // 处理移动动作
    }

    public void handleShoreUpAction(IslandTile targetTile) {
        // 处理加固动作
    }

    public void handleGiveCardAction(Adventurer receiver, Card card) {
        // Only TreasureCard can be given
        if (!(card instanceof TreasureCard)) {
            // Optionally show an error message in the UI
            return;
        }
        TreasureCard treasureCard = (TreasureCard) card;
        // Get the current player (giver)
        Adventurer giver = gameController.getPlayers().stream()
                .filter(a -> a.getTreasureCards().contains(treasureCard))
                .findFirst().orElse(null);
        if (giver == null) {
            // Optionally show an error message in the UI
            return;
        }
        // Use GiveCardAction to perform the transfer
        GiveCardAction giveCardAction = new GiveCardAction(gameController);
        boolean success = giveCardAction.execute(giver, receiver, treasureCard);
    }

    public void handleCaptureTreasureAction() {
        // 处理获取宝物动作
    }

    public void handleEndTurn() {
        // 处理结束回合
    }

    /**
     * Use a special action card (Helicopter Lift or Sandbags).
     * @param player The player using the card
     * @param card The special action card to use
     * @param targetTiles The target tiles (for Helicopter Lift: destination, for Sandbags: tile to shore up)
     * @param affectedPlayers The players to move (for Helicopter Lift), can be null for Sandbags
     */
    public void handleUseSpecialActionCard(Adventurer player, SpecialActionCard card, List<IslandTile> targetTiles, List<Adventurer> affectedPlayers) {
        if (card == null || player == null) return;
        switch (card.getCardType()) {
            case HELICOPTER_LIFT:
                // Move affected players to the destination tile
                if (targetTiles == null || targetTiles.isEmpty() || affectedPlayers == null || affectedPlayers.isEmpty()) return;
                IslandTile destination = targetTiles.get(0);
                for (Adventurer adv : affectedPlayers) {
                    adv.setCurrentTile(destination);
                }
                player.removeSpecialCard(card);
                // Optionally update UI or notify success
                break;
            case SANDBAGS:
                // Shore up the specified tile
                if (targetTiles == null || targetTiles.isEmpty()) return;
                IslandTile tileToShoreUp = targetTiles.get(0);
                tileToShoreUp.shoreUp();
                player.removeSpecialCard(card);
                // Optionally update UI or notify success
                break;
            default:
                // Other special cards (e.g., Waters Rise) are not used directly by players
                break;
        }
    }
}    