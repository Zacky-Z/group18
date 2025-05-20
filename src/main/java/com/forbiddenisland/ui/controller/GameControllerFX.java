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
            logEvent("MOVE", "Attempted move to null or sunken tile");
            showErrorDialog("Movement Error", "Cannot move to this location! The tile is unavailable.");
            return;
        }
        
        // get current player from gameController
        Adventurer currentPlayer = gameController.getCurrentPlayer();
        if (currentPlayer == null) {
            logEvent("ERROR", "Current player is null during move action");
            showErrorDialog("Player Error", "No active player found. Game state may be corrupted.");
            return;
        }
        
        // Create move action and check if movement is possible
        com.forbiddenisland.core.action.MoveAction moveAction = 
                new com.forbiddenisland.core.action.MoveAction(gameController);
        
        // Special confirmation for pilot flying (non-adjacent moves)
        IslandTile currentTile = currentPlayer.getCurrentTile();
        if (currentPlayer.getType() == com.forbiddenisland.enums.AdventurerType.PILOT &&
                !isAdjacentTile(currentTile, targetTile)) {
            
            boolean confirmFly = showConfirmDialog("Pilot Special Ability", 
                "Do you want to use your Pilot ability to fly directly to " + 
                targetTile.getName() + "? You can only use this ability once per turn.");
                
            if (!confirmFly) {
                logEvent("MOVE", "Pilot cancelled special flight");
                return;
            }
            
            logEvent("MOVE", "Pilot using special flight ability");
        }
        
        // Try to move
        boolean moveSuccess = moveAction.execute(currentPlayer, targetTile);
        
        if (moveSuccess) {
            // Log the successful move
            logEvent("MOVE", currentPlayer.getName() + " moved from " + 
                    (currentTile != null ? currentTile.getName() : "unknown") + 
                    " to " + targetTile.getName());
                    
            // update the UI after successful move
            mapView.updateAllTiles();  // redraw all tiles
            playerInfoView.updatePlayerInfo(gameController.getPlayers());
            
            // decrease player's remaining actions
            gameController.useAction();
            
            // Show feedback on remaining actions
            int actionsLeft = gameController.getTurnManager().getActionsRemaining();
            showSuccessDialog("Move Complete", 
                "Successfully moved to " + targetTile.getName() + ".\n" +
                "Actions remaining: " + actionsLeft);
        } else {
            // move failed, show detailed error
            logEvent("MOVE", "Failed move attempt to " + targetTile.getName());
            
            // Give more specific error feedback
            String errorReason = "";
            if (!isReachable(currentTile, targetTile)) {
                errorReason = "This location is not adjacent or reachable with your abilities.";
            } else if (targetTile.isSunk()) {
                errorReason = "This tile has sunk and cannot be moved to.";
            } else {
                errorReason = "You cannot move to that location due to game constraints.";
            }
            
            showErrorDialog("Invalid Move", errorReason);
        }
    }
    
    // Helper to determine if tiles are adjacent (simplified)
    private boolean isAdjacentTile(IslandTile a, IslandTile b) {
        if (a == null || b == null) return false;
        
        // Get coordinates (assuming tiles have getX() and getY() methods)
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        
        // Adjacent means they differ by at most 1 in either x or y but not both
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }
    
    // Helper to determine if a tile is reachable by the current player
    private boolean isReachable(IslandTile from, IslandTile to) {
        // This is a simplified implementation
        // In a real game, this would check adjacency, special abilities, etc.
        return isAdjacentTile(from, to);
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
            cardView.updatePlayerHandsView(gameController.getPlayers());
            
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
                cardView.updatePlayerHandsView(gameController.getPlayers());
                
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
            logEvent("ERROR", "GameController is null when ending turn");
            showErrorDialog("System Error", "Game controller not initialized. Please restart the game.");
            return;
    }

        // get current player info for ui updates
        Adventurer currentPlayer = gameController.getCurrentPlayer();
        if (currentPlayer == null) {
            logEvent("ERROR", "Current player is null when ending turn");
            showErrorDialog("Player Error", "No active player found. Game state may be corrupted.");
            return;
        }
        
        // Confirm the player wants to end their turn
        boolean confirmEnd = showConfirmDialog("End Turn", 
            "Are you sure you want to end " + currentPlayer.getName() + "'s turn? " +
            "You won't be able to take any more actions this turn.");
            
        if (!confirmEnd) {
            logEvent("INFO", "Player cancelled ending turn");
            return; // Player chose not to end their turn
        }
        
        try {
            logEvent("TURN", currentPlayer.getName() + " is ending their turn");
            
            // first draw 2 treasure cards for player
            gameController.drawTreasureCards();
            logEvent("CARDS", "Drew treasure cards for " + currentPlayer.getName());
            
            // update cards view to show new cards
            cardView.updatePlayerHandsView(gameController.getPlayers());
            
            // draw flood cards according to water meter level
            logEvent("FLOOD", "Drawing flood cards at level " + 
                    gameController.getWaterMeter().getCurrentLevel());
            gameController.drawFloodCards();
            
            // update island view to show flooding changes
            mapView.updateAllTiles();
            
            // check for game over conditions with detailed feedback
            if (gameController.isGameOver()) {
                if (gameController.isGameWon()) {
                    logEvent("GAME", "Players have won the game!");
                    // Show detailed victory stats instead of simple message
                    displayVictoryInfo();
                } else {
                    String lossReason = determineLossReason();
                    logEvent("GAME", "Game over. Reason: " + lossReason);
                    showErrorDialog("Game Over", 
                        "The expedition has failed. " + lossReason +
                        "\n\nBetter luck next time!");
                }
                return;
            }
            
            // move to next player's turn if game is still active
            startNextTurn();
            
            // Get new current player after advancing turn
            Adventurer nextPlayer = gameController.getCurrentPlayer();
            logEvent("TURN", "Turn ended. It's now " + nextPlayer.getName() + "'s turn");
            
            // update player info UI to show new active player
            playerInfoView.updatePlayerInfo(gameController.getPlayers());
            
            // Show helpful message for next player
            showSuccessDialog("Next Turn", 
                "It's now " + nextPlayer.getName() + "'s turn!\n" +
                "You have 3 actions available.");
            
        } catch (Exception e) {
            // something went wrong - log and show error
            logEvent("ERROR", "Error during end turn: " + e.getMessage());
            e.printStackTrace();
            showErrorDialog("Turn Error", 
                "An error occurred while ending the turn: " + e.getMessage() + 
                "\n\nPlease check the console for details.");
        }
    }
    
    // helper method to determine why the game was lost
    private String determineLossReason() {
        // Let's check specific loss conditions in order of priority
        
        // Check if water level is at maximum
        if (gameController.getWaterMeter().isAtMaxLevel()) {
            return "The water level has reached its maximum! The island is completely submerged.";
        }
        
        // Find Fools' Landing and check if it's sunk
        IslandTile foolsLanding = null;
        for (IslandTile tile : gameController.getIslandTiles()) {
            if ("Fools' Landing".equals(tile.getName())) {
                foolsLanding = tile;
                break;
            }
        }
        
        if (foolsLanding != null && foolsLanding.isSunk()) {
            return "Fools' Landing has sunk! There's no way to escape the island.";
        }
        
        // Check if all tiles for a specific treasure have sunk
        java.util.Map<com.forbiddenisland.enums.TreasureType, Integer> remainingSites = 
                countRemainingTreasureSites();
                
        for (com.forbiddenisland.enums.TreasureType treasureType : remainingSites.keySet()) {
            if (remainingSites.get(treasureType) == 0) {
                return "All sites for the " + treasureType.getDisplayName() + 
                       " have sunk, making it impossible to collect this treasure.";
            }
        }
        
        // Check if any player is on a sunken tile with no adjacent tiles to move to
        for (Adventurer player : gameController.getPlayers()) {
            IslandTile playerTile = player.getCurrentTile();
            if (playerTile != null && playerTile.isSunk()) {
                // Check if there's any adjacent non-sunken tile
                boolean canEscape = false;
                
                // Special case for Diver who can move through sunken tiles
                if (player.getType() == com.forbiddenisland.enums.AdventurerType.DIVER) {
                    canEscape = true; // Diver can almost always escape
                } else {
                    // Check adjacent tiles
                    for (IslandTile tile : gameController.getIslandTiles()) {
                        if (isAdjacent(playerTile, tile) && !tile.isSunk()) {
                            canEscape = true;
                            break;
                        }
                    }
                }
                
                if (!canEscape) {
                    return player.getName() + " has drowned as their tile sank with no adjacent tiles to escape to.";
                }
            }
        }
        
        // Default case if we can't determine the specific reason
        return "The island has sunk too much to complete your mission.";
    }
    
    // helper method to check if two tiles are adjacent
    private boolean isAdjacent(IslandTile a, IslandTile b) {
        if (a == null || b == null) return false;
        
        int ax = a.getX();
        int ay = a.getY();
        int bx = b.getX();
        int by = b.getY();
        
        // Tiles are adjacent if they differ by 1 in one coordinate and match in the other
        return (Math.abs(ax - bx) == 1 && ay == by) || (ax == bx && Math.abs(ay - by) == 1);
    }
    
    // helper method to count remaining non-sunken sites for each treasure type
    private java.util.Map<com.forbiddenisland.enums.TreasureType, Integer> countRemainingTreasureSites() {
        java.util.Map<com.forbiddenisland.enums.TreasureType, Integer> siteCounts = 
                new java.util.HashMap<>();
                
        // Initialize counts to 0
        for (com.forbiddenisland.enums.TreasureType type : com.forbiddenisland.enums.TreasureType.values()) {
            siteCounts.put(type, 0);
        }
        
        // Count non-sunken sites for each treasure type
        for (IslandTile tile : gameController.getIslandTiles()) {
            if (!tile.isSunk() && tile.getTreasure() != null) {
                com.forbiddenisland.enums.TreasureType tileType = tile.getTreasure();
                siteCounts.put(tileType, siteCounts.get(tileType) + 1);
            }
        }
        
        return siteCounts;
    }

    // helper method to display victory information with detailed stats
    private void displayVictoryInfo() {
        // Calculate some game statistics for the victory screen
        int turnsPlayed = calculateTurnsPlayed();
        int tilesRemaining = countRemainingTiles();
        int waterLevel = gameController.getWaterMeter().getCurrentLevel();
        java.util.List<String> capturedTreasures = getCapturedTreasureNames();
        
        StringBuilder victoryMessage = new StringBuilder();
        victoryMessage.append("Congratulations! Your team has escaped Forbidden Island!\n\n");
        victoryMessage.append("===== Mission Statistics =====\n");
        victoryMessage.append("Turns played: ").append(turnsPlayed).append("\n");
        victoryMessage.append("Remaining tiles: ").append(tilesRemaining).append(" out of ").append(gameController.getIslandTiles().size()).append("\n");
        victoryMessage.append("Final water level: ").append(waterLevel).append("\n\n");
        
        victoryMessage.append("Treasures captured:\n");
        for (String treasure : capturedTreasures) {
            victoryMessage.append("- ").append(treasure).append("\n");
        }
        
        victoryMessage.append("\nThe team successfully escaped before the island sank completely!\n");
        victoryMessage.append("Would you like to play again?");
        
        // Log victory info
        logEvent("VICTORY", "Players won the game after " + turnsPlayed + " turns with water level " + waterLevel);
        
        // Show the message with option to play again
        boolean playAgain = showConfirmDialog("Victory!", victoryMessage.toString());
        
        if (playAgain) {
            // TODO: implement game restart logic
            System.out.println("Starting new game...");
        } else {
            // Exit game or return to main menu
            System.out.println("Returning to main menu...");
        }
    }
    
    // helper methods for victory stats
    
    private int calculateTurnsPlayed() {
        // This would ideally come from the game state
        // For now, return a placeholder value
        return 12; // Placeholder
    }
    
    private int countRemainingTiles() {
        int count = 0;
        for (IslandTile tile : gameController.getIslandTiles()) {
            if (!tile.isSunk()) {
                count++;
            }
        }
        return count;
    }
    
    private java.util.List<String> getCapturedTreasureNames() {
        java.util.List<String> treasureNames = new java.util.ArrayList<>();
        
        for (com.forbiddenisland.enums.TreasureType type : com.forbiddenisland.enums.TreasureType.values()) {
            // Check if this treasure has been captured
            boolean captured = false;
            
            for (Adventurer player : gameController.getPlayers()) {
                for (Treasure treasure : player.getCapturedFigurines()) {
                    if (treasure.getType() == type) {
                        captured = true;
                        break;
                    }
                }
                if (captured) break;
            }
            
            if (captured) {
                treasureNames.add(type.getDisplayName());
            }
        }
        
        return treasureNames;
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
            cardView.updatePlayerHandsView(gameController.getPlayers());
            
        } catch (Exception e) {
            System.err.println("Error using special card: " + e.getMessage());
            e.printStackTrace();
            showErrorDialog("Card Error", "An error occurred while using this card: " + e.getMessage());
        }
    }
    
    // helper method to handle Helicopter Lift special card
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
    
    // helper method to show confirmation dialogs
    private boolean showConfirmDialog(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // add Yes and No buttons (default are OK and Cancel)
        javafx.scene.control.ButtonType yesButton = new javafx.scene.control.ButtonType("Yes");
        javafx.scene.control.ButtonType noButton = new javafx.scene.control.ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        
        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        
        // return true if user clicked Yes, false otherwise
        return result.isPresent() && result.get() == yesButton;
    }
    
    // logs game events to console (could be extended to write to file)
    private void logEvent(String eventType, String message) {
        // get current timestamp for the log
        String timestamp = java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        // construct log msg with timestamps
        String logMsg = "[" + timestamp + "] " + eventType + ": " + message;
        
        // currently just output to console, but could be extended
        System.out.println(logMsg);
        
        // TODO: in a real game, we might want to add file logging
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