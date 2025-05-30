package com.forbiddenisland.ui;

import com.forbiddenisland.ForbiddenIslandGame;
import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.Player;
import com.forbiddenisland.model.Card;
import com.forbiddenisland.model.TreasureCard;
import com.forbiddenisland.model.SpecialActionCard;
import com.forbiddenisland.model.AdventurerRole;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.effect.DropShadow;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Player Information Panel
 * Displays information for all players in the game
 * Part of the GUI integration work (Week 12)
 */
public class PlayerInfoPanel extends VBox {

    // Core components for player information display
    private Game game;
    private List<PlayerView> playerViews;
    private TextArea handCardsArea;
    private Button discardButton;
    private Button viewOtherPlayersButton;
    private ForbiddenIslandGame mainApp;

    /**
     * Constructor for PlayerInfoPanel
     * Initializes the panel with game state and player information
     * Part of GUI integration (Week 12)
     *
     * @param game The current game instance
     */
    public PlayerInfoPanel(Game game) {
        this.game = game;
        this.playerViews = new ArrayList<>();

        // Setup panel layout and styling
        setPadding(new Insets(10));
        setSpacing(10);
        setPrefWidth(300);

        setBorder(new Border(new BorderStroke(
                Color.GRAY,
                BorderStrokeStyle.SOLID,
                new CornerRadii(5),
                BorderWidths.DEFAULT
        )));

        // Initialize panel components
        Label titleLabel = new Label("Player Information");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getChildren().add(titleLabel);

        initializePlayerViews();
        createHandCardsArea();
    }

    private void initializePlayerViews() {
        VBox playersContainer = new VBox(10); // Add 10px spacing
        playersContainer.setPadding(new Insets(5));
        playersContainer.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #ddd; -fx-border-radius: 5;");

        Label playersHeader = new Label("Players");
        playersHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        playersHeader.setPadding(new Insets(0, 0, 5, 0));
        playersContainer.getChildren().add(playersHeader);

        for (Player player : game.getPlayers()) {
            PlayerView playerView = new PlayerView(player);
            playerViews.add(playerView);
            playersContainer.getChildren().add(playerView);
        }

        getChildren().add(playersContainer);
    }

    /**
     * Create hand cards display area
     */
    private void createHandCardsArea() {
        // Create hand cards container
        VBox handCardsContainer = new VBox(10);
        handCardsContainer.setPadding(new Insets(10));
        handCardsContainer.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5;");

        Label handLabel = new Label("Hand Cards");
        handLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Set up hand cards display area
        handCardsArea = new TextArea();
        handCardsArea.setEditable(false);
        handCardsArea.setPrefRowCount(12);  // Increase display lines for tile layout
        handCardsArea.setPrefColumnCount(30);
        handCardsArea.setWrapText(true);
        handCardsArea.setStyle("-fx-font-size: 14px; -fx-font-family: 'Arial';");

        // Create button container
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(5));
        buttonBox.setAlignment(Pos.CENTER);

        discardButton = new Button("Discard Card");
        discardButton.setPrefWidth(120);
        discardButton.setMaxWidth(Double.MAX_VALUE);
        discardButton.setStyle("-fx-font-size: 13px;");
        discardButton.setOnAction(e -> handleDiscardAction());

        viewOtherPlayersButton = new Button("View Teammate Hands");
        viewOtherPlayersButton.setPrefWidth(120);
        viewOtherPlayersButton.setMaxWidth(Double.MAX_VALUE);
        viewOtherPlayersButton.setStyle("-fx-font-size: 13px;");
        viewOtherPlayersButton.setOnAction(e -> handleViewOtherPlayersAction());

        buttonBox.getChildren().addAll(discardButton, viewOtherPlayersButton);

        // Add all elements to hand cards container
        handCardsContainer.getChildren().addAll(handLabel, handCardsArea, buttonBox);

        // Set alignment and size for hand cards container
        handCardsContainer.setAlignment(Pos.CENTER);
        handCardsContainer.setPrefWidth(280);
        handCardsContainer.setMaxWidth(Double.MAX_VALUE);

        // Add to main panel
        getChildren().add(handCardsContainer);

        // Initial update of hand cards display
        updateHandCardsDisplay();
    }

    /**
     * Update information for all players
     */
    public void update() {
        // Update current player highlighting
        Player currentPlayer = game.getCurrentPlayer();

        for (PlayerView playerView : playerViews) {
            playerView.update();
            playerView.setHighlighted(playerView.getPlayer().equals(currentPlayer));
        }

        // Update hand cards display
        updateHandCardsDisplay();
    }

    /**
     * Update the hand cards display area
     */
    private void updateHandCardsDisplay() {
        if (game == null || handCardsArea == null) return;

        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;

        StringBuilder sb = new StringBuilder();
        List<Card> hand = currentPlayer.getHand();

        sb.append(currentPlayer.getName())
                .append(" (").append(currentPlayer.getRole().getDescription()).append(") ")
                .append("Hand: ").append(hand.size()).append("/").append(Player.MAX_HAND_SIZE).append("\n\n");

        if (hand.isEmpty()) {
            sb.append("No hand cards");
        } else {
            // Group cards by type
            List<TreasureCard> treasureCards = new ArrayList<>();
            List<SpecialActionCard> specialCards = new ArrayList<>();

            for (Card card : hand) {
                if (card instanceof TreasureCard) {
                    treasureCards.add((TreasureCard) card);
                } else if (card instanceof SpecialActionCard) {
                    specialCards.add((SpecialActionCard) card);
                }
            }

            // Display treasure cards
            if (!treasureCards.isEmpty()) {
                sb.append("Treasure Cards:\n");
                for (TreasureCard card : treasureCards) {
                    sb.append("• ").append(card.getTreasureType().getDisplayName()).append("\n");
                }
                sb.append("\n");
            }

            // Display special action cards
            if (!specialCards.isEmpty()) {
                sb.append("Special Action Cards:\n");
                for (SpecialActionCard card : specialCards) {
                    sb.append("• ").append(card.getName()).append(" - ").append(card.getDescription()).append("\n");
                }
            }
        }

        handCardsArea.setText(sb.toString());

        // Ensure hand card count in player views is updated
        for (PlayerView playerView : playerViews) {
            playerView.update();
        }
    }

    /**
     * Handle discard card action
     */
    private void handleDiscardAction() {
        Player currentPlayer = game.getCurrentPlayer();
        List<Card> hand = currentPlayer.getHand();

        if (hand.isEmpty()) {
            showMessage("You have no hand cards to discard.");
            return;
        }

        boolean isOverLimit = currentPlayer.isHandOverLimit();
        int cardsToDiscard = isOverLimit ? hand.size() - Player.MAX_HAND_SIZE : 1; // Discard to limit if over, else 1

        Dialog<List<Card>> dialog = new Dialog<>();
        dialog.setTitle("Discard Cards");

        String headerText;
        if (isOverLimit) {
            headerText = "You must discard " + cardsToDiscard + " cards\nCurrent hand: " + hand.size() + "/" + Player.MAX_HAND_SIZE;
        } else {
            headerText = "Please select cards to discard\nCurrent hand: " + hand.size() + "/" + Player.MAX_HAND_SIZE;
        }
        dialog.setHeaderText(headerText);

        ButtonType discardButtonType = new ButtonType("Discard", ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(discardButtonType, cancelButtonType);

        // Disable cancel if forced discard (hand over limit during draw phase)
        if (isOverLimit && game.getCurrentPhase() == Game.GamePhase.DRAW_TREASURE_CARDS_PHASE) {
            dialog.getDialogPane().getButtonTypes().remove(cancelButtonType);
        }

        ListView<Card> cardListView = new ListView<>();
        cardListView.getItems().addAll(hand);
        cardListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Set cell factory to display card information
        cardListView.setCellFactory(param -> new ListCell<Card>() {
            @Override
            protected void updateItem(Card card, boolean empty) {
                super.updateItem(card, empty);
                if (empty || card == null) {
                    setText(null);
                } else {
                    String cardInfo;
                    if (card instanceof TreasureCard) {
                        TreasureCard tc = (TreasureCard) card;
                        cardInfo = tc.getTreasureType().getDisplayName() + " Treasure Card";
                    } else if (card instanceof SpecialActionCard) {
                        cardInfo = card.getName() + " - " + card.getDescription();
                    } else {
                        cardInfo = card.getName();
                    }
                    setText(cardInfo);
                }
            }
        });

        dialog.getDialogPane().setContent(cardListView);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == discardButtonType) {
                return new ArrayList<>(cardListView.getSelectionModel().getSelectedItems());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(selectedCards -> {
            if (isOverLimit && selectedCards.size() != cardsToDiscard) {
                showMessage("You must discard " + cardsToDiscard + " cards");
                handleDiscardAction(); // Re-open discard dialog
            } else if (selectedCards.isEmpty()) {
                showMessage("Please select at least one card to discard");
                // Re-open dialog if forced discard
                if (isOverLimit) {
                    handleDiscardAction();
                }
            } else {
                for (Card card : selectedCards) {
                    currentPlayer.removeCardFromHand(card);
                    game.getTreasureDeck().discardCard(card);
                    System.out.println(currentPlayer.getName() + " discarded: " + card.getName());
                }

                // Update UI display
                updateHandCardsDisplay();

                // Check if still over hand limit
                if (currentPlayer.isHandOverLimit()) {
                    showMessage("You still need to discard " + (currentPlayer.getHand().size() - Player.MAX_HAND_SIZE) + " cards");
                    handleDiscardAction(); // Continue discarding if still over
                } else {
                    // Move to draw flood cards phase if in draw treasure phase
                    if (game.getCurrentPhase() == Game.GamePhase.DRAW_TREASURE_CARDS_PHASE) {
                        System.out.println("Discard complete, entering draw flood cards phase");
                        game.setCurrentPhase(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE);

                        // Update entire game interface
                        if (mainApp != null) {
                            mainApp.updateGameState();
                        }
                    }
                }
            }
        });
    }

    /**
     * Handle view other players' hands action
     */
    private void handleViewOtherPlayersAction() {
        List<Player> otherPlayers = new ArrayList<>();
        Player currentPlayer = game.getCurrentPlayer();

        for (Player player : game.getPlayers()) {
            if (player != currentPlayer) {
                otherPlayers.add(player);
            }
        }

        if (otherPlayers.isEmpty()) {
            showMessage("No other players");
            return;
        }

        Dialog<Player> dialog = new Dialog<>();
        dialog.setTitle("View Teammate Hands");
        dialog.setHeaderText("Select a player to view their hand");

        ButtonType viewButtonType = new ButtonType("View", ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(viewButtonType, cancelButtonType);

        ListView<Player> playerListView = new ListView<>();
        playerListView.getItems().addAll(otherPlayers);
        playerListView.setCellFactory(param -> new ListCell<Player>() {
            @Override
            protected void updateItem(Player player, boolean empty) {
                super.updateItem(player, empty);
                if (empty || player == null) {
                    setText(null);
                } else {
                    setText(player.getName() + " (" + player.getRole() + ")");
                }
            }
        });

        dialog.getDialogPane().setContent(playerListView);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == viewButtonType) {
                return playerListView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(selectedPlayer -> {
            showPlayerCards(selectedPlayer);
        });
    }

    /**
     * Show a player's hand cards
     */
    private void showPlayerCards(Player player) {
        if (player == null || player.getHand().isEmpty()) {
            showMessage(player == null ? "Player does not exist" : player.getName() + " has no hand cards");
            return;
        }

        Dialog<Void> cardsDialog = new Dialog<>();
        cardsDialog.setTitle(player.getName() + "'s Hand");
        cardsDialog.setHeaderText(player.getName() + " (" + player.getRole().getDescription() + ") currently has " +
                player.getHand().size() + " hand cards");

        ButtonType closeButtonType = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
        cardsDialog.getDialogPane().getButtonTypes().add(closeButtonType);

        ListView<Card> cardListView = new ListView<>();
        cardListView.getItems().addAll(player.getHand());
        cardListView.setCellFactory(param -> new ListCell<Card>() {
            @Override
            protected void updateItem(Card card, boolean empty) {
                super.updateItem(card, empty);
                if (empty || card == null) {
                    setText(null);
                } else {
                    if (card instanceof TreasureCard) {
                        TreasureCard tc = (TreasureCard) card;
                        setText(tc.getTreasureType().getDisplayName() + " Treasure Card");
                    } else if (card instanceof SpecialActionCard) {
                        setText(card.getName() + " - " + card.getDescription());
                    } else {
                        setText(card.getName());
                    }
                }
            }
        });

        cardsDialog.getDialogPane().setContent(cardListView);
        cardsDialog.showAndWait();
    }

    /**
     * Show information message dialog
     */
    private void showMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Set game object
     *
     * @param game New game object
     */
    public void setGame(Game game) {
        this.game = game;
        getChildren().clear();

        Label titleLabel = new Label("Player Information");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getChildren().add(titleLabel);

        playerViews.clear();
        initializePlayerViews();
        createHandCardsArea();  // Recreate hand cards area
    }

    /**
     * Force open discard dialog when hand over limit
     */
    public void forceDiscardAction() {
        if (game != null && game.getCurrentPlayer() != null) {
            Player currentPlayer = game.getCurrentPlayer();
            if (currentPlayer.isHandOverLimit()) {
                handleDiscardAction();
            }
        }
    }

    /**
     * Sets the reference to the main application
     *
     * @param mainApp ForbiddenIslandGame instance
     */
    public void setMainApp(ForbiddenIslandGame mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Inner class: Single player view
     */
    private class PlayerView extends VBox {
        private Player player;
        private Label nameLabel;
        private Label roleLabel;
        private Label cardsLabel;
        private Label locationLabel;
        private Label colorLabel;
        private ImageView pawnImageView;

        public PlayerView(Player player) {
            this.player = player;

            setPadding(new Insets(8));
            setSpacing(5);

            // Set border and rounded corners
            setBorder(new Border(new BorderStroke(
                    Color.LIGHTGRAY,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(8),
                    BorderWidths.DEFAULT
            )));

            // Create header bar with player number and role
            HBox headerBox = new HBox(8);
            headerBox.setAlignment(Pos.CENTER_LEFT);

            // Extract player number
            String playerName = player.getName();
            String playerNumber = playerName.replaceAll("\\D+", ""); // Extract numeric part

            // Create pawn image
            int pawnImageNumber = 1;
            try {
                pawnImageNumber = Integer.parseInt(playerNumber);
                // Ensure image number is between 1-7
                if (pawnImageNumber < 1 || pawnImageNumber > 7) {
                    pawnImageNumber = 1;
                }
            } catch (NumberFormatException e) {
                // Use default value 1 if parsing fails
            }

            // Load pawn image
            String imagePath = "/images/pawns/" + pawnImageNumber + ".png";
            pawnImageView = new ImageView();
            try {
                Image pawnImage = new Image(getClass().getResourceAsStream(imagePath),
                        30, 30, true, true);
                pawnImageView.setImage(pawnImage);

                // Add shadow effect
                DropShadow pawnShadow = new DropShadow();
                pawnShadow.setRadius(3.0);
                pawnShadow.setOffsetX(2.0);
                pawnShadow.setOffsetY(2.0);
                pawnShadow.setColor(Color.color(0, 0, 0, 0.5));
                pawnImageView.setEffect(pawnShadow);
            } catch (Exception e) {
                // If image loading fails, create a colored rectangle as fallback
                String playerColor = player.getPawn().getColor();
                javafx.scene.paint.Color color = getColorFromString(playerColor);

                javafx.scene.shape.Rectangle colorRect = new javafx.scene.shape.Rectangle(24, 24);
                colorRect.setFill(color);
                colorRect.setStroke(Color.BLACK);
                colorRect.setStrokeWidth(1);
                colorRect.setArcWidth(5);
                colorRect.setArcHeight(5);

                // Create a StackPane to contain the rectangle and player number
                StackPane pawnStack = new StackPane();
                Label numLabel = new Label(playerNumber);
                numLabel.setTextFill(playerColor.equalsIgnoreCase("WHITE") ||
                        playerColor.equalsIgnoreCase("YELLOW") ?
                        Color.BLACK : Color.WHITE);
                numLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                pawnStack.getChildren().addAll(colorRect, numLabel);

                headerBox.getChildren().add(pawnStack);
                pawnImageView = null;
            }

            // If image loaded successfully, add to headerBox
            if (pawnImageView != null) {
                headerBox.getChildren().add(pawnImageView);
            }

            // Create player name and role labels
            nameLabel = new Label();
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            // Create role label with background color
            VBox playerInfoBox = new VBox(2);
            playerInfoBox.setAlignment(Pos.CENTER_LEFT);

            // Add player name to headerBox
            headerBox.getChildren().add(nameLabel);

            // Create role label with distinctive background color
            roleLabel = new Label();
            roleLabel.setFont(Font.font("Arial", 12));
            roleLabel.setPadding(new Insets(3, 6, 3, 6));
            roleLabel.setTextFill(Color.WHITE);

            // Set different background color based on role
            String roleBgColor = getRoleBackgroundColor(player.getRole());
            roleLabel.setStyle("-fx-background-color: " + roleBgColor + "; -fx-background-radius: 3;");

            // Create location and hand cards labels
            locationLabel = new Label();
            locationLabel.setFont(Font.font("Arial", 12));

            cardsLabel = new Label();
            cardsLabel.setFont(Font.font("Arial", 12));

            // Current player marker
            colorLabel = new Label();
            colorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

            // Add all components to player info box
            playerInfoBox.getChildren().addAll(roleLabel, locationLabel, cardsLabel);

            // Add all components to main container
            getChildren().addAll(headerBox, playerInfoBox);

            // Initial update
            update();
        }

        public void update() {
            nameLabel.setText(player.getName());

            // Get short description of role (content before first parenthesis)
            String roleDesc = player.getRole().getDescription();
            int bracketIndex = roleDesc.indexOf('(');
            String shortDesc = bracketIndex > 0 ? roleDesc.substring(0, bracketIndex).trim() : roleDesc;

            // Set role name and short description
            roleLabel.setText(player.getRole().getChineseName() + " - " + shortDesc);

            // Update hand cards information
            cardsLabel.setText("Cards: " + player.getHand().size() + "/" + Player.MAX_HAND_SIZE);

            // Update current location
            if (player.getPawn() != null && player.getPawn().getCurrentLocation() != null) {
                locationLabel.setText("Location: " + player.getPawn().getCurrentLocation().getName());
            } else {
                locationLabel.setText("Location: Unknown");
            }
        }

        public void setHighlighted(boolean highlighted) {
            if (highlighted) {
                // Use more prominent highlight effect
                setStyle("-fx-background-color: #FFFFE0; -fx-border-color: #FFD700; -fx-border-width: 2; -fx-effect: dropshadow(three-pass-box, #FFD700, 10, 0.5, 0, 0);");

                // Add current player marker
                if (!getChildren().contains(colorLabel)) {
                    colorLabel.setText("【Current Player】");
                    colorLabel.setTextFill(Color.RED);
                    colorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                    getChildren().add(0, colorLabel);

                    // Add arrow indicator
                    Label arrowLabel = new Label("➤");
                    arrowLabel.setTextFill(Color.RED);
                    arrowLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                    HBox arrowBox = new HBox(arrowLabel);
                    arrowBox.setAlignment(Pos.CENTER_LEFT);
                    getChildren().add(1, arrowBox);
                }
            } else {
                setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-width: 1;");

                // Remove current player marker
                getChildren().removeIf(node -> node == colorLabel || (node instanceof HBox && ((HBox) node).getChildren().size() == 1 && ((HBox) node).getChildren().get(0) instanceof Label && ((Label) ((HBox) node).getChildren().get(0)).getText().equals("➤")));
            }
        }

        private javafx.scene.paint.Color getColorFromString(String colorName) {
            switch (colorName.toUpperCase()) {
                case "RED":
                    return Color.RED;
                case "BLUE":
                    return Color.BLUE;
                case "GREEN":
                    return Color.GREEN;
                case "BLACK":
                    return Color.BLACK;
                case "WHITE":
                    return Color.WHITE;
                case "YELLOW":
                    return Color.YELLOW;
                default:
                    return Color.GRAY;
            }
        }

        private String getRoleBackgroundColor(AdventurerRole role) {
            switch (role) {
                case DIVER:
                    return "#0066CC"; // Diver: Blue
                case ENGINEER:
                    return "#CC6600"; // Engineer: Brown
                case EXPLORER:
                    return "#009933"; // Explorer: Green
                case MESSENGER:
                    return "#CC33FF"; // Messenger: Purple
                case NAVIGATOR:
                    return "#FFCC00"; // Navigator: Yellow
                case PILOT:
                    return "#FF3300"; // Pilot: Red
                default:
                    return "#666666"; // Default: Gray
            }
        }

        public Player getPlayer() {
            return player;
        }
    }
}