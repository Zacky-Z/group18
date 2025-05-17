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
        if (targetTile == null || targetTile.isSunk()) {
            showErrorDialog("Movement Error", "Cannot move to this location!");
            return;
        }
        
        // get current player from gameController
        Adventurer currentPlayer = gameController.getCurrentPlayer();
        if (currentPlayer == null) {
            // this is so weird, should nvr happen
            return;
        }
        
        // creat a new move action obj and try to move
        com.forbiddenisland.core.action.MoveAction moveAction = 
                new com.forbiddenisland.core.action.MoveAction(gameController);
        
        boolean moveSuccess = moveAction.execute(currentPlayer, targetTile);
        
        if (moveSuccess) {
            // update the UI after successful move
            mapView.updateAllTiles();  // redraw all tiles
            playerInfoView.updatePlayerInfo(gameController.getPlayers());
            
            // decrease player's remaining actions
            gameController.useAction();
            
            // show a success msg maybe?
            System.out.println("Player moved to " + targetTile.getName());
        } else {
            // move failed, show error
            showErrorDialog("Invalid Move", "You cannot move to that location!");
        }
    }

    public void handleShoreUpAction(IslandTile targetTile) {
        // should not happen but let's check it
        if (targetTile == null) {
            showErrorDialog("Shore Up Error", "No tile selected for shoring up");
            return;
        }
        
        Adventurer currPlayer = gameController.getCurrentPlayer();
        
        // check if the player is null (shouldn't happen)
        if (currPlayer == null) {
            System.err.println("Error: Current player is null!");
            return; //abort operation
        }
        
        // make sure the tile is actually flooded - can't shore up normal or sunk tiles
        if (!targetTile.isFlooded()) {
            showErrorDialog("Shore Up Error", "This tile cannot be shored up - it's not flooded!");
            return;
        }
        
        // create shore up action instance
        com.forbiddenisland.core.action.ShoreUpAction shoreUpAction = 
                new com.forbiddenisland.core.action.ShoreUpAction(gameController);
        
        // try to shore up!
        boolean shoreUpSuccessful = shoreUpAction.execute(currPlayer, targetTile);
        
        if (shoreUpSuccessful) {
            // succeeded - update ui
            mapView.updateAllTiles();
            
            // subtract one of player's actions
            gameController.useAction();
            
            // print something to console
            System.out.println(currPlayer.getName() + " shored up " + targetTile.getName());
            
            // check if engineer has a second shore up
            if (shoreUpAction.canShoreUpAgain(currPlayer)) {
                // TODO: prompt user for second shore up location
                System.out.println("Engineer can shore up a second tile!");
            }
        } else {
            // Couldn't shore up for some reason
            showErrorDialog("Shore Up Failed", "You cannot shore up this location. Make sure it's adjacent or you have special abilities.");
        }
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
        // Safety check - make sure we have a valid game state
        if (gameController == null) {
            return;
        }
        
        // get current player info for ui updates
        Adventurer currentPlayer = gameController.getCurrentPlayer();
        
        try {
            // first draw 2 treasure cards for player
            gameController.drawTreasureCards();
            
            // update cards view to show new cards
            cardView.updatePlayerCards(gameController.getPlayers());
            
            // draw flood cards according to water meter level
            gameController.drawFloodCards();
            
            // update island view to show flooding changes
            mapView.updateAllTiles();
            
            // check for game over conditions - we could add special screen later
            if (gameController.isGameOver()) {
                if (gameController.isGameWon()) {
                    showSuccessDialog("Victory!", "You have won the game by collecting all treasures and escaping the island!");
                } else {
                    showErrorDialog("Game Over", "The island has sunk or another losing condition has occurred.");
                }
                return;
            }
            
            // move to next player's turn if game is still active
            startNextTurn();
            
            // log to console for debugging
            System.out.println("Turn ended. It's now " + gameController.getCurrentPlayer().getName() + "'s turn");
            
            // update player info UI to show new active player
            playerInfoView.updatePlayerInfo(gameController.getPlayers());
            
        } catch (Exception e) {
            // something went wrong - log and show error
            System.err.println("Error during end turn: " + e.getMessage());
            e.printStackTrace();
            showErrorDialog("Turn Error", "An error occurred while ending the turn: " + e.getMessage());
        }
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
    
    // helper method to show error dialogs
    private void showErrorDialog(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);  // no header
        alert.setContentText(message);
        alert.showAndWait();  // wait for user to click OK
    }
    
    // helper method to show success dialogs
    private void showSuccessDialog(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /*
     * Start next player's turn by updating game state
     */
    private void startNextTurn() {
        // ask the turn manager to move to next player
        com.forbiddenisland.core.system.TurnManager turnManager = gameController.getTurnManager();
        if (turnManager != null) {
            turnManager.startNextTurn();
            
            // reset ability trackers for new turn
            resetPlayerAbilities();
        } else {
            System.err.println("WARNING: TurnManager is null, can't start next turn!");
        }
    }
    
    // reset player special abilities for new turn
    private void resetPlayerAbilities() {
        // this is needed to reset stuff like pilot flying or engineer double shore-up
        // TODO: implement properly with ability trackers
        System.out.println("Resetting player abilities for new turn");
    }
}    