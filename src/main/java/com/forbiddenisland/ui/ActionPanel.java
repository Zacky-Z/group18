package com.forbiddenisland.ui;

import com.forbiddenisland.ForbiddenIslandGame;
import com.forbiddenisland.model.*;
import com.forbiddenisland.model.Card;
import com.forbiddenisland.model.Game;
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
import javafx.scene.control.*;
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

/**
 * 游戏操作面板，显示当前玩家可以执行的操作按钮
 * 优化布局以确保在各种窗口尺寸下完整显示
 */
public class ActionPanel extends VBox {

    private Game game;
    private ForbiddenIslandGame mainApp;
    private GameBoardView gameBoardView;
    private Label actionPointsLabel;
    private Button moveButton;
    private Button shoreUpButton;
    private Button giveCardButton;
    private Button captureTreasureButton;
    private Button specialActionButton;
    private Button endActionsAndDrawTreasureButton;
    private Button drawFloodCardsButton;
    private VBox actionButtonsBox;
    private ScrollPane scrollPane;

    public ActionPanel(Game game, ForbiddenIslandGame mainApp) {
        this.game = game;
        this.mainApp = mainApp;

        setPadding(new Insets(10));
        setSpacing(10);
        setPrefWidth(250);
        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999; -fx-border-width: 1px;");

        Label titleLabel = new Label("行动面板");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        getChildren().add(titleLabel);

        actionPointsLabel = new Label("行动点数: " + game.getActionsRemainingInTurn());
        actionPointsLabel.setFont(Font.font("Arial", 14));
        getChildren().add(actionPointsLabel);

        actionButtonsBox = new VBox(8);
        actionButtonsBox.setPadding(new Insets(5));

        scrollPane = new ScrollPane(actionButtonsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle("-fx-background-color: transparent;");
        getChildren().add(scrollPane);

        createActionButtons();
        createDiscardArea();
    }

    private void createActionButtons() {
        moveButton = new Button("移动");
        moveButton.setMaxWidth(Double.MAX_VALUE);
        moveButton.setOnAction(e -> handleMoveAction());

        shoreUpButton = new Button("治水 (Shore Up)");
        shoreUpButton.setMaxWidth(Double.MAX_VALUE);
        shoreUpButton.setOnAction(e -> handleShoreUpAction());

        giveCardButton = new Button("送卡 (Give Card)");
        giveCardButton.setMaxWidth(Double.MAX_VALUE);
        giveCardButton.setOnAction(e -> handleGiveCardAction());

        captureTreasureButton = new Button("取宝 (Capture Treasure)");
        captureTreasureButton.setMaxWidth(Double.MAX_VALUE);
        captureTreasureButton.setOnAction(e -> handleCaptureTreasureAction());

        specialActionButton = new Button("特殊能力 (Special Ability)");
        specialActionButton.setMaxWidth(Double.MAX_VALUE);
        specialActionButton.setOnAction(e -> handleSpecialAction());
        
        endActionsAndDrawTreasureButton = new Button("结束行动 / 抽宝藏牌");
        endActionsAndDrawTreasureButton.setMaxWidth(Double.MAX_VALUE);
        endActionsAndDrawTreasureButton.setStyle("-fx-font-weight: bold;");
        endActionsAndDrawTreasureButton.setOnAction(e -> handleEndActionsAndDrawTreasure());
        
        drawFloodCardsButton = new Button("抽取洪水牌");
        drawFloodCardsButton.setMaxWidth(Double.MAX_VALUE);
        drawFloodCardsButton.setStyle("-fx-font-weight: bold;");
        drawFloodCardsButton.setDisable(true);
        drawFloodCardsButton.setOnAction(e -> handleDrawFloodCards());
        
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

    private void createDiscardArea() {
        Label discardLabel = new Label("手牌");
        discardLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getChildren().add(discardLabel);

        TextArea handCardsArea = new TextArea();
        handCardsArea.setEditable(false);
        handCardsArea.setPrefRowCount(6);
        handCardsArea.setPrefColumnCount(15);
        handCardsArea.setWrapText(true);
        handCardsArea.setStyle("-fx-font-size: 12px;");
        getChildren().add(handCardsArea);

        Button discardButton = new Button("弃牌");
        discardButton.setPrefWidth(200);
        discardButton.setMaxWidth(Double.MAX_VALUE);
        discardButton.setOnAction(e -> handleDiscardAction());
        getChildren().add(discardButton);
    }

    private void handleMoveAction() {
        if (!canPerformAction()) return;
        Player currentPlayer = game.getCurrentPlayer();
        Set<IslandTile> validMoves = currentPlayer.getValidMoves(game);

        if (validMoves.isEmpty()) {
            System.out.println(currentPlayer.getName() + " 没有有效的移动目标！");
            return;
        }

        System.out.println(currentPlayer.getName() + " 选择移动目标...");
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
                    if (wasPilotFlight) {
                        currentPlayer.setPilotAbilityUsedThisTurn(true);
                        System.out.println(currentPlayer.getName() + " 飞行到了 " + selectedTile.getName());
                    } else {
                        System.out.println(currentPlayer.getName() + " 移动到了 " + selectedTile.getName());
                    }
                    mainApp.updateGameState(); 
                } else {
                    System.out.println(currentPlayer.getName() + " 尝试移动失败：没有行动点。");
                }
            } else {
                System.out.println(currentPlayer.getName() + " 无效的移动选择。请从高亮板块中选择。");
            }
        });
    }

    private void handleShoreUpAction() {
        if (!canPerformAction()) return;
        Player currentPlayer = game.getCurrentPlayer();
        Set<IslandTile> validShoreUpTiles = getValidShoreUpTiles(currentPlayer);

        if (validShoreUpTiles.isEmpty()) {
            System.out.println(currentPlayer.getName() + " 附近没有可治水的板块！");
            return;
        }

        System.out.println(currentPlayer.getName() + " 选择要治水的板块...");
        gameBoardView.highlightTiles(validShoreUpTiles, Color.ORANGE);

        gameBoardView.setTileSelectionCallback(selectedTile -> {
            gameBoardView.clearSelectionHighlights();
            gameBoardView.setTileSelectionCallback(null); 

            if (validShoreUpTiles.contains(selectedTile)) {
                if (game.spendAction()) {
                    selectedTile.shoreUp();
                    if (currentPlayer.getRole() == AdventurerRole.ENGINEER) {
                        System.out.println(currentPlayer.getName() + " (工程师) 治水了 " + selectedTile.getName() + "，可以选择再治水一个板块...");
                        Set<IslandTile> remainingShoreUpTiles = getValidShoreUpTiles(currentPlayer);
                        remainingShoreUpTiles.remove(selectedTile); 
                        if (!remainingShoreUpTiles.isEmpty()) {
                            gameBoardView.highlightTiles(remainingShoreUpTiles, Color.ORANGE);
                            gameBoardView.setTileSelectionCallback(secondTile -> {
                                gameBoardView.clearSelectionHighlights();
                                gameBoardView.setTileSelectionCallback(null);
                                if (remainingShoreUpTiles.contains(secondTile)) {
                                    secondTile.shoreUp();
                                    System.out.println(currentPlayer.getName() + " (工程师) 治水了 " + selectedTile.getName() + " 和 " + secondTile.getName());
                                } else {
                                    System.out.println(currentPlayer.getName() + " 治水了 " + selectedTile.getName() + "，但放弃了第二次治水机会");
                                }
                                mainApp.updateGameState();
                            });
                        } else {
                            System.out.println(currentPlayer.getName() + " 治水了 " + selectedTile.getName() + "，没有更多可治水的板块");
                            mainApp.updateGameState();
                        }
                    } else {
                        System.out.println(currentPlayer.getName() + " 治水了 " + selectedTile.getName());
                        mainApp.updateGameState();
                    }
                } else {
                    System.out.println(currentPlayer.getName() + " 尝试治水失败：没有行动点。");
                }
            } else {
                System.out.println(currentPlayer.getName() + " 无效的治水选择。请从高亮板块中选择。");
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
            System.out.println(currentPlayer.getName() + " 当前位置无效，无法取宝");
            return;
        }
        
        TreasureType treasure = currentLocation.getAssociatedTreasure();
        if (treasure == null) {
            System.out.println(currentPlayer.getName() + " 当前板块没有宝藏！");
            return;
        }
        
        int treasureCardCount = 0;
        List<Card> treasureCardsToDiscard = new ArrayList<>();
        for (Card card : currentPlayer.getHand()) {
            if (card instanceof TreasureCard) {
                TreasureCard treasureCard = (TreasureCard) card;
                if (treasureCard.getTreasureType() == treasure) {
                    treasureCardCount++;
                    treasureCardsToDiscard.add(card);
                }
            }
        }
        
        int requiredCards = currentPlayer.getRole().getTreasureCardsNeededForCapture();
        if (treasureCardCount >= requiredCards) {
            if (game.spendAction()) {
                for (int i = 0; i < requiredCards; i++) {
                    currentPlayer.removeCardFromHand(treasureCardsToDiscard.get(i));
                    game.getTreasureDeck().discardCard(treasureCardsToDiscard.get(i)); 
                }
                currentPlayer.addCollectedTreasure(treasure);
                for (Treasure t : game.getTreasures()) {
                    if (t.getType() == treasure) {
                        t.setCollected();
                        break;
                    }
                }
                System.out.println(currentPlayer.getName() + " 获得了宝藏：" + treasure.getDisplayName() + "！");
                mainApp.updateGameState();
            } else {
                System.out.println(currentPlayer.getName() + " 尝试取宝失败：没有行动点。");
            }
        } else {
            System.out.println(currentPlayer.getName() + " 需要" + requiredCards + "张" + treasure.getDisplayName() + "宝藏卡才能取宝！当前只有" + treasureCardCount + "张。");
        }
    }

    private void handleSpecialAction() {
        Player currentPlayer = game.getCurrentPlayer();
        List<Card> specialCards = new ArrayList<>();
        for (Card card : currentPlayer.getHand()) {
            if (card instanceof SpecialActionCard) {
                specialCards.add(card);
            }
        }
        if (specialCards.isEmpty()) {
            System.out.println(currentPlayer.getName() + " 没有特殊行动卡！");
            return;
        }

        Dialog<Card> dialog = new Dialog<>();
        dialog.setTitle("选择特殊行动卡");
        dialog.setHeaderText("选择要使用的特殊行动卡");
        ButtonType useButtonType = new ButtonType("使用", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
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
                System.out.println(currentPlayer.getName() + " 未知的特殊行动卡类型");
            }
        });
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
            System.out.println(player.getName() + " 没有可以使用沙袋的板块！");
            return;
        }
        System.out.println(player.getName() + " 选择使用沙袋的板块...");
        gameBoardView.highlightTiles(floodedTiles, Color.ORANGE);
        gameBoardView.setTileSelectionCallback(selectedTile -> {
            gameBoardView.clearSelectionHighlights();
            gameBoardView.setTileSelectionCallback(null);
            if (floodedTiles.contains(selectedTile)) {
                player.removeCardFromHand(card);
                game.getTreasureDeck().discardCard(card);
                selectedTile.setFlooded(false);
                System.out.println(player.getName() + " 使用沙袋治水了 " + selectedTile.getName());
                mainApp.updateGameState();
            } else {
                System.out.println(player.getName() + " 无效的选择。请从高亮板块中选择。");
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
        playerDialog.setTitle("直升机升空");
        playerDialog.setHeaderText("选择要移动的玩家（可多选）");
        ButtonType nextButtonType = new ButtonType("下一步", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
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
                System.out.println(player.getName() + " 未选择任何玩家，取消直升机升空。");
                return;
            }
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
            System.out.println(player.getName() + " 选择直升机降落的目标板块...");
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
                    System.out.println(player.getName() + " 使用直升机升空卡将 " + movedPlayersNames + " 移动到了 " + selectedTile.getName());
                    mainApp.updateGameState();
                } else {
                    System.out.println(player.getName() + " 无效的选择。请从高亮板块中选择。");
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
            System.out.println(currentPlayer.getName() + " 没有宝藏卡可以给予！");
            return;
        }
        List<Player> validRecipientPlayers = new ArrayList<>();
        IslandTile currentLocation = currentPlayer.getCurrentLocation();
        boolean isMessenger = currentPlayer.getRole() == AdventurerRole.MESSENGER;
        for (Player otherPlayer : game.getPlayers()) {
            if (otherPlayer != currentPlayer) {
                if (isMessenger || otherPlayer.getCurrentLocation() == currentLocation) {
                    validRecipientPlayers.add(otherPlayer);
                }
            }
        }
        if (validRecipientPlayers.isEmpty()) {
            if (isMessenger) {
                System.out.println(currentPlayer.getName() + " 没有其他玩家可以接收卡牌！");
            } else {
                System.out.println(currentPlayer.getName() + " 同一板块上没有其他玩家！");
            }
            return;
        }
        
        Dialog<Player> playerDialog = new Dialog<>();
        playerDialog.setTitle("选择玩家");
        playerDialog.setHeaderText("选择要给予卡牌的玩家");
        ButtonType selectPlayerButtonType = new ButtonType("选择", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelDialogButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
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
                    setText(p.getName() + " (" + p.getRole() + ")");
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
            Dialog<TreasureCard> cardDialog = new Dialog<>();
            cardDialog.setTitle("选择卡牌");
            cardDialog.setHeaderText("选择要给予 " + selectedPlayer.getName() + " 的卡牌");
            ButtonType giveButtonType = new ButtonType("给予", ButtonBar.ButtonData.OK_DONE);
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
                        setText(tc.getName() + " - " + tc.getTreasureType().getDisplayName());
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
                    System.out.println(currentPlayer.getName() + " 将 " + selectedCardToGive.getTreasureType().getDisplayName() + " 卡给予了 " + selectedPlayer.getName());
                    mainApp.updateGameState();
                } else {
                    System.out.println(currentPlayer.getName() + " 尝试送卡失败：没有行动点。");
                }
            });
        });
    }

    private void handleEndActionsAndDrawTreasure() {
        if (game == null || mainApp == null || game.getCurrentPlayer() == null) {
            System.err.println("ActionPanel: Cannot end actions, required components missing.");
            return;
        }
        Player currentPlayer = game.getCurrentPlayer();
        System.out.println(currentPlayer.getName() + " 结束行动，开始抽取宝藏牌...");
        game.setCurrentPhase(Game.GamePhase.DRAW_TREASURE_CARDS_PHASE); 
        disableActionButtonsForPhaseChange();
        endActionsAndDrawTreasureButton.setDisable(true); 

        game.playerDrawsTreasureCards(); 
        
        System.out.println("INFO: checkAndHandleHandLimit and playerProceedsToDrawFloodCardsPhase calls commented out in ActionPanel.");
        if (!currentPlayer.isHandOverLimit()) { 
             System.out.println("INFO: Manually proceeding to flood card phase from ActionPanel (TEMPORARY).");
             game.setCurrentPhase(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE);
             update(); 
        } else {
            System.out.println(currentPlayer.getName() + " needs to discard cards. Manual discard action needed.");
        }
    }

    private void handleDrawFloodCards() {
        if (game == null || mainApp == null || game.getCurrentPlayer() == null) {
            System.err.println("ActionPanel: Cannot draw flood cards, required components missing.");
            return;
        }
        Player currentPlayer = game.getCurrentPlayer();
        System.out.println(currentPlayer.getName() + " 正在抽取洪水牌...");
        drawFloodCardsButton.setDisable(true); 
        System.out.println("INFO: processFloodCardDrawAndAdvance call commented out in ActionPanel.");
        game.playerDrawsFloodCards_REVISED();
        if (!game.checkGameOverConditions()) {
             System.out.println("INFO: Manually advancing to next turn from ActionPanel (TEMPORARY).");
            game.nextTurn(); 
            update(); 
        } else {
            System.out.println("INFO: Game over after flood card draw.");
            disableAllButtons(); 
        }
    }

    private boolean canPerformAction() {
        if (game == null || mainApp == null || gameBoardView == null || game.getCurrentPlayer() == null) {
            System.err.println("ActionPanel: Cannot perform action, required components missing.");
            return false;
        }
        if (game.getActionsRemainingInTurn() <= 0) {
             System.out.println(game.getCurrentPlayer().getName() + " 没有剩余行动点！");
             return false;
        }
        if (game.getCurrentPhase() != Game.GamePhase.ACTION_PHASE) { 
            System.out.println(game.getCurrentPlayer().getName() + " 已不在行动阶段，无法执行行动。");
            return false;
        }
        return true;
    }
    
    public void updateActionPointsDisplay() {
        if (game != null && actionPointsLabel != null) {
            actionPointsLabel.setText("行动点数: " + game.getActionsRemainingInTurn());
        }
    }

    private void handleDiscardAction() {
        showMessage("弃牌功能（手动选择）将在后续版本中实现");
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer.isHandOverLimit()) {
            System.out.println(currentPlayer.getName() + " 手牌已满，请弃牌。");
        } else {
            System.out.println(currentPlayer.getName() + " 手牌未满，无需弃牌。");
        }
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("游戏信息");
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
        boolean inActionPhase = game.getCurrentPhase() == Game.GamePhase.ACTION_PHASE;
        boolean canAct = inActionPhase && game.getActionsRemainingInTurn() > 0;

        moveButton.setDisable(!canAct);
        shoreUpButton.setDisable(!canAct);
        giveCardButton.setDisable(!canAct);
        captureTreasureButton.setDisable(!canAct);
        specialActionButton.setDisable(!canAct);

        endActionsAndDrawTreasureButton.setDisable(!inActionPhase); 
        drawFloodCardsButton.setDisable(game.getCurrentPhase() != Game.GamePhase.DRAW_FLOOD_CARDS_PHASE);
    }
    
    public void disableActionButtonsForPhaseChange() {
        moveButton.setDisable(true);
        shoreUpButton.setDisable(true);
        giveCardButton.setDisable(true);
        captureTreasureButton.setDisable(true);
        specialActionButton.setDisable(true);
    }

    public void disableAllButtons() {
        moveButton.setDisable(true);
        shoreUpButton.setDisable(true);
        giveCardButton.setDisable(true);
        captureTreasureButton.setDisable(true);
        specialActionButton.setDisable(true);
        endActionsAndDrawTreasureButton.setDisable(true);
        drawFloodCardsButton.setDisable(true);
        if(actionPointsLabel != null){
            actionPointsLabel.setText("行动点数: N/A");
        }
    }
}
