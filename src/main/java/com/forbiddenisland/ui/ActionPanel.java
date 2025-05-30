package com.forbiddenisland.ui;

import com.forbiddenisland.ForbiddenIslandGame;
import com.forbiddenisland.model.*;
import com.forbiddenisland.model.Card;
import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.Game.GamePhase;
import com.forbiddenisland.model.Player;
import com.forbiddenisland.model.IslandTile;
import com.forbiddenisland.model.AdventurerRole;
import com.forbiddenisland.model.TreasureCard;
import com.forbiddenisland.model.TreasureType;
import com.forbiddenisland.model.SpecialActionCard;
import com.forbiddenisland.model.SandbagsCard;
import com.forbiddenisland.model.HelicopterLiftCard;
import com.forbiddenisland.model.Treasure;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.StringBuilder;
import javafx.application.Platform;

/**
 * Game Action Panel
 * Displays and manages available actions for the current player
 * Part of the GUI integration work (Week 12)
 * Core component for turn management system (Week 13)
 */
public class ActionPanel extends VBox {

    // Core components for game action management
    private Game game;
    private ForbiddenIslandGame mainApp;
    private GameBoardView gameBoardView;

    // UI Components for action display and control
    private Label actionPointsLabel;
    private Button moveButton;
    private Button shoreUpButton;
    private Button giveCardButton;
    private Button captureTreasureButton;
    private Button specialActionButton;
    private Button endActionsAndDrawTreasureButton;
    private Button drawFloodCardsButton;
    private VBox actionButtonsBox;
    private boolean isProcessingAction = false; // Action processing lock flag

    /**
     * Constructor for ActionPanel
     * Initializes the panel with game actions and turn management controls
     *
     * @param game    The current game instance
     * @param mainApp The main application instance
     */
    public ActionPanel(Game game, ForbiddenIslandGame mainApp) {
        this.game = game;
        this.mainApp = mainApp;

        // Setup panel layout and styling
        setPadding(new Insets(10));
        setSpacing(10);
        setPrefWidth(280);
        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999; -fx-border-width: 1px;");

        // Initialize panel components
        Label titleLabel = new Label("Action Panel");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        getChildren().add(titleLabel);

        // Setup action points display
        actionPointsLabel = new Label("Actions Remaining: " + game.getActionsRemainingInTurn());
        actionPointsLabel.setFont(Font.font("Arial", 14));
        getChildren().add(actionPointsLabel);

        // Create action buttons container
        actionButtonsBox = new VBox(8);
        actionButtonsBox.setPadding(new Insets(5));
        getChildren().add(actionButtonsBox);

        createActionButtons();
    }

    private void createActionButtons() {
        // Button styling
        String buttonStyle = "-fx-background-color: linear-gradient(#f2f2f2, #d6d6d6); " +
                "-fx-background-radius: 5; -fx-padding: 8 15; -fx-font-size: 13px;";
        String phaseButtonStyle = "-fx-background-color: linear-gradient(#61a2b1, #2A5058); " +
                "-fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 5; -fx-padding: 8 15; -fx-font-size: 13px;";

        moveButton = new Button("Move");
        moveButton.setMaxWidth(Double.MAX_VALUE);
        moveButton.setStyle(buttonStyle);
        moveButton.setOnAction(e -> handleMoveAction());

        shoreUpButton = new Button("Shore Up");
        shoreUpButton.setMaxWidth(Double.MAX_VALUE);
        shoreUpButton.setStyle(buttonStyle);
        shoreUpButton.setOnAction(e -> handleShoreUpAction());

        giveCardButton = new Button("Give Card");
        giveCardButton.setMaxWidth(Double.MAX_VALUE);
        giveCardButton.setStyle(buttonStyle);
        giveCardButton.setOnAction(e -> handleGiveCardAction());

        captureTreasureButton = new Button("Capture Treasure");
        captureTreasureButton.setMaxWidth(Double.MAX_VALUE);
        captureTreasureButton.setStyle(buttonStyle);
        captureTreasureButton.setOnAction(e -> handleCaptureTreasureAction());

        specialActionButton = new Button("Special Ability");
        specialActionButton.setMaxWidth(Double.MAX_VALUE);
        specialActionButton.setStyle(buttonStyle);
        specialActionButton.setOnAction(e -> handleSpecialAction());

        endActionsAndDrawTreasureButton = new Button("End Actions / Draw Treasure");
        endActionsAndDrawTreasureButton.setMaxWidth(Double.MAX_VALUE);
        endActionsAndDrawTreasureButton.setStyle(phaseButtonStyle);
        endActionsAndDrawTreasureButton.setOnAction(e -> handleEndActionsAndDrawTreasure());

        drawFloodCardsButton = new Button("Draw Flood Cards");
        drawFloodCardsButton.setMaxWidth(Double.MAX_VALUE);
        drawFloodCardsButton.setStyle(phaseButtonStyle);
        drawFloodCardsButton.setDisable(true);
        drawFloodCardsButton.setOnAction(e -> handleDrawFloodCards());

        // Add buttons to panel
        actionButtonsBox.getChildren().addAll(
                moveButton,
                shoreUpButton,
                giveCardButton,
                captureTreasureButton,
                specialActionButton,
                new Separator(),
                endActionsAndDrawTreasureButton,
                drawFloodCardsButton
        );
    }

    private void handleMoveAction() {
        if (!canPerformAction()) return;
        Player currentPlayer = game.getCurrentPlayer();
        Set<IslandTile> validMoves = currentPlayer.getValidMoves(game);

        if (validMoves.isEmpty()) {
            System.out.println(currentPlayer.getName() + " has no valid move targets!");
            showMessage(currentPlayer.getName() + " has no valid move targets!");
            return;
        }

        System.out.println(currentPlayer.getName() + " selecting move target...");
        gameBoardView.highlightTiles(validMoves, Color.BLUE);

        gameBoardView.setTileSelectionCallback(selectedTile -> {
            gameBoardView.clearSelectionHighlights();
            gameBoardView.setTileSelectionCallback(null);

            if (validMoves.contains(selectedTile)) {
                if (game.spendAction()) {
                    boolean wasPilotFlight = false;
                    if (currentPlayer.getRole() == AdventurerRole.PILOT &&
                            !currentPlayer.isPilotAbilityUsedThisTurn()) {
                        int[] currentCoords = game.getTileCoordinates(currentPlayer.getCurrentLocation());
                        if (currentCoords != null) {
                            List<IslandTile> adjacentTiles = game.getValidAdjacentTiles(currentCoords[0], currentCoords[1], true);
                            if (!adjacentTiles.contains(selectedTile)) {
                                wasPilotFlight = true;
                            }
                        }
                    }
                    currentPlayer.moveTo(selectedTile);
                    String message;
                    if (wasPilotFlight) {
                        currentPlayer.setPilotAbilityUsedThisTurn(true);
                        message = currentPlayer.getName() + " flew to " + selectedTile.getName();
                    } else {
                        message = currentPlayer.getName() + " moved to " + selectedTile.getName();
                    }
                    System.out.println(message);
                    mainApp.updateGameState();
                } else {
                    String message = currentPlayer.getName() + " failed to move: no action points left.";
                    System.out.println(message);
                    showMessage(message);
                }
            } else {
                String message = currentPlayer.getName() + " invalid move selection. Please choose from highlighted tiles.";
                System.out.println(message);
                showMessage(message);
            }
        });
    }

    private void handleShoreUpAction() {
        if (!canPerformAction()) return;
        Player currentPlayer = game.getCurrentPlayer();
        Set<IslandTile> validShoreUpTiles = getValidShoreUpTiles(currentPlayer);

        if (validShoreUpTiles.isEmpty()) {
            String message = currentPlayer.getName() + " has no tiles to shore up nearby!";
            System.out.println(message);
            showMessage(message);
            return;
        }

        System.out.println(currentPlayer.getName() + " selecting tile to shore up...");
        gameBoardView.highlightTiles(validShoreUpTiles, Color.ORANGE);

        gameBoardView.setTileSelectionCallback(selectedTile -> {
            gameBoardView.clearSelectionHighlights();
            gameBoardView.setTileSelectionCallback(null);

            if (validShoreUpTiles.contains(selectedTile)) {
                if (game.spendAction()) {
                    selectedTile.shoreUp();
                    if (currentPlayer.getRole() == AdventurerRole.ENGINEER) {
                        String message = currentPlayer.getName() + " (Engineer) shored up " + selectedTile.getName() + ", can choose to shore up another tile...";
                        System.out.println(message);
                        Set<IslandTile> remainingShoreUpTiles = getValidShoreUpTiles(currentPlayer);
                        remainingShoreUpTiles.remove(selectedTile);
                        if (!remainingShoreUpTiles.isEmpty()) {
                            gameBoardView.highlightTiles(remainingShoreUpTiles, Color.ORANGE);
                            gameBoardView.setTileSelectionCallback(secondTile -> {
                                gameBoardView.clearSelectionHighlights();
                                gameBoardView.setTileSelectionCallback(null);
                                String finalMessage;
                                if (remainingShoreUpTiles.contains(secondTile)) {
                                    secondTile.shoreUp();
                                    finalMessage = currentPlayer.getName() + " (Engineer) shored up " + selectedTile.getName() + " and " + secondTile.getName();
                                } else {
                                    finalMessage = currentPlayer.getName() + " shored up " + selectedTile.getName() + " but skipped second shore up opportunity";
                                }
                                System.out.println(finalMessage);
                                showMessage(finalMessage);
                                mainApp.updateGameState();
                            });
                        } else {
                            String finalMessage = currentPlayer.getName() + " shored up " + selectedTile.getName() + ", no more tiles to shore up";
                            System.out.println(finalMessage);
                            showMessage(finalMessage);
                            mainApp.updateGameState();
                        }
                    } else {
                        String message = currentPlayer.getName() + " shored up " + selectedTile.getName();
                        System.out.println(message);
                        showMessage(message);
                        mainApp.updateGameState();
                    }
                } else {
                    String message = currentPlayer.getName() + " failed to shore up: no action points left.";
                    System.out.println(message);
                    showMessage(message);
                }
            } else {
                String message = currentPlayer.getName() + " invalid shore up selection. Please choose from highlighted tiles.";
                System.out.println(message);
                showMessage(message);
            }
        });
    }

    private Set<IslandTile> getValidShoreUpTiles(Player player) {
        Set<IslandTile> validTiles = new HashSet<>();
        if (player == null || game == null) return validTiles;
        IslandTile currentLocation = player.getCurrentLocation();
        if (currentLocation == null) return validTiles;
        int[] currentCoords = game.getTileCoordinates(currentLocation);
        if (currentCoords == null) return validTiles;

        if (currentLocation.isFlooded()) {
            validTiles.add(currentLocation);
        }

        boolean canShoreUpDiagonally = player.getRole().canShoreUpDiagonally();
        int r = currentCoords[0];
        int c = currentCoords[1];
        int[] dr = {-1, 1, 0, 0, -1, -1, 1, 1};
        int[] dc = {0, 0, -1, 1, -1, 1, -1, 1};
        int limit = canShoreUpDiagonally ? 8 : 4;

        for (int i = 0; i < limit; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];
            if (game.isValidBoardCoordinate(nr, nc)) {
                IslandTile adjacentTile = game.getGameBoard()[nr][nc];
                if (adjacentTile != null && adjacentTile.isFlooded()) {
                    validTiles.add(adjacentTile);
                }
            }
        }
        return validTiles;
    }

    private void handleCaptureTreasureAction() {
        if (!canPerformAction()) return;
        Player currentPlayer = game.getCurrentPlayer();
        IslandTile currentLocation = currentPlayer.getCurrentLocation();

        if (currentLocation == null) {
            String message = currentPlayer.getName() + " current location is invalid, cannot capture treasure";
            System.out.println(message);
            showMessage(message);
            return;
        }

        TreasureType treasure = currentLocation.getAssociatedTreasure();
        if (treasure == null) {
            String message = currentPlayer.getName() + " current tile has no treasure!";
            System.out.println(message);
            showMessage(message);
            return;
        }

        int requiredCards = currentPlayer.getRole().getTreasureCardsNeededForCapture();
        int treasureCardCount = 0;

        for (Card card : currentPlayer.getHand()) {
            if (card instanceof TreasureCard) {
                TreasureCard treasureCard = (TreasureCard) card;
                if (treasureCard.getTreasureType() == treasure) {
                    treasureCardCount++;
                }
            }
        }

        if (treasureCardCount >= requiredCards) {
            if (game.spendAction()) {
                if (currentPlayer.captureTreasure(treasure, game.getTreasureDeck())) {
                    // Update treasure status
                    for (Treasure t : game.getTreasures()) {
                        if (t.getType() == treasure) {
                            t.setCollected();
                            break;
                        }
                    }
                    String message = currentPlayer.getName() + " captured the treasure: " + treasure.getDisplayName() + "!";
                    System.out.println(message);
                    showMessage(message);
                    mainApp.updateGameState();
                }
            } else {
                String message = currentPlayer.getName() + " failed to capture treasure: no action points left.";
                System.out.println(message);
                showMessage(message);
            }
        } else {
            String message = currentPlayer.getName() + " needs " + requiredCards + " " + treasure.getDisplayName() + " treasure cards to capture! Currently has " + treasureCardCount + ".";
            System.out.println(message);
            showMessage(message);
        }
    }

    private void handleSpecialAction() {
        Player currentPlayer = game.getCurrentPlayer();

        // Handle Navigator special ability
        if (currentPlayer.getRole() == AdventurerRole.NAVIGATOR) {
            handleNavigatorSpecialAbility();
            return;
        }

        // Handle special action cards
        List<Card> specialCards = new ArrayList<>();
        for (Card card : currentPlayer.getHand()) {
            if (card instanceof SpecialActionCard) {
                specialCards.add(card);
            }
        }
        if (specialCards.isEmpty()) {
            showMessage(currentPlayer.getName() + " has no special action cards!");
            return;
        }

        Dialog<Card> dialog = new Dialog<>();
        dialog.setTitle("Select Special Action Card");
        dialog.setHeaderText("Choose a special action card to use");
        ButtonType useButtonType = new ButtonType("Use", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(useButtonType, cancelButtonType);
        ListView<Card> cardListView = new ListView<>();
        cardListView.getItems().addAll(specialCards);
        cardListView.setCellFactory(param -> new ListCell<Card>() {
            @Override
            protected void updateItem(Card card, boolean empty) {
                super.updateItem(card, empty);
                if (empty || card == null) {
                    setText(null);
                } else {
                    setText(card.getName() + " - " + card.getDescription());
                }
            }
        });
        dialog.getDialogPane().setContent(cardListView);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == useButtonType) {
                return cardListView.getSelectionModel().getSelectedItem();
            }
            return null;
        });
        dialog.showAndWait().ifPresent(selectedCard -> {
            if (selectedCard instanceof SandbagsCard) {
                useSandbagsCard(currentPlayer, (SandbagsCard) selectedCard);
            } else if (selectedCard instanceof HelicopterLiftCard) {
                useHelicopterLiftCard(currentPlayer, (HelicopterLiftCard) selectedCard);
            } else {
                showMessage(currentPlayer.getName() + " unknown special action card type");
            }
        });
    }

    /**
     * Handle Navigator special ability - can move other players up to 2 adjacent tiles
     */
    private void handleNavigatorSpecialAbility() {
        if (!canPerformAction()) return;

        Player currentPlayer = game.getCurrentPlayer();

        // Select player to move
        List<Player> otherPlayers = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            if (player != currentPlayer) {
                otherPlayers.add(player);
            }
        }

        if (otherPlayers.isEmpty()) {
            showMessage("No other players to move!");
            return;
        }

        Dialog<Player> playerDialog = new Dialog<>();
        playerDialog.setTitle("Navigator Special Ability");
        playerDialog.setHeaderText("Select player to move");

        ButtonType selectButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        playerDialog.getDialogPane().getButtonTypes().addAll(selectButtonType, cancelButtonType);

        ListView<Player> playerListView = new ListView<>();
        playerListView.getItems().addAll(otherPlayers);
        playerListView.setCellFactory(param -> new ListCell<Player>() {
            @Override
            protected void updateItem(Player player, boolean empty) {
                super.updateItem(player, empty);
                if (empty || player == null) {
                    setText(null);
                } else {
                    setText(player.getName() + " (" + player.getRole() + ") - Location: " +
                            (player.getCurrentLocation() != null ? player.getCurrentLocation().getName() : "Unknown"));
                }
            }
        });

        playerDialog.getDialogPane().setContent(playerListView);

        playerDialog.setResultConverter(dialogButton -> {
            if (dialogButton == selectButtonType) {
                return playerListView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        playerDialog.showAndWait().ifPresent(selectedPlayer -> {
            // Get selected player's current location
            IslandTile currentLocation = selectedPlayer.getCurrentLocation();
            if (currentLocation == null) {
                showMessage(selectedPlayer.getName() + " current location is invalid!");
                return;
            }

            // Get valid moves (up to 2 steps)
            Set<IslandTile> validMoves = getValidNavigatorMoves(selectedPlayer);

            if (validMoves.isEmpty()) {
                showMessage("No valid move targets!");
                return;
            }

            // Highlight movable tiles
            gameBoardView.highlightTiles(validMoves, Color.PURPLE);

            // Set tile selection callback
            gameBoardView.setTileSelectionCallback(selectedTile -> {
                gameBoardView.clearSelectionHighlights();
                gameBoardView.setTileSelectionCallback(null);

                if (validMoves.contains(selectedTile)) {
                    if (game.spendAction()) {
                        selectedPlayer.moveTo(selectedTile);
                        String message = currentPlayer.getName() + " (Navigator) moved " +
                                selectedPlayer.getName() + " to " + selectedTile.getName();
                        System.out.println(message);
                        showMessage(message);
                        mainApp.updateGameState();
                    } else {
                        showMessage("Not enough action points!");
                    }
                } else {
                    showMessage("Invalid move selection!");
                }
            });
        });
    }

    /**
     * Get valid moves for Navigator to move other players (up to 2 steps)
     */
    private Set<IslandTile> getValidNavigatorMoves(Player targetPlayer) {
        Set<IslandTile> validMoves = new HashSet<>();
        IslandTile currentLocation = targetPlayer.getCurrentLocation();

        if (currentLocation == null || game == null) return validMoves;

        // Get current coordinates
        int[] currentCoords = game.getTileCoordinates(currentLocation);
        if (currentCoords == null) return validMoves;

        // First step reachable tiles
        Set<IslandTile> firstStepTiles = new HashSet<>();
        int r = currentCoords[0];
        int c = currentCoords[1];

        // Orthogonal directions
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        // Check first step reachable tiles
        for (int i = 0; i < 4; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];

            if (game.isValidBoardCoordinate(nr, nc)) {
                IslandTile adjacentTile = game.getGameBoard()[nr][nc];
                if (adjacentTile != null && !adjacentTile.isSunk()) {
                    firstStepTiles.add(adjacentTile);
                    validMoves.add(adjacentTile);
                }
            }
        }

        // Second step reachable tiles
        for (IslandTile firstStepTile : firstStepTiles) {
            int[] firstStepCoords = game.getTileCoordinates(firstStepTile);
            if (firstStepCoords == null) continue;

            r = firstStepCoords[0];
            c = firstStepCoords[1];

            // Check second step reachable tiles
            for (int i = 0; i < 4; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];

                if (game.isValidBoardCoordinate(nr, nc)) {
                    IslandTile adjacentTile = game.getGameBoard()[nr][nc];
                    if (adjacentTile != null && !adjacentTile.isSunk() && adjacentTile != currentLocation) {
                        validMoves.add(adjacentTile);
                    }
                }
            }
        }

        return validMoves;
    }

    private void useSandbagsCard(Player player, SandbagsCard card) {
        Set<IslandTile> floodedTiles = new HashSet<>();
        IslandTile[][] board = game.getGameBoard();
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                IslandTile tile = board[r][c];
                if (tile != null && tile.isFlooded() && !tile.isSunk()) {
                    floodedTiles.add(tile);
                }
            }
        }
        if (floodedTiles.isEmpty()) {
            System.out.println(player.getName() + " has no tiles to use sandbags on!");
            return;
        }
        System.out.println(player.getName() + " selecting tile to use sandbags on...");
        gameBoardView.highlightTiles(floodedTiles, Color.ORANGE);
        gameBoardView.setTileSelectionCallback(selectedTile -> {
            gameBoardView.clearSelectionHighlights();
            gameBoardView.setTileSelectionCallback(null);
            if (floodedTiles.contains(selectedTile)) {
                player.removeCardFromHand(card);
                game.getTreasureDeck().discardCard(card);
                selectedTile.setFlooded(false);
                System.out.println(player.getName() + " used sandbags to shore up " + selectedTile.getName());
                mainApp.updateGameState();
            } else {
                System.out.println(player.getName() + " invalid selection. Please choose from highlighted tiles.");
            }
        });
    }

    private void useHelicopterLiftCard(Player player, HelicopterLiftCard card) {
        List<Player> allPlayers = game.getPlayers();
        Map<Player, Boolean> selectedPlayersMap = new HashMap<>();
        for (Player p : allPlayers) {
            selectedPlayersMap.put(p, false);
        }

        Dialog<List<Player>> playerDialog = new Dialog<>();
        playerDialog.setTitle("Helicopter Lift");
        playerDialog.setHeaderText("Select players to move (multiple selection)");
        ButtonType nextButtonType = new ButtonType("Next", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        playerDialog.getDialogPane().getButtonTypes().addAll(nextButtonType, cancelButtonType);
        VBox playerSelectionBox = new VBox(10);
        List<CheckBox> playerCheckBoxes = new ArrayList<>();
        for (Player p : allPlayers) {
            CheckBox checkBox = new CheckBox(p.getName() + " (" + p.getRole() + ")");
            if (p == player) {
                checkBox.setSelected(true);
                selectedPlayersMap.put(p, true);
            }
            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                selectedPlayersMap.put(p, newVal);
            });
            playerCheckBoxes.add(checkBox);
            playerSelectionBox.getChildren().add(checkBox);
        }
        playerDialog.getDialogPane().setContent(playerSelectionBox);
        playerDialog.setResultConverter(dialogButton -> {
            if (dialogButton == nextButtonType) {
                List<Player> resultList = new ArrayList<>();
                for (Map.Entry<Player, Boolean> entry : selectedPlayersMap.entrySet()) {
                    if (entry.getValue()) {
                        resultList.add(entry.getKey());
                    }
                }
                return resultList;
            }
            return null;
        });
        playerDialog.showAndWait().ifPresent(selectedPlayersList -> {
            if (selectedPlayersList.isEmpty()) {
                showMessage(player.getName() + " didn't select any players, helicopter lift canceled.");
                return;
            }

            // Check win conditions
            boolean allTreasuresCollected = game.getTreasures().stream().allMatch(Treasure::isCollected);
            IslandTile foolsLanding = game.getIslandTileByName("Fools' Landing");
            boolean allPlayersAtFoolsLanding = true;

            for (Player p : allPlayers) {
                if (p.getCurrentLocation() != foolsLanding) {
                    allPlayersAtFoolsLanding = false;
                    break;
                }
            }

            // If all treasures collected and all players at Fools' Landing, declare victory
            if (allTreasuresCollected && allPlayersAtFoolsLanding) {
                player.removeCardFromHand(card);
                game.getTreasureDeck().discardCard(card);

                // Show victory message
                String victoryMessage = "Congratulations! All treasures collected and all players at Fools' Landing! Helicopter successfully took off! You win!";
                showMessage(victoryMessage);

                // Call game over dialog
                mainApp.updateGameState();
                return;
            }

            // Normal helicopter card usage
            Set<IslandTile> validDestinations = new HashSet<>();
            IslandTile[][] board = game.getGameBoard();
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    IslandTile tile = board[r][c];
                    if (tile != null && !tile.isSunk()) {
                        validDestinations.add(tile);
                    }
                }
            }
            System.out.println(player.getName() + " selecting helicopter landing tile...");
            gameBoardView.highlightTiles(validDestinations, Color.PURPLE);
            gameBoardView.setTileSelectionCallback(selectedTile -> {
                gameBoardView.clearSelectionHighlights();
                gameBoardView.setTileSelectionCallback(null);
                if (validDestinations.contains(selectedTile)) {
                    player.removeCardFromHand(card);
                    game.getTreasureDeck().discardCard(card);
                    StringBuilder movedPlayersNames = new StringBuilder();
                    for (Player p : selectedPlayersList) {
                        p.moveTo(selectedTile);
                        movedPlayersNames.append(p.getName()).append(", ");
                    }
                    if (movedPlayersNames.length() > 0) {
                        movedPlayersNames.setLength(movedPlayersNames.length() - 2);
                    }
                    String message = player.getName() + " used helicopter lift to move " + movedPlayersNames + " to " + selectedTile.getName();
                    System.out.println(message);
                    showMessage(message);
                    mainApp.updateGameState();
                } else {
                    showMessage(player.getName() + " invalid selection. Please choose from highlighted tiles.");
                }
            });
        });
    }

    private void handleGiveCardAction() {
        if (!canPerformAction()) return;
        Player currentPlayer = game.getCurrentPlayer();
        List<TreasureCard> treasureCardsInHand = new ArrayList<>();
        for (Card cardInHand : currentPlayer.getHand()) {
            if (cardInHand instanceof TreasureCard) {
                treasureCardsInHand.add((TreasureCard) cardInHand);
            }
        }
        if (treasureCardsInHand.isEmpty()) {
            String message = currentPlayer.getName() + " has no treasure cards to give!";
            System.out.println(message);
            showMessage(message);
            return;
        }
        List<Player> validRecipientPlayers = new ArrayList<>();
        IslandTile currentLocation = currentPlayer.getCurrentLocation();
        boolean isMessenger = currentPlayer.getRole() == AdventurerRole.MESSENGER;
        for (Player otherPlayer : game.getPlayers()) {
            if (otherPlayer != currentPlayer) {
                // Check if player's hand is full
                if (otherPlayer.getHand().size() >= Player.MAX_HAND_SIZE) {
                    continue; // Skip players with full hands
                }

                if (isMessenger || otherPlayer.getCurrentLocation() == currentLocation) {
                    validRecipientPlayers.add(otherPlayer);
                }
            }
        }
        if (validRecipientPlayers.isEmpty()) {
            String message;
            if (isMessenger) {
                message = currentPlayer.getName() + " has no other players to receive cards (all players have full hands)!";
            } else {
                message = currentPlayer.getName() + " has no players on the same tile to receive cards!";
            }
            System.out.println(message);
            showMessage(message);
            return;
        }

        Dialog<Player> playerDialog = new Dialog<>();
        playerDialog.setTitle("Select Player");
        playerDialog.setHeaderText("Select player to give card to");
        ButtonType selectPlayerButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelDialogButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        playerDialog.getDialogPane().getButtonTypes().addAll(selectPlayerButtonType, cancelDialogButtonType);
        ListView<Player> playerListView = new ListView<>();
        playerListView.getItems().addAll(validRecipientPlayers);
        playerListView.setCellFactory(param -> new ListCell<Player>() {
            @Override
            protected void updateItem(Player p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setText(null);
                } else {
                    setText(p.getName() + " (" + p.getRole() + ") - Cards: " + p.getHand().size() + "/" + Player.MAX_HAND_SIZE);
                }
            }
        });
        playerDialog.getDialogPane().setContent(playerListView);
        playerDialog.setResultConverter(dialogButton -> {
            if (dialogButton == selectPlayerButtonType) {
                return playerListView.getSelectionModel().getSelectedItem();
            }
            return null;
        });
        playerDialog.showAndWait().ifPresent(selectedPlayer -> {
            // Double-check selected player's hand isn't full (in case state changed while dialog was open)
            if (selectedPlayer.getHand().size() >= Player.MAX_HAND_SIZE) {
                showMessage(selectedPlayer.getName() + " hand is full, cannot receive more cards!");
                return;
            }

            Dialog<TreasureCard> cardDialog = new Dialog<>();
            cardDialog.setTitle("Select Card");
            cardDialog.setHeaderText("Select card to give to " + selectedPlayer.getName());
            ButtonType giveButtonType = new ButtonType("Give", ButtonBar.ButtonData.OK_DONE);
            cardDialog.getDialogPane().getButtonTypes().addAll(giveButtonType, cancelDialogButtonType);
            ListView<TreasureCard> cardListView = new ListView<>();
            cardListView.getItems().addAll(treasureCardsInHand);
            cardListView.setCellFactory(param -> new ListCell<TreasureCard>() {
                @Override
                protected void updateItem(TreasureCard tc, boolean empty) {
                    super.updateItem(tc, empty);
                    if (empty || tc == null) {
                        setText(null);
                    } else {
                        setText(tc.getTreasureType().getDisplayName() + " treasure card");
                    }
                }
            });
            cardDialog.getDialogPane().setContent(cardListView);
            cardDialog.setResultConverter(dialogButton -> {
                if (dialogButton == giveButtonType) {
                    return cardListView.getSelectionModel().getSelectedItem();
                }
                return null;
            });
            cardDialog.showAndWait().ifPresent(selectedCardToGive -> {
                if (game.spendAction()) {
                    currentPlayer.removeCardFromHand(selectedCardToGive);
                    selectedPlayer.addCardToHand(selectedCardToGive);
                    String message = currentPlayer.getName() + " gave " + selectedCardToGive.getTreasureType().getDisplayName() + " card to " + selectedPlayer.getName();
                    System.out.println(message);
                    showMessage(message);

                    // Ensure UI fully updates
                    if (mainApp != null) {
                        mainApp.updateGameState();
                    }
                } else {
                    String message = currentPlayer.getName() + " failed to give card: no action points left.";
                    System.out.println(message);
                    showMessage(message);
                }
            });
        });
    }

    private void handleEndActionsAndDrawTreasure() {
        if (game == null || mainApp == null || game.getCurrentPlayer() == null) {
            System.err.println("ActionPanel: Cannot end actions, required components missing.");
            return;
        }

        // If already processing another action, ignore this click
        if (isProcessingAction) {
            System.out.println("Processing another action, please wait...");
            return;
        }

        isProcessingAction = true; // Set lock flag
        disableAllButtons(); // Immediately disable all buttons to prevent duplicate clicks

        try {
            Player currentPlayer = game.getCurrentPlayer();
            System.out.println(currentPlayer.getName() + " ends actions, drawing treasure cards...");
            game.setCurrentPhase(Game.GamePhase.DRAW_TREASURE_CARDS_PHASE);

            game.playerDrawsTreasureCards();

            // Ensure UI updates, especially if Waters Rise card was drawn
            mainApp.updateGameState();

            // Check current game phase as it might have changed if Waters Rise card was drawn
            if (game.getCurrentPhase() == Game.GamePhase.DRAW_FLOOD_CARDS_PHASE) {
                // If already in draw flood cards phase (likely because Waters Rise card was drawn)
                System.out.println("Game phase changed to draw flood cards, likely due to Waters Rise card");
                update();
                return;
            }

            // Check hand limit
            if (currentPlayer.isHandOverLimit()) {
                System.out.println(currentPlayer.getName() + " has exceeded hand limit, needs to discard");
                showMessage(currentPlayer.getName() + " has exceeded hand limit, please select cards to discard");

                // Update UI to show discard buttons
                mainApp.updateGameState();

                // Force open discard dialog
                Platform.runLater(() -> {
                    if (mainApp != null && mainApp.getPlayerInfoPanel() != null) {
                        mainApp.getPlayerInfoPanel().forceDiscardAction();
                    }
                });
            } else {
                // If hand not over limit, proceed to draw flood cards phase
                System.out.println(currentPlayer.getName() + " hand not over limit, proceeding to draw flood cards");
                game.setCurrentPhase(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE);
                update();
            }
        } finally {
            isProcessingAction = false; // Always release lock flag
            update(); // Ensure button states are correctly updated
        }
    }

    private void handleDrawFloodCards() {
        if (game == null || mainApp == null || game.getCurrentPlayer() == null) {
            System.err.println("ActionPanel: Cannot draw flood cards, required components missing.");
            return;
        }

        // If already processing another action, ignore this click
        if (isProcessingAction) {
            System.out.println("Processing another action, please wait...");
            return;
        }

        isProcessingAction = true; // Set lock flag
        disableAllButtons(); // Immediately disable all buttons to prevent duplicate clicks

        try {
            Player currentPlayer = game.getCurrentPlayer();
            System.out.println(currentPlayer.getName() + " drawing flood cards...");

            // Draw flood cards
            game.playerDrawsFloodCards_REVISED();

            // Check game over conditions - let main game class handle game over dialog
            if (game.checkGameOverConditions()) {
                System.out.println("Game over!");
                // No longer showing message directly, handled by main game class
                mainApp.updateGameState(); // This will trigger game over handling in ForbiddenIslandGame
            } else {
                // Ensure water level updates
                mainApp.updateGameState();

                // Proceed to next player's turn
                game.nextTurn();

                // Update UI again to reflect new player's state
                mainApp.updateGameState();

                showMessage(game.getCurrentPlayer().getName() + "'s turn begins");
            }
        } finally {
            isProcessingAction = false; // Always release lock flag
            update(); // Ensure button states are correctly updated
        }
    }

    private boolean canPerformAction() {
        if (game == null || mainApp == null || gameBoardView == null || game.getCurrentPlayer() == null) {
            System.err.println("ActionPanel: Cannot perform action, required components missing.");
            return false;
        }
        if (game.getActionsRemainingInTurn() <= 0) {
            String message = game.getCurrentPlayer().getName() + " has no action points left!";
            System.out.println(message);
            showMessage(message);
            return false;
        }
        if (game.getCurrentPhase() != Game.GamePhase.ACTION_PHASE) {
            String message = game.getCurrentPlayer().getName() + " is no longer in action phase, cannot perform actions.";
            System.out.println(message);
            showMessage(message);
            return false;
        }
        return true;
    }

    public void updateActionPointsDisplay() {
        if (game != null && actionPointsLabel != null) {
            actionPointsLabel.setText("Action Points: " + game.getActionsRemainingInTurn());
        }
    }

    public void disableAllButtons() {
        moveButton.setDisable(true);
        shoreUpButton.setDisable(true);
        giveCardButton.setDisable(true);
        captureTreasureButton.setDisable(true);
        specialActionButton.setDisable(true);
        endActionsAndDrawTreasureButton.setDisable(true);
        drawFloodCardsButton.setDisable(true);
        if (actionPointsLabel != null) {
            actionPointsLabel.setText("Action Points: N/A");
        }
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setGameBoardView(GameBoardView gameBoardView) {
        this.gameBoardView = gameBoardView;
    }

    public void setGame(Game game) {
        this.game = game;
        update();
    }

    public void update() {
        if (game == null || game.getCurrentPlayer() == null) {
            disableAllButtons();
            return;
        }

        updateActionPointsDisplay();

        // If processing action, keep all buttons disabled
        if (isProcessingAction) {
            disableAllButtons();
            return;
        }

        Game.GamePhase currentPhase = game.getCurrentPhase();
        Player currentPlayer = game.getCurrentPlayer();

        // Update button states based on game phase
        switch (currentPhase) {
            case ACTION_PHASE:
                boolean canAct = game.getActionsRemainingInTurn() > 0;
                moveButton.setDisable(!canAct);
                shoreUpButton.setDisable(!canAct);
                giveCardButton.setDisable(!canAct);
                captureTreasureButton.setDisable(!canAct);
                specialActionButton.setDisable(!canAct);

                endActionsAndDrawTreasureButton.setDisable(false);
                drawFloodCardsButton.setDisable(true);

                actionPointsLabel.setText("Action Points: " + game.getActionsRemainingInTurn());
                break;

            case DRAW_TREASURE_CARDS_PHASE:
                disableActionButtonsForPhaseChange();
                endActionsAndDrawTreasureButton.setDisable(true);
                drawFloodCardsButton.setDisable(true);

                // If player hand exceeds limit, show discard buttons
                if (currentPlayer.isHandOverLimit()) {
                    System.out.println(currentPlayer.getName() + " needs to discard cards");
                }

                actionPointsLabel.setText("Draw Treasure Cards Phase");
                break;

            case DRAW_FLOOD_CARDS_PHASE:
                disableActionButtonsForPhaseChange();
                endActionsAndDrawTreasureButton.setDisable(true);
                drawFloodCardsButton.setDisable(false);

                actionPointsLabel.setText("Draw Flood Cards Phase");
                break;
        }
    }

    public void disableActionButtonsForPhaseChange() {
        moveButton.setDisable(true);
        shoreUpButton.setDisable(true);
        giveCardButton.setDisable(true);
        captureTreasureButton.setDisable(true);
        specialActionButton.setDisable(true);
    }
}