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
    private Button viewOtherPlayersButton;
    private VBox actionButtonsBox;
    private TextArea handCardsArea;

    public ActionPanel(Game game, ForbiddenIslandGame mainApp) {
        this.game = game;
        this.mainApp = mainApp;

        setPadding(new Insets(10));
        setSpacing(10);
        setPrefWidth(280);
        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999; -fx-border-width: 1px;");

        Label titleLabel = new Label("行动面板");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        getChildren().add(titleLabel);

        actionPointsLabel = new Label("行动点数: " + game.getActionsRemainingInTurn());
        actionPointsLabel.setFont(Font.font("Arial", 14));
        getChildren().add(actionPointsLabel);

        // 创建按钮面板（不使用滚动条）
        actionButtonsBox = new VBox(8);
        actionButtonsBox.setPadding(new Insets(5));
        
        // 直接添加按钮面板，不使用ScrollPane
        getChildren().add(actionButtonsBox);

        createActionButtons();
        createHandCardsArea();
    }

    private void createActionButtons() {
        // 创建按钮样式
        String buttonStyle = "-fx-background-color: linear-gradient(#f2f2f2, #d6d6d6); " +
                             "-fx-background-radius: 5; -fx-padding: 8 15; -fx-font-size: 13px;";
        String phaseButtonStyle = "-fx-background-color: linear-gradient(#61a2b1, #2A5058); " +
                                 "-fx-text-fill: white; -fx-font-weight: bold; " +
                                 "-fx-background-radius: 5; -fx-padding: 8 15; -fx-font-size: 13px;";

        moveButton = new Button("移动");
        moveButton.setMaxWidth(Double.MAX_VALUE);
        moveButton.setStyle(buttonStyle);
        moveButton.setOnAction(e -> handleMoveAction());

        shoreUpButton = new Button("治水 (Shore Up)");
        shoreUpButton.setMaxWidth(Double.MAX_VALUE);
        shoreUpButton.setStyle(buttonStyle);
        shoreUpButton.setOnAction(e -> handleShoreUpAction());

        giveCardButton = new Button("送卡 (Give Card)");
        giveCardButton.setMaxWidth(Double.MAX_VALUE);
        giveCardButton.setStyle(buttonStyle);
        giveCardButton.setOnAction(e -> handleGiveCardAction());

        captureTreasureButton = new Button("取宝 (Capture Treasure)");
        captureTreasureButton.setMaxWidth(Double.MAX_VALUE);
        captureTreasureButton.setStyle(buttonStyle);
        captureTreasureButton.setOnAction(e -> handleCaptureTreasureAction());

        specialActionButton = new Button("特殊能力 (Special Ability)");
        specialActionButton.setMaxWidth(Double.MAX_VALUE);
        specialActionButton.setStyle(buttonStyle);
        specialActionButton.setOnAction(e -> handleSpecialAction());
        
        endActionsAndDrawTreasureButton = new Button("结束行动 / 抽宝藏牌");
        endActionsAndDrawTreasureButton.setMaxWidth(Double.MAX_VALUE);
        endActionsAndDrawTreasureButton.setStyle(phaseButtonStyle);
        endActionsAndDrawTreasureButton.setOnAction(e -> handleEndActionsAndDrawTreasure());
        
        drawFloodCardsButton = new Button("抽取洪水牌");
        drawFloodCardsButton.setMaxWidth(Double.MAX_VALUE);
        drawFloodCardsButton.setStyle(phaseButtonStyle);
        drawFloodCardsButton.setDisable(true);
        drawFloodCardsButton.setOnAction(e -> handleDrawFloodCards());
        
        // 添加按钮到面板
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

    private void createHandCardsArea() {
        Label handLabel = new Label("手牌");
        handLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getChildren().add(handLabel);

        handCardsArea = new TextArea();
        handCardsArea.setEditable(false);
        handCardsArea.setPrefRowCount(6);
        handCardsArea.setPrefColumnCount(15);
        handCardsArea.setWrapText(true);
        handCardsArea.setStyle("-fx-font-size: 12px;");
        getChildren().add(handCardsArea);

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(5));
        
        Button discardButton = new Button("弃牌");
        discardButton.setPrefWidth(120);
        discardButton.setMaxWidth(Double.MAX_VALUE);
        discardButton.setOnAction(e -> handleDiscardAction());
        
        viewOtherPlayersButton = new Button("查看队友手牌");
        viewOtherPlayersButton.setPrefWidth(120);
        viewOtherPlayersButton.setMaxWidth(Double.MAX_VALUE);
        viewOtherPlayersButton.setOnAction(e -> handleViewOtherPlayersAction());
        
        buttonBox.getChildren().addAll(discardButton, viewOtherPlayersButton);
        buttonBox.setAlignment(Pos.CENTER);
        getChildren().add(buttonBox);
    }

    private void handleMoveAction() {
        if (!canPerformAction()) return;
        Player currentPlayer = game.getCurrentPlayer();
        Set<IslandTile> validMoves = currentPlayer.getValidMoves(game);

        if (validMoves.isEmpty()) {
            System.out.println(currentPlayer.getName() + " 没有有效的移动目标！");
            showMessage(currentPlayer.getName() + " 没有有效的移动目标！");
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
                    String message;
                    if (wasPilotFlight) {
                        currentPlayer.setPilotAbilityUsedThisTurn(true);
                        message = currentPlayer.getName() + " 飞行到了 " + selectedTile.getName();
                    } else {
                        message = currentPlayer.getName() + " 移动到了 " + selectedTile.getName();
                    }
                    System.out.println(message);
                    mainApp.updateGameState(); 
                } else {
                    String message = currentPlayer.getName() + " 尝试移动失败：没有行动点。";
                    System.out.println(message);
                    showMessage(message);
                }
            } else {
                String message = currentPlayer.getName() + " 无效的移动选择。请从高亮板块中选择。";
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
            String message = currentPlayer.getName() + " 附近没有可治水的板块！";
            System.out.println(message);
            showMessage(message);
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
                        String message = currentPlayer.getName() + " (工程师) 治水了 " + selectedTile.getName() + "，可以选择再治水一个板块...";
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
                                    finalMessage = currentPlayer.getName() + " (工程师) 治水了 " + selectedTile.getName() + " 和 " + secondTile.getName();
                                } else {
                                    finalMessage = currentPlayer.getName() + " 治水了 " + selectedTile.getName() + "，但放弃了第二次治水机会";
                                }
                                System.out.println(finalMessage);
                                showMessage(finalMessage);
                                mainApp.updateGameState();
                            });
                        } else {
                            String finalMessage = currentPlayer.getName() + " 治水了 " + selectedTile.getName() + "，没有更多可治水的板块";
                            System.out.println(finalMessage);
                            showMessage(finalMessage);
                            mainApp.updateGameState();
                        }
                    } else {
                        String message = currentPlayer.getName() + " 治水了 " + selectedTile.getName();
                        System.out.println(message);
                        showMessage(message);
                        mainApp.updateGameState();
                    }
                } else {
                    String message = currentPlayer.getName() + " 尝试治水失败：没有行动点。";
                    System.out.println(message);
                    showMessage(message);
                }
            } else {
                String message = currentPlayer.getName() + " 无效的治水选择。请从高亮板块中选择。";
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
            String message = currentPlayer.getName() + " 当前位置无效，无法取宝";
            System.out.println(message);
            showMessage(message);
            return;
        }
        
        TreasureType treasure = currentLocation.getAssociatedTreasure();
        if (treasure == null) {
            String message = currentPlayer.getName() + " 当前板块没有宝藏！";
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
                    // 更新宝藏状态
                    for (Treasure t : game.getTreasures()) {
                        if (t.getType() == treasure) {
                            t.setCollected();
                            break;
                        }
                    }
                    String message = currentPlayer.getName() + " 获得了宝藏：" + treasure.getDisplayName() + "！";
                    System.out.println(message);
                    showMessage(message);
                    mainApp.updateGameState();
                }
            } else {
                String message = currentPlayer.getName() + " 尝试取宝失败：没有行动点。";
                System.out.println(message);
                showMessage(message);
            }
        } else {
            String message = currentPlayer.getName() + " 需要" + requiredCards + "张" + treasure.getDisplayName() + "宝藏卡才能取宝！当前只有" + treasureCardCount + "张。";
            System.out.println(message);
            showMessage(message);
        }
    }

    private void handleSpecialAction() {
        Player currentPlayer = game.getCurrentPlayer();
        
        // 处理领航员特殊能力
        if (currentPlayer.getRole() == AdventurerRole.NAVIGATOR) {
            handleNavigatorSpecialAbility();
            return;
        }
        
        // 处理特殊卡牌
        List<Card> specialCards = new ArrayList<>();
        for (Card card : currentPlayer.getHand()) {
            if (card instanceof SpecialActionCard) {
                specialCards.add(card);
            }
        }
        if (specialCards.isEmpty()) {
            showMessage(currentPlayer.getName() + " 没有特殊行动卡！");
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
                showMessage(currentPlayer.getName() + " 未知的特殊行动卡类型");
            }
        });
    }
    
    /**
     * 处理领航员特殊能力 - 可以移动其他玩家最多2个相邻板块
     */
    private void handleNavigatorSpecialAbility() {
        if (!canPerformAction()) return;
        
        Player currentPlayer = game.getCurrentPlayer();
        
        // 选择要移动的玩家
        List<Player> otherPlayers = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            if (player != currentPlayer) {
                otherPlayers.add(player);
            }
        }
        
        if (otherPlayers.isEmpty()) {
            showMessage("没有其他玩家可以移动！");
            return;
        }
        
        Dialog<Player> playerDialog = new Dialog<>();
        playerDialog.setTitle("领航员特殊能力");
        playerDialog.setHeaderText("选择要移动的玩家");
        
        ButtonType selectButtonType = new ButtonType("选择", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
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
                    setText(player.getName() + " (" + player.getRole() + ") - 位置: " + 
                           (player.getCurrentLocation() != null ? player.getCurrentLocation().getName() : "未知"));
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
            // 获取选择的玩家当前位置
            IslandTile currentLocation = selectedPlayer.getCurrentLocation();
            if (currentLocation == null) {
                showMessage(selectedPlayer.getName() + " 当前位置无效！");
                return;
            }
            
            // 获取可移动的位置（最多2步）
            Set<IslandTile> validMoves = getValidNavigatorMoves(selectedPlayer);
            
            if (validMoves.isEmpty()) {
                showMessage("没有有效的移动目标！");
                return;
            }
            
            // 高亮显示可移动的板块
            gameBoardView.highlightTiles(validMoves, Color.PURPLE);
            
            // 设置板块选择回调
            gameBoardView.setTileSelectionCallback(selectedTile -> {
                gameBoardView.clearSelectionHighlights();
                gameBoardView.setTileSelectionCallback(null);
                
                if (validMoves.contains(selectedTile)) {
                    if (game.spendAction()) {
                        selectedPlayer.moveTo(selectedTile);
                        String message = currentPlayer.getName() + " (领航员) 将 " + 
                                        selectedPlayer.getName() + " 移动到了 " + selectedTile.getName();
                        System.out.println(message);
                        showMessage(message);
                        mainApp.updateGameState();
                    } else {
                        showMessage("没有足够的行动点！");
                    }
                } else {
                    showMessage("无效的移动选择！");
                }
            });
        });
    }

    /**
     * 获取领航员可以移动其他玩家的有效位置（最多2步）
     */
    private Set<IslandTile> getValidNavigatorMoves(Player targetPlayer) {
        Set<IslandTile> validMoves = new HashSet<>();
        IslandTile currentLocation = targetPlayer.getCurrentLocation();
        
        if (currentLocation == null || game == null) return validMoves;
        
        // 获取当前位置的坐标
        int[] currentCoords = game.getTileCoordinates(currentLocation);
        if (currentCoords == null) return validMoves;
        
        // 第一步可到达的位置
        Set<IslandTile> firstStepTiles = new HashSet<>();
        int r = currentCoords[0];
        int c = currentCoords[1];
        
        // 正交方向
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        
        // 检查第一步可到达的位置
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
        
        // 第二步可到达的位置
        for (IslandTile firstStepTile : firstStepTiles) {
            int[] firstStepCoords = game.getTileCoordinates(firstStepTile);
            if (firstStepCoords == null) continue;
            
            r = firstStepCoords[0];
            c = firstStepCoords[1];
            
            // 检查第二步可到达的位置
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
                showMessage(player.getName() + " 未选择任何玩家，取消直升机升空。");
                return;
            }
            
            // 检查是否满足胜利条件
            boolean allTreasuresCollected = game.getTreasures().stream().allMatch(Treasure::isCollected);
            IslandTile foolsLanding = game.getIslandTileByName("Fools' Landing");
            boolean allPlayersAtFoolsLanding = true;
            
            for (Player p : allPlayers) {
                if (p.getCurrentLocation() != foolsLanding) {
                    allPlayersAtFoolsLanding = false;
                    break;
                }
            }
            
            // 如果所有宝藏都已收集且所有玩家都在愚者起飞点，直接宣布胜利
            if (allTreasuresCollected && allPlayersAtFoolsLanding) {
                player.removeCardFromHand(card);
                game.getTreasureDeck().discardCard(card);
                
                // 显示胜利消息
                String victoryMessage = "恭喜！所有宝藏已收集，所有玩家都在愚者起飞点，直升机成功起飞！你们赢了！";
                showMessage(victoryMessage);
                
                // 调用游戏结束对话框
                mainApp.updateGameState();
                return;
            }
            
            // 正常使用直升机卡
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
                    String message = player.getName() + " 使用直升机升空卡将 " + movedPlayersNames + " 移动到了 " + selectedTile.getName();
                    System.out.println(message);
                    showMessage(message);
                    mainApp.updateGameState();
                } else {
                    showMessage(player.getName() + " 无效的选择。请从高亮板块中选择。");
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
            String message = currentPlayer.getName() + " 没有宝藏卡可以给予！";
            System.out.println(message);
            showMessage(message);
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
            String message;
            if (isMessenger) {
                message = currentPlayer.getName() + " 没有其他玩家可以接收卡牌！";
            } else {
                message = currentPlayer.getName() + " 同一板块上没有其他玩家！";
            }
            System.out.println(message);
            showMessage(message);
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
                    setText(p.getName() + " (" + p.getRole().getChineseName() + ")");
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
                        setText(tc.getTreasureType().getDisplayName() + " 宝藏卡");
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
                    String message = currentPlayer.getName() + " 将 " + selectedCardToGive.getTreasureType().getDisplayName() + " 卡给予了 " + selectedPlayer.getName();
                    System.out.println(message);
                    showMessage(message);
                    mainApp.updateGameState();
                } else {
                    String message = currentPlayer.getName() + " 尝试送卡失败：没有行动点。";
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
        Player currentPlayer = game.getCurrentPlayer();
        System.out.println(currentPlayer.getName() + " 结束行动，开始抽取宝藏牌...");
        game.setCurrentPhase(Game.GamePhase.DRAW_TREASURE_CARDS_PHASE); 
        disableActionButtonsForPhaseChange();
        endActionsAndDrawTreasureButton.setDisable(true); 

        game.playerDrawsTreasureCards(); 
        
        // 确保UI更新，特别是在抽到洪水上涨卡牌的情况下
        mainApp.updateGameState();
        
        // 检查当前游戏阶段，因为如果抽到了洪水上涨卡牌，阶段可能已经改变
        if (game.getCurrentPhase() == Game.GamePhase.DRAW_FLOOD_CARDS_PHASE) {
            // 如果已经进入抽洪水牌阶段（可能是因为抽到了洪水上涨卡牌），直接更新UI
            System.out.println("游戏阶段已变为抽洪水牌阶段，可能是因为抽到了洪水上涨卡牌");
            update();
            return;
        }
        
        // 检查手牌上限
        if (currentPlayer.isHandOverLimit()) {
            System.out.println(currentPlayer.getName() + " 手牌超过上限，需要弃牌");
            showMessage(currentPlayer.getName() + " 手牌超过上限，请选择要弃掉的卡牌");
            handleDiscardAction();
        } else {
            // 如果手牌未超过上限，直接进入抽洪水牌阶段
            System.out.println(currentPlayer.getName() + " 手牌未超过上限，进入抽洪水牌阶段");
            game.setCurrentPhase(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE);
            update();
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
        
        // 抽取洪水牌
        game.playerDrawsFloodCards_REVISED();
        
        // 检查游戏结束条件
        if (game.checkGameOverConditions()) {
            System.out.println("游戏结束！");
            disableAllButtons();
            showMessage("游戏结束！");
        } else {
            // 确保水位计更新
            mainApp.updateGameState();
            
            // 进入下一个玩家的回合
            game.nextTurn();
            
            // 再次更新UI以反映新玩家的状态
            mainApp.updateGameState();
            
            showMessage(game.getCurrentPlayer().getName() + " 的回合开始");
        }
    }

    private boolean canPerformAction() {
        if (game == null || mainApp == null || gameBoardView == null || game.getCurrentPlayer() == null) {
            System.err.println("ActionPanel: Cannot perform action, required components missing.");
            return false;
        }
        if (game.getActionsRemainingInTurn() <= 0) {
             String message = game.getCurrentPlayer().getName() + " 没有剩余行动点！";
             System.out.println(message);
             showMessage(message);
             return false;
        }
        if (game.getCurrentPhase() != Game.GamePhase.ACTION_PHASE) { 
            String message = game.getCurrentPlayer().getName() + " 已不在行动阶段，无法执行行动。";
            System.out.println(message);
            showMessage(message);
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
        Player currentPlayer = game.getCurrentPlayer();
        if (!currentPlayer.isHandOverLimit()) {
            showMessage("你的手牌未超过上限，不需要弃牌。");
            
            // 如果当前是抽宝藏牌阶段，且手牌未超过上限，确保进入抽洪水牌阶段
            if (game.getCurrentPhase() == Game.GamePhase.DRAW_TREASURE_CARDS_PHASE) {
                game.setCurrentPhase(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE);
                update();
            }
            return;
        }
        
        List<Card> hand = currentPlayer.getHand();
        int cardsToDiscard = hand.size() - Player.MAX_HAND_SIZE;
        
        Dialog<List<Card>> dialog = new Dialog<>();
        dialog.setTitle("弃牌");
        dialog.setHeaderText("请选择 " + cardsToDiscard + " 张卡牌弃掉\n当前手牌: " + hand.size() + "/" + Player.MAX_HAND_SIZE);
        
        ButtonType discardButtonType = new ButtonType("弃掉", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(discardButtonType, cancelButtonType);
        
        ListView<Card> cardListView = new ListView<>();
        cardListView.getItems().addAll(hand);
        cardListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        // 设置单元格工厂以显示卡牌信息
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
                        cardInfo = tc.getTreasureType().getDisplayName() + " 宝藏卡";
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
            if (selectedCards.size() != cardsToDiscard) {
                showMessage("请选择正确数量的卡牌: " + cardsToDiscard);
                handleDiscardAction(); // 重新打开弃牌对话框
            } else {
                for (Card card : selectedCards) {
                    currentPlayer.removeCardFromHand(card);
                    game.getTreasureDeck().discardCard(card);
                    System.out.println(currentPlayer.getName() + " 弃掉了: " + card.getName());
                }
                
                // 检查是否仍然超过手牌上限
                if (currentPlayer.isHandOverLimit()) {
                    handleDiscardAction(); // 如果仍然超过，继续弃牌
                } else {
                    // 如果在抽宝藏牌阶段弃牌完成，自动进入抽洪水牌阶段
                    if (game.getCurrentPhase() == Game.GamePhase.DRAW_TREASURE_CARDS_PHASE) {
                        System.out.println("弃牌完成，进入抽洪水牌阶段");
                        game.setCurrentPhase(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE);
                        update();
                    }
                }
            }
        });
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
        updateHandCardsDisplay();
        
        Game.GamePhase currentPhase = game.getCurrentPhase();
        Player currentPlayer = game.getCurrentPlayer();
        
        // 根据游戏阶段更新按钮状态
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
                
                actionPointsLabel.setText("行动点数: " + game.getActionsRemainingInTurn());
                break;
                
            case DRAW_TREASURE_CARDS_PHASE:
                disableActionButtonsForPhaseChange();
                endActionsAndDrawTreasureButton.setDisable(true);
                drawFloodCardsButton.setDisable(true);
                
                // 如果玩家手牌超过上限，显示弃牌按钮
                if (currentPlayer.isHandOverLimit()) {
                    // 这里我们可以添加一个提示，但实际上handleEndActionsAndDrawTreasure已经会调用handleDiscardAction
                    System.out.println(currentPlayer.getName() + " 需要弃牌");
                }
                
                actionPointsLabel.setText("抽宝藏牌阶段");
                break;
                
            case DRAW_FLOOD_CARDS_PHASE:
                disableActionButtonsForPhaseChange();
                endActionsAndDrawTreasureButton.setDisable(true);
                drawFloodCardsButton.setDisable(false);
                
                actionPointsLabel.setText("抽洪水牌阶段");
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

    public void disableAllButtons() {
        moveButton.setDisable(true);
        shoreUpButton.setDisable(true);
        giveCardButton.setDisable(true);
        captureTreasureButton.setDisable(true);
        specialActionButton.setDisable(true);
        endActionsAndDrawTreasureButton.setDisable(true);
        drawFloodCardsButton.setDisable(true);
        viewOtherPlayersButton.setDisable(true);
        if(actionPointsLabel != null){
            actionPointsLabel.setText("行动点数: N/A");
        }
    }

    /**
     * 处理查看其他玩家手牌的操作
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
            showMessage("没有其他玩家");
            return;
        }
        
        Dialog<Player> dialog = new Dialog<>();
        dialog.setTitle("查看队友手牌");
        dialog.setHeaderText("选择要查看手牌的玩家");
        
        ButtonType viewButtonType = new ButtonType("查看", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
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
     * 显示指定玩家的手牌
     */
    private void showPlayerCards(Player player) {
        if (player == null || player.getHand().isEmpty()) {
            showMessage(player == null ? "玩家不存在" : player.getName() + " 没有手牌");
            return;
        }
        
        Dialog<Void> cardsDialog = new Dialog<>();
        cardsDialog.setTitle(player.getName() + " 的手牌");
        cardsDialog.setHeaderText(player.getName() + " (" + player.getRole().getChineseName() + ") 当前拥有 " + 
                                  player.getHand().size() + " 张手牌");
        
        ButtonType closeButtonType = new ButtonType("关闭", ButtonBar.ButtonData.CANCEL_CLOSE);
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
                        setText(tc.getTreasureType().getDisplayName() + " 宝藏卡");
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
     * 更新手牌显示区域
     */
    private void updateHandCardsDisplay() {
        if (game == null || handCardsArea == null) return;
        
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;
        
        StringBuilder sb = new StringBuilder();
        List<Card> hand = currentPlayer.getHand();
        
        sb.append(currentPlayer.getName())
          .append(" (").append(currentPlayer.getRole().getChineseName()).append(") ")
          .append("的手牌：").append(hand.size()).append("/").append(Player.MAX_HAND_SIZE).append("\n\n");
        
        if (hand.isEmpty()) {
            sb.append("没有手牌");
        } else {
            for (int i = 0; i < hand.size(); i++) {
                Card card = hand.get(i);
                sb.append(i + 1).append(". ");
                
                if (card instanceof TreasureCard) {
                    TreasureCard tc = (TreasureCard) card;
                    sb.append(tc.getTreasureType().getDisplayName()).append(" 宝藏卡");
                } else if (card instanceof SpecialActionCard) {
                    sb.append(card.getName()).append(" - ").append(card.getDescription());
                } else {
                    sb.append(card.getName());
                }
                
                sb.append("\n");
            }
        }
        
        handCardsArea.setText(sb.toString());
    }
}
