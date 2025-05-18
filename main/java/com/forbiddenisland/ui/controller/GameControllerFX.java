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
            showErrorDialog("Invalid Card Type", "Only treasure cards can be given to other players.");
            return;
        }
        
        TreasureCard treasureCard = (TreasureCard) card;
        
        // Get the current player as giver
        Adventurer giver = gameController.getCurrentPlayer();
        
        // Double-check that giver has the card
        if (!giver.getTreasureCards().contains(treasureCard)) {
            // unusual case - may occur if UI is out of sync with game state
            showErrorDialog("Card Error", "You don't have this card to give!");
            return;
        }
        
        // Check if receiving player is null
        if (receiver == null) {
            showErrorDialog("Invalid Receiver", "No player selected to receive the card.");
            return;
        }
        
        // Disallow giving card to yourself 
        if (giver.equals(receiver)) {
            showErrorDialog("Invalid Receiver", "You cannot give a card to yourself!");
            return; 
        }
        
        // Use GiveCardAction to perform the transfer
        GiveCardAction giveCardAction = new GiveCardAction(gameController);
        boolean success = giveCardAction.execute(giver, receiver, treasureCard);
        
        if (success) {
            // Update UI components
            cardView.updatePlayerCards(gameController.getPlayers());
            
            // Use up an action
            gameController.useAction();
            
            // Show success message
            String cardName = treasureCard.getName();
            String receiverName = receiver.getName();
            
            // This prints to console for debugging
            System.out.println("Card transfer successful: " + cardName + " given to " + receiverName);
            
            showSuccessDialog("Card Given", "Successfully gave " + cardName + " to " + receiverName + ".");
        } else {
            // Show failure message with hint about possible reasons
            if (giver.getCurrentTile() != receiver.getCurrentTile() && 
                giver.getType() != com.forbiddenisland.enums.AdventurerType.MESSENGER) {
                
                showErrorDialog("Transfer Failed", 
                               "Players must be on the same tile to exchange cards (unless you're the Messenger).");
            } else {
                // Generic error - should rarely happen
                showErrorDialog("Transfer Failed", 
                               "Could not give the card. Check player positions and card ownership.");
            }
        }
    }

    public void handleCaptureTreasureAction() {
        // Get the current player
        Adventurer currentPlayer = gameController.getCurrentPlayer();
        if (currentPlayer == null) {
            showErrorDialog("Error", "No active player found");
            return;
        }
        
        // Check if player is on a treasure tile
        IslandTile currentTile = currentPlayer.getCurrentTile();
        if (currentTile == null) {
            showErrorDialog("Capture Treasure Error", "You are not on any tile!");
            return;
        }
        
        // See if the current tile has a treasure
        if (currentTile.getTreasure() == null) {
            showErrorDialog("Capture Treasure Error", 
                           "This tile doesn't contain a treasure site!");
            return;
        }
        
        // Create action and try to execute
        com.forbiddenisland.core.action.CaptureTreasureAction treasureAction = 
                new com.forbiddenisland.core.action.CaptureTreasureAction(gameController);
        
        try {
            boolean captureSuccess = treasureAction.execute(currentPlayer);
            
            if (captureSuccess) {
                // Update UI
                mapView.updateAllTiles();
                playerInfoView.updatePlayerInfo(gameController.getPlayers());
                cardView.updatePlayerCards(gameController.getPlayers());
                
                // Use an action
                gameController.useAction();
                
                // Show success message with captured treasure info
                String treasureName = currentTile.getTreasure().getDisplayName();
                showSuccessDialog("Treasure Captured!", 
                                 "You successfully captured the " + treasureName + "!");
                
                // Check if all treasures have been captured - game win condition
                if (gameController.areAllTreasuresCaptured()) {
                    // Maybe show special message or highlight escape route
                    System.out.println("All treasures captured! Head to Fools' Landing to escape!");
                }
            } else {
                // Show informative message about why it failed
                showErrorDialog("Capture Failed", 
                               "You need 4 matching treasure cards of " + 
                               currentTile.getTreasure().getDisplayName() + 
                               " to capture this treasure!");
            }
        } catch (Exception e) {
            // Something really went wrong
            System.err.println("Error capturing treasure: " + e.getMessage());
            showErrorDialog("System Error", "An unexpected error occurred: " + e.getMessage());
        }
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

    // Use a special action card (Helicopter Lift or Sandbags)
    // Can be used at any time, even during another player's turn!
    // player: The player using the card
    // card: The special action card to use
    // targetTiles: Target tiles (for Helicopter Lift: destination, for Sandbags: tile to shore up)
    // affectedPlayers: Players to move (for Helicopter Lift), can be null for Sandbags
    public void handleUseSpecialActionCard(Adventurer player, SpecialActionCard card, List<IslandTile> targetTiles, List<Adventurer> affectedPlayers) {
        // Check for null parameters
        if (card == null) {
            showErrorDialog("Card Error", "No special action card selected!");
            return;
        }
        
        if (player == null) {
            showErrorDialog("Player Error", "No player selected to use the card!");
            return;
        }

        // Make sure player actually has this card
        if (!player.getSpecialCards().contains(card)) {
            showErrorDialog("Invalid Card", "This player doesn't have this special card!");
            return;
        }
        
        try {
            switch (card.getCardType()) {
                case HELICOPTER_LIFT:
                    if (!handleHelicopterLift(player, card, targetTiles, affectedPlayers)) {
                        return; // Early return if helicopter lift failed
                    }
                    break;
                    
                case SANDBAGS:
                    if (!handleSandbags(player, card, targetTiles)) {
                        return; // Early return if sandbags failed
                    }
                    break;
                    
                case WATERS_RISE:
                    // Waters Rise cards are handled automatically when drawn
                    showErrorDialog("Card Error", "Waters Rise cards are played automatically when drawn!");
                    return;
                    
                default:
                    showErrorDialog("Unknown Card", "This special card type is not recognized!");
                    return;
            }
            
            // If we get here, card was successfully used
            // Update UI to reflect changes
            mapView.updateAllTiles();
            playerInfoView.updatePlayerInfo(gameController.getPlayers());
            cardView.updatePlayerCards(gameController.getPlayers());
            
        } catch (Exception e) {
            System.err.println("Error using special card: " + e.getMessage());
            e.printStackTrace();
            showErrorDialog("Card Error", "An error occurred while using this card: " + e.getMessage());
        }
    }
    
    // Helper method to handle Helicopter Lift special card
    private boolean handleHelicopterLift(Adventurer player, SpecialActionCard card, 
                                       List<IslandTile> targetTiles, List<Adventurer> affectedPlayers) {
        // Validate parameters
        if (targetTiles == null || targetTiles.isEmpty()) {
            showErrorDialog("Helicopter Error", "No destination selected for helicopter lift!");
            return false;
        }
        
        if (affectedPlayers == null || affectedPlayers.isEmpty()) {
            showErrorDialog("Helicopter Error", "No players selected to move with helicopter!");
            return false;
        }
        
        // Get the destination tile
        IslandTile destination = targetTiles.get(0);
        
        // Check if destination is valid (not sunk)
        if (destination.isSunk()) {
            showErrorDialog("Invalid Destination", "Cannot fly to a sunken tile!");
            return false;
        }
        
        // Move all selected players to the destination
        for (Adventurer adv : affectedPlayers) {
            adv.setCurrentTile(destination);
        }
        
        // Remove the card from player's hand
        player.removeSpecialCard(card);
        
        // Show success message with details
        StringBuilder playerNames = new StringBuilder();
        for (int i = 0; i < affectedPlayers.size(); i++) {
            if (i > 0) {
                playerNames.append(i == affectedPlayers.size() - 1 ? " and " : ", ");
            }
            playerNames.append(affectedPlayers.get(i).getName());
        }
        
        showSuccessDialog("Helicopter Lift", 
                         "Successfully transported " + playerNames.toString() + 
                         " to " + destination.getName() + "!");
        
        return true;
    }
    
    // Helper method to handle Sandbags special card
    private boolean handleSandbags(Adventurer player, SpecialActionCard card, List<IslandTile> targetTiles) {
        // Validate parameters
        if (targetTiles == null || targetTiles.isEmpty()) {
            showErrorDialog("Sandbags Error", "No tile selected to shore up!");
            return false;
        }
        
        // Get the tile to shore up
        IslandTile tileToShoreUp = targetTiles.get(0);
        
        // Check if tile is flooded (not normal or already sunk)
        if (!tileToShoreUp.isFlooded()) {
            if (tileToShoreUp.isSunk()) {
                showErrorDialog("Invalid Tile", "Cannot shore up a tile that has already sunk!");
            } else {
                showErrorDialog("Invalid Tile", "This tile is not flooded and doesn't need shoring up!");
            }
            return false;
        }
        
        // Shore up the tile
        tileToShoreUp.shoreUp();
        
        // Remove the card from player's hand
        player.removeSpecialCard(card);
        
        // Show success message
        showSuccessDialog("Sandbags Used", 
                         "Successfully shored up " + tileToShoreUp.getName() + "!");
        
        return true;
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