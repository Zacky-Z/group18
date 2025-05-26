package com.forbiddenisland.ui;

import com.forbiddenisland.model.Card;
import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.Player;
import com.forbiddenisland.model.IslandTile;
import com.forbiddenisland.model.AdventurerRole; // For Pilot check
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
// import javafx.util.StringConverter; // Not used currently
import com.forbiddenisland.ForbiddenIslandGame;
// import java.util.Optional; // Not used currently
import java.util.Set;
import java.util.List; // For isAdjacent check

// 每回合3个动作
public class ActionPanel extends VBox {
    private Game game;
    private ForbiddenIslandGame application;
    private GameBoardView gameBoardView; 
    private Label actionCountLabel;
    private Label waterLevelLabel;
    private Label treasureStatusLabel;
    // private int remainingActions = 3; // Removed: Now managed by Game.java

    private Button moveButton;
    private Button shoreUpButton; // Placeholder for future
    private Button giveCardButton; // Placeholder for future
    private Button captureTreasureButton; // Placeholder for future
    private Button specialActionButton; // Placeholder for future
    private Button endActionsAndDrawTreasureButton; // New button
    private Button drawFloodCardsButton; // New button for drawing flood cards

    public ActionPanel(Game game, ForbiddenIslandGame application) {
        this.game = game;
        this.application = application;

        setPadding(new Insets(10));
        setSpacing(10);
        setPrefWidth(250);

        setBorder(new Border(new BorderStroke(
                Color.GRAY,
                BorderStrokeStyle.SOLID,
                new CornerRadii(5),
                BorderWidths.DEFAULT
        )));

        Label titleLabel = new Label("行动 与 阶段"); // Title updated for clarity
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getChildren().add(titleLabel);

        // 添加剩余动作点数显示
        actionCountLabel = new Label();
        actionCountLabel.setFont(Font.font("Arial", 12));
        getChildren().add(actionCountLabel);

        // 添加水位显示
        waterLevelLabel = new Label();
        waterLevelLabel.setFont(Font.font("Arial", 12));
        getChildren().add(waterLevelLabel);

        // 添加宝藏状态显示
        Label treasureLabel = new Label("宝藏状态:");
        treasureLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        getChildren().add(treasureLabel);

        // 宝藏列表显示
        treasureStatusLabel = new Label();
        treasureStatusLabel.setWrapText(true);
        treasureStatusLabel.setFont(Font.font("Arial", 12));
        getChildren().add(treasureStatusLabel);

        initializeButtons();
        update(); // Initial UI state based on game model
    }

    private void initializeButtons() {
        moveButton = new Button("移动 (Move)");
        moveButton.setOnAction(e -> handleMoveAction());
        moveButton.setMaxWidth(Double.MAX_VALUE);

        shoreUpButton = new Button("治水 (Shore Up)");
        // shoreUpButton.setOnAction(e -> handleShoreUpAction());
        shoreUpButton.setDisable(true); // TODO: Implement
        shoreUpButton.setMaxWidth(Double.MAX_VALUE);

        giveCardButton = new Button("送卡 (Give Card)");
        // giveCardButton.setOnAction(e -> handleGiveCardAction());
        giveCardButton.setDisable(true); // TODO: Implement
        giveCardButton.setMaxWidth(Double.MAX_VALUE);

        captureTreasureButton = new Button("取宝 (Capture Treasure)");
        // captureTreasureButton.setOnAction(e -> handleCaptureTreasureAction());
        captureTreasureButton.setDisable(true); // TODO: Implement
        captureTreasureButton.setMaxWidth(Double.MAX_VALUE);

        specialActionButton = new Button("特殊能力 (Special Ability)");
        // specialActionButton.setOnAction(e -> handleSpecialAction());
        specialActionButton.setDisable(true); // TODO: Implement
        specialActionButton.setMaxWidth(Double.MAX_VALUE);
        
        endActionsAndDrawTreasureButton = new Button("结束行动 / 抽宝藏牌");
        endActionsAndDrawTreasureButton.setOnAction(e -> handleEndActionsAndDrawTreasure());
        endActionsAndDrawTreasureButton.setMaxWidth(Double.MAX_VALUE);
        endActionsAndDrawTreasureButton.setStyle("-fx-font-weight: bold;"); // Make it stand out
        
        drawFloodCardsButton = new Button("抽取洪水牌");
        drawFloodCardsButton.setOnAction(e -> handleDrawFloodCards());
        drawFloodCardsButton.setMaxWidth(Double.MAX_VALUE);
        drawFloodCardsButton.setStyle("-fx-font-weight: bold;");
        drawFloodCardsButton.setDisable(true); // Initially disabled
        
        VBox buttonContainer = new VBox(8); 
        buttonContainer.getChildren().addAll(
            moveButton, 
            shoreUpButton, 
            giveCardButton, 
            captureTreasureButton, 
            specialActionButton,
            new Separator(), // Visually separate action buttons from phase progression buttons
            endActionsAndDrawTreasureButton,
            drawFloodCardsButton // Add new button here
        );
        
        // Add view hand button separately if desired, or integrate into action list
        Button viewHandButton = new Button("查看手牌 (View Hand)");
        viewHandButton.setOnAction(e -> showPlayerHand());
        viewHandButton.setMaxWidth(Double.MAX_VALUE);

        getChildren().addAll(buttonContainer, viewHandButton);
    }
    
    private void handleMoveAction() {
        if (!canPerformAction()) return;

        Player currentPlayer = game.getCurrentPlayer();
        Set<IslandTile> validMoves = currentPlayer.getValidMoves(game);

        if (validMoves.isEmpty()) {
            application.getStatusPanel().setStatus(currentPlayer.getName() + " 没有有效的移动目标！");
            return;
        }

        application.getStatusPanel().setStatus(currentPlayer.getName() + " 选择移动目标...");
        gameBoardView.highlightTiles(validMoves, Color.BLUE); 

        gameBoardView.setTileSelectionCallback(selectedTile -> {
            gameBoardView.clearSelectionHighlights();
            gameBoardView.setTileSelectionCallback(null); // Consume the callback

            if (validMoves.contains(selectedTile)) {
                if (game.spendAction()) {
                    boolean wasPilotFlight = false;
                    if (currentPlayer.getRole() == AdventurerRole.PILOT && 
                        !currentPlayer.isPilotAbilityUsedThisTurn()) {
                        // Check if the move was a flight: if selectedTile is not adjacent (ortho/diag)
                        int[] currentCoords = game.getTileCoordinates(currentPlayer.getCurrentLocation());
                        if (currentCoords != null) {
                            List<IslandTile> adjacentTiles = game.getValidAdjacentTiles(currentCoords[0], currentCoords[1], true);
                            if (!adjacentTiles.contains(selectedTile)) {
                                wasPilotFlight = true;
                            }
                        }
                    }

                    currentPlayer.moveTo(selectedTile);

                    if (wasPilotFlight) {
                        currentPlayer.setPilotAbilityUsedThisTurn(true);
                        application.getStatusPanel().setStatus(currentPlayer.getName() + " 飞行到了 " + selectedTile.getName());
                    } else {
                        application.getStatusPanel().setStatus(currentPlayer.getName() + " 移动到了 " + selectedTile.getName());
                    }
                    
                    application.updateGameState(); 
                } else {
                    application.getStatusPanel().setStatus("尝试移动失败：没有行动点。 "); // Should be caught by canPerformAction generally
                }
            } else {
                application.getStatusPanel().setStatus("无效的移动选择。请从高亮板块中选择。 ");
            }
        });
    }

    private void handleEndActionsAndDrawTreasure() {
        if (game == null || application == null || game.getCurrentPlayer() == null) {
            System.err.println("ActionPanel: Cannot end actions, required components missing.");
            return;
        }
        Player currentPlayer = game.getCurrentPlayer();
        application.getStatusPanel().setStatus(currentPlayer.getName() + " 结束行动，开始抽取宝藏牌...");

        game.setCurrentPhase(Game.GamePhase.DRAW_TREASURE_CARDS_PHASE); 
        disableActionButtons();
        endActionsAndDrawTreasureButton.setDisable(true); 

        List<String> drawResults = game.playerDrawsTreasureCards(); 
        application.showTreasureDrawResults(drawResults); // Show results in StatusPanel
        application.updateGameState(); // Update UI to show newly drawn cards in hand counts etc.

        // Check hand limit after drawing cards. This method in ForbiddenIslandGame will handle UI for discarding if needed.
        // If hand is over limit, this call will block until discards are made, then proceed.
        application.checkAndHandleHandLimit(currentPlayer, () -> {
            // This callback is executed after hand limit is resolved.
            application.playerProceedsToDrawFloodCardsPhase(); 
        });
    }

    private void handleDrawFloodCards() {
        if (game == null || application == null || game.getCurrentPlayer() == null) {
            System.err.println("ActionPanel: Cannot draw flood cards, required components missing.");
            return;
        }
        Player currentPlayer = game.getCurrentPlayer();
        application.getStatusPanel().setStatus(currentPlayer.getName() + " 正在抽取洪水牌...");
        drawFloodCardsButton.setDisable(true); // Disable after click

        // This will call game.playerDrawsFloodCards_REVISED(), update game state,
        // and then advance to the next turn if game is not over.
        application.processFloodCardDrawAndAdvance(); 
    }

    private boolean canPerformAction() {
        if (game == null || application == null || gameBoardView == null || game.getCurrentPlayer() == null) {
            System.err.println("ActionPanel: Cannot perform action, required components missing.");
            return false;
        }
        if (game.getActionsRemainingInTurn() <= 0) {
             application.getStatusPanel().setStatus("没有剩余行动点！");
             return false;
        }
        if(endActionsAndDrawTreasureButton.isDisabled()){ // If this button is disabled, means we are past action phase
            application.getStatusPanel().setStatus("已进入抽牌阶段，无法执行行动。");
            return false;
        }
        return true;
    }
    
    public void updateActionCountDisplay() {
        if (game != null) {
            actionCountLabel.setText("剩余行动点: " + game.getActionsRemainingInTurn());
        } else {
            actionCountLabel.setText("剩余行动点: -");
        }
    }
    
    public void setGameBoardView(GameBoardView gameBoardView) {
        this.gameBoardView = gameBoardView;
    }

    public void update() {
        if (game == null) return;

        Player currentPlayer = game.getCurrentPlayer();
        updateActionCountDisplay();

        // Update water level display
        waterLevelLabel.setText("当前水位: " + game.getWaterMeter().getCurrentWaterLevel() +
                                " (抽 " + game.getWaterMeter().getNumberOfFloodCardsToDraw() + " 张洪水牌)");

        // Update treasure status display
        StringBuilder treasureText = new StringBuilder();
        game.getTreasures().forEach(treasure -> {
            treasureText.append(treasure.getType().getDisplayName());
            if (treasure.isCollected()) {
                treasureText.append(" (已获取)");
            } else {
                treasureText.append(" (未获取)");
            }
            treasureText.append("\n");
        });
        treasureStatusLabel.setText(treasureText.toString());

        if (currentPlayer != null) {
            // Control button states based on game phase and actions remaining
            boolean inActionPhase = game.getCurrentPhase() == Game.GamePhase.ACTION_PHASE;
            boolean hasActions = game.getActionsRemainingInTurn() > 0;

            moveButton.setDisable(!inActionPhase || !hasActions);
            shoreUpButton.setDisable(true); // Keep disabled until implemented
            giveCardButton.setDisable(true); // Keep disabled until implemented
            captureTreasureButton.setDisable(true); // Keep disabled until implemented
            specialActionButton.setDisable(true); // Keep disabled until implemented
            
            endActionsAndDrawTreasureButton.setDisable(!inActionPhase);
            // drawFloodCardsButton is controlled by ForbiddenIslandGame.playerProceedsToDrawFloodCardsPhase

            if (game.getCurrentPhase() == Game.GamePhase.DRAW_TREASURE_CARDS_PHASE) {
                // If in treasure draw phase, ensure action buttons are disabled
                disableActionButtons(); 
                endActionsAndDrawTreasureButton.setDisable(true);
            } else if (game.getCurrentPhase() == Game.GamePhase.DRAW_FLOOD_CARDS_PHASE) {
                // If in flood draw phase, also disable action buttons and treasure draw button
                disableActionButtons();
                endActionsAndDrawTreasureButton.setDisable(true);
            } else if (inActionPhase && !hasActions) {
                 // If in action phase but no actions left, action buttons should be disabled
                disableActionButtons();
                // endActionsAndDrawTreasureButton should remain enabled to end the action phase.
            }

        } else {
            disableAllButtons(); // If no current player, disable everything
        }
    }
    
    /**
     * Called when the game state indicates a new turn or action points have been reset.
     * Ensures the UI reflects the current number of actions available from the Game model.
     */
    public void refreshActionState() { 
        if (game != null && game.getCurrentPlayer() != null) {
            enableActionButtons(); // Enable standard action buttons
            endActionsAndDrawTreasureButton.setDisable(false); // Ensure this is enabled at start of action phase
            drawFloodCardsButton.setDisable(true); // Ensure flood card button is disabled at start of a new action phase
        }
        update(); 
    }

    private void disableActionButtons(){
        moveButton.setDisable(true);
        shoreUpButton.setDisable(true);
        giveCardButton.setDisable(true);
        captureTreasureButton.setDisable(true);
        specialActionButton.setDisable(true);
    }
    
    private void enableActionButtons(){
        // Actual enablement will depend on game state (e.g., valid moves for moveButton)
        // This method just removes the 'forced disable' state.
        // The update() method will then set the correct disable state based on actions remaining etc.
        moveButton.setDisable(false);
        shoreUpButton.setDisable(false); // Placeholder, will be true until implemented
        giveCardButton.setDisable(false); // Placeholder
        captureTreasureButton.setDisable(false); // Placeholder
        specialActionButton.setDisable(false); // Placeholder
    }

    private void disableAllButtons(){
        disableActionButtons();
        endActionsAndDrawTreasureButton.setDisable(true);
        drawFloodCardsButton.setDisable(true);
    }

    public void disableActions() {
        disableAllButtons();
        actionCountLabel.setText("剩余行动点: 0"); // Reflect disabled state
        System.out.println("ActionPanel actions disabled.");
    }

    // Existing showPlayerHand method - slightly modified to fit better
    private void showPlayerHand() {
        if (game == null || game.getCurrentPlayer() == null) return;
        Player currentPlayer = game.getCurrentPlayer();
        List<Card> hand = currentPlayer.getHand();

        Dialog<Card> dialog = new Dialog<>();
        dialog.setTitle(currentPlayer.getName() + " 的手牌 (" + hand.size() + "/" + Player.MAX_HAND_SIZE + ")");
        dialog.setHeaderText(null);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        ListView<Card> handListView = new ListView<>();
        handListView.getItems().addAll(hand);
        handListView.setCellFactory(param -> new ListCell<Card>() {
            @Override
            protected void updateItem(Card card, boolean empty) {
                super.updateItem(card, empty);
                if (empty || card == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(card.getName() + " ( " + card.getDescription() + " ) ");
                }
            }
        });
        handListView.setPrefHeight(200);

        dialog.getDialogPane().setContent(handListView);
        dialog.getDialogPane().setPrefWidth(400);
        dialog.showAndWait();
    }

    public void enableDrawFloodCardsButton(boolean enable) {
        drawFloodCardsButton.setDisable(!enable);
    }
}