package com.forbiddenisland.ui;

import com.forbiddenisland.model.Card;
import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.Player;
import com.forbiddenisland.model.IslandTile;
import com.forbiddenisland.model.AdventurerRole; // For Pilot check
import com.forbiddenisland.model.TreasureCard; // For treasure capture
import com.forbiddenisland.model.TreasureType; // For treasure types
import com.forbiddenisland.model.SpecialActionCard; // For special action cards
import com.forbiddenisland.model.SandbagsCard; // For Sandbags
import com.forbiddenisland.model.HelicopterLiftCard; // For Helicopter Lift
import com.forbiddenisland.model.Treasure; // For treasure collection
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
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.StringBuilder;

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
        shoreUpButton.setOnAction(e -> handleShoreUpAction());
        shoreUpButton.setMaxWidth(Double.MAX_VALUE);

        giveCardButton = new Button("送卡 (Give Card)");
        giveCardButton.setOnAction(e -> handleGiveCardAction());
        giveCardButton.setMaxWidth(Double.MAX_VALUE);

        captureTreasureButton = new Button("取宝 (Capture Treasure)");
        captureTreasureButton.setOnAction(e -> handleCaptureTreasureAction());
        captureTreasureButton.setMaxWidth(Double.MAX_VALUE);

        specialActionButton = new Button("特殊能力 (Special Ability)");
        specialActionButton.setOnAction(e -> handleSpecialAction());
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

    private void handleShoreUpAction() {
        if (!canPerformAction()) return;

        Player currentPlayer = game.getCurrentPlayer();
        Set<IslandTile> validShoreUpTiles = getValidShoreUpTiles(currentPlayer);

        if (validShoreUpTiles.isEmpty()) {
            application.getStatusPanel().setStatus(currentPlayer.getName() + " 附近没有可治水的板块！");
            return;
        }

        application.getStatusPanel().setStatus(currentPlayer.getName() + " 选择要治水的板块...");
        gameBoardView.highlightTiles(validShoreUpTiles, javafx.scene.paint.Color.ORANGE);

        gameBoardView.setTileSelectionCallback(selectedTile -> {
            gameBoardView.clearSelectionHighlights();
            gameBoardView.setTileSelectionCallback(null); // Consume the callback

            if (validShoreUpTiles.contains(selectedTile)) {
                if (game.spendAction()) {
                    // 执行治水操作
                    selectedTile.shoreUp();
                    
                    // 工程师特殊能力：一个行动可以治水两个板块
                    if (currentPlayer.getRole() == AdventurerRole.ENGINEER) {
                        application.getStatusPanel().setStatus(currentPlayer.getName() + 
                            " (工程师) 治水了 " + selectedTile.getName() + 
                            "，可以选择再治水一个板块...");
                        
                        // 获取剩余可治水的板块（排除刚治水的板块）
                        Set<IslandTile> remainingShoreUpTiles = getValidShoreUpTiles(currentPlayer);
                        remainingShoreUpTiles.remove(selectedTile); // 移除已治水的板块
                        
                        if (!remainingShoreUpTiles.isEmpty()) {
                            gameBoardView.highlightTiles(remainingShoreUpTiles, javafx.scene.paint.Color.ORANGE);
                            
                            gameBoardView.setTileSelectionCallback(secondTile -> {
                                gameBoardView.clearSelectionHighlights();
                                gameBoardView.setTileSelectionCallback(null);
                                
                                if (remainingShoreUpTiles.contains(secondTile)) {
                                    // 执行第二次治水
                                    secondTile.shoreUp();
                                    application.getStatusPanel().setStatus(currentPlayer.getName() + 
                                        " (工程师) 治水了 " + selectedTile.getName() + 
                                        " 和 " + secondTile.getName());
                                } else {
                                    application.getStatusPanel().setStatus(currentPlayer.getName() + 
                                        " 治水了 " + selectedTile.getName() + 
                                        "，但放弃了第二次治水机会");
                                }
                                application.updateGameState();
                            });
                        } else {
                            application.getStatusPanel().setStatus(currentPlayer.getName() + 
                                " 治水了 " + selectedTile.getName() + 
                                "，没有更多可治水的板块");
                            application.updateGameState();
                        }
                    } else {
                        application.getStatusPanel().setStatus(currentPlayer.getName() + 
                            " 治水了 " + selectedTile.getName());
                        application.updateGameState();
                    }
                } else {
                    application.getStatusPanel().setStatus("尝试治水失败：没有行动点。");
                }
            } else {
                application.getStatusPanel().setStatus("无效的治水选择。请从高亮板块中选择。");
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

        // 当前位置
        if (currentLocation.isFlooded()) {
            validTiles.add(currentLocation);
        }

        // 检查相邻板块（包括对角线，如果是探险家）
        boolean canShoreUpDiagonally = player.getRole().canShoreUpDiagonally();
        int r = currentCoords[0];
        int c = currentCoords[1];

        // 定义方向数组（上、下、左、右 + 对角线）
        int[] dr = {-1, 1, 0, 0, -1, -1, 1, 1};
        int[] dc = {0, 0, -1, 1, -1, 1, -1, 1};
        int limit = canShoreUpDiagonally ? 8 : 4; // 探险家可以斜向治水

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
            application.getStatusPanel().setStatus("当前位置无效，无法取宝");
            return;
        }
        
        // 检查当前板块是否有宝藏
        TreasureType treasure = currentLocation.getAssociatedTreasure();
        if (treasure == null) {
            application.getStatusPanel().setStatus("当前板块没有宝藏！");
            return;
        }
        
        // 统计玩家手牌中对应宝藏卡的数量
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
        
        // 获取角色所需的宝藏卡数量
        int requiredCards = currentPlayer.getRole().getTreasureCardsNeededForCapture();
        
        if (treasureCardCount >= requiredCards) {
            // 玩家有足够的宝藏卡，花费一个行动点获取宝藏
            if (game.spendAction()) {
                // 丢弃宝藏卡
                for (int i = 0; i < requiredCards; i++) {
                    game.discardCardAndPlaceOnTreasureDiscardPile(currentPlayer, treasureCardsToDiscard.get(i));
                }
                
                // 添加宝藏到玩家收集列表
                currentPlayer.addCollectedTreasure(treasure);
                
                // 标记宝藏为已收集
                for (Treasure t : game.getTreasures()) {
                    if (t.getType() == treasure) {
                        t.setCollected();
                        break;
                    }
                }
                
                application.getStatusPanel().setStatus(currentPlayer.getName() + " 获得了宝藏：" + treasure.getDisplayName() + "！");
                application.updateGameState();
            } else {
                application.getStatusPanel().setStatus("尝试取宝失败：没有行动点。");
            }
        } else {
            application.getStatusPanel().setStatus("需要" + requiredCards + "张" + treasure.getDisplayName() + "宝藏卡才能取宝！当前只有" + treasureCardCount + "张。");
        }
    }

    private void handleSpecialAction() {
        Player currentPlayer = game.getCurrentPlayer();
        List<Card> specialCards = new ArrayList<>();
        
        // 找出玩家手中的特殊行动卡
        for (Card card : currentPlayer.getHand()) {
            if (card instanceof SpecialActionCard) {
                specialCards.add(card);
            }
        }
        
        if (specialCards.isEmpty()) {
            application.getStatusPanel().setStatus(currentPlayer.getName() + " 没有特殊行动卡！");
            return;
        }

        // 创建特殊行动卡选择对话框
        Dialog<Card> dialog = new Dialog<>();
        dialog.setTitle("选择特殊行动卡");
        dialog.setHeaderText("选择要使用的特殊行动卡");

        // 设置按钮
        ButtonType useButtonType = new ButtonType("使用", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(useButtonType, cancelButtonType);
        
        // 创建卡片列表
        ListView<Card> cardListView = new ListView<>();
        cardListView.getItems().addAll(specialCards);
        
        // 设置单元格渲染
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
        
        // 转换结果
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == useButtonType) {
                return cardListView.getSelectionModel().getSelectedItem();
            }
            return null;
        });
        
        // 显示对话框并处理结果
        dialog.showAndWait().ifPresent(selectedCard -> {
            if (selectedCard instanceof SandbagsCard) {
                // 沙袋卡 - 治水任意板块
                useSandbagsCard(currentPlayer, (SandbagsCard) selectedCard);
            } else if (selectedCard instanceof HelicopterLiftCard) {
                // 直升机卡 - 移动一个或多个棋子
                useHelicopterLiftCard(currentPlayer, (HelicopterLiftCard) selectedCard);
            } else {
                application.getStatusPanel().setStatus("未知的特殊行动卡类型");
            }
        });
    }
    
    private void useSandbagsCard(Player player, SandbagsCard card) {
        // 查找所有淹没的板块(任何地方，不限于玩家周围)
        Set<IslandTile> floodedTiles = new HashSet<>();
        IslandTile[][] board = game.getGameBoard();
        
        // 遍历棋盘寻找淹没的板块
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                IslandTile tile = board[r][c];
                if (tile != null && tile.isFlooded() && !tile.isSunk()) {
                    floodedTiles.add(tile);
                }
            }
        }
        
        if (floodedTiles.isEmpty()) {
            application.getStatusPanel().setStatus("没有可以使用沙袋的板块！");
            return;
        }
        
        application.getStatusPanel().setStatus(player.getName() + " 选择使用沙袋的板块...");
        gameBoardView.highlightTiles(floodedTiles, Color.ORANGE);
        
        gameBoardView.setTileSelectionCallback(selectedTile -> {
            gameBoardView.clearSelectionHighlights();
            gameBoardView.setTileSelectionCallback(null);
            
            if (floodedTiles.contains(selectedTile)) {
                // 移除卡片并使用
                player.removeCardFromHand(card);
                
                // 将板块从淹没状态恢复到正常状态
                selectedTile.setFlooded(false);
                application.getStatusPanel().setStatus(player.getName() + " 使用沙袋治水了 " + selectedTile.getName());
                application.updateGameState();
            } else {
                application.getStatusPanel().setStatus("无效的选择。请从高亮板块中选择。");
            }
        });
    }
    
    private void useHelicopterLiftCard(Player player, HelicopterLiftCard card) {
        // 第一步：选择要移动的玩家
        List<Player> allPlayers = game.getPlayers();
        Map<Player, Boolean> selectedPlayers = new HashMap<>();
        
        // 初始化所有玩家为未选中
        for (Player p : allPlayers) {
            selectedPlayers.put(p, false);
        }
        
        // 创建玩家选择对话框
        Dialog<List<Player>> playerDialog = new Dialog<>();
        playerDialog.setTitle("直升机升空");
        playerDialog.setHeaderText("选择要移动的玩家（可多选）");

        // 设置按钮
        ButtonType nextButtonType = new ButtonType("下一步", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        playerDialog.getDialogPane().getButtonTypes().addAll(nextButtonType, cancelButtonType);
        
        // 创建玩家列表，使用CheckBox来支持多选
        VBox playerSelectionBox = new VBox(10);
        List<CheckBox> playerCheckBoxes = new ArrayList<>();
        
        for (Player p : allPlayers) {
            CheckBox checkBox = new CheckBox(p.getName() + " (" + p.getRole() + ")");
            
            // 如果是当前玩家，默认选中
            if (p == player) {
                checkBox.setSelected(true);
                selectedPlayers.put(p, true);
            }
            
            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                selectedPlayers.put(p, newVal);
            });
            
            playerCheckBoxes.add(checkBox);
            playerSelectionBox.getChildren().add(checkBox);
        }
        
        playerDialog.getDialogPane().setContent(playerSelectionBox);
        
        // 转换结果
        playerDialog.setResultConverter(dialogButton -> {
            if (dialogButton == nextButtonType) {
                List<Player> selectedPlayersList = new ArrayList<>();
                for (Map.Entry<Player, Boolean> entry : selectedPlayers.entrySet()) {
                    if (entry.getValue()) {
                        selectedPlayersList.add(entry.getKey());
                    }
                }
                return selectedPlayersList;
            }
            return null;
        });
        
        // 显示玩家选择对话框
        playerDialog.showAndWait().ifPresent(selectedPlayersList -> {
            if (selectedPlayersList.isEmpty()) {
                application.getStatusPanel().setStatus("未选择任何玩家，取消直升机升空。");
                return;
            }
            
            // 第二步：选择目标板块
            // 获取所有未沉没的板块作为可能的目标
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
            
            application.getStatusPanel().setStatus("选择直升机降落的目标板块...");
            gameBoardView.highlightTiles(validDestinations, Color.PURPLE);
            
            gameBoardView.setTileSelectionCallback(selectedTile -> {
                gameBoardView.clearSelectionHighlights();
                gameBoardView.setTileSelectionCallback(null);
                
                if (validDestinations.contains(selectedTile)) {
                    // 移除卡片并使用
                    player.removeCardFromHand(card);
                    
                    // 移动所有选中的玩家到目标板块
                    StringBuilder movedPlayersNames = new StringBuilder();
                    for (Player p : selectedPlayersList) {
                        p.moveTo(selectedTile);
                        movedPlayersNames.append(p.getName()).append(", ");
                    }
                    
                    // 去掉最后的逗号和空格
                    if (movedPlayersNames.length() > 0) {
                        movedPlayersNames.setLength(movedPlayersNames.length() - 2);
                    }
                    
                    application.getStatusPanel().setStatus(
                        player.getName() + " 使用直升机升空卡将 " + movedPlayersNames + 
                        " 移动到了 " + selectedTile.getName());
                    application.updateGameState();
                } else {
                    application.getStatusPanel().setStatus("无效的选择。请从高亮板块中选择。");
                }
            });
        });
    }

    private void handleGiveCardAction() {
        if (!canPerformAction()) return;
        
        Player currentPlayer = game.getCurrentPlayer();
        
        // 检查是否有宝藏卡可以送出
        List<TreasureCard> treasureCards = new ArrayList<>();
        for (Card card : currentPlayer.getHand()) {
            if (card instanceof TreasureCard) {
                treasureCards.add((TreasureCard) card);
            }
        }
        
        if (treasureCards.isEmpty()) {
            application.getStatusPanel().setStatus(currentPlayer.getName() + " 没有宝藏卡可以给予！");
            return;
        }
        
        // 获取同一板块上的其他玩家
        List<Player> playersOnSameTile = new ArrayList<>();
        IslandTile currentLocation = currentPlayer.getCurrentLocation();
        
        // 信使可以给予任何地方的玩家
        boolean isMessenger = currentPlayer.getRole() == AdventurerRole.MESSENGER;
        
        for (Player player : game.getPlayers()) {
            if (player != currentPlayer) {
                if (isMessenger || player.getCurrentLocation() == currentLocation) {
                    playersOnSameTile.add(player);
                }
            }
        }
        
        if (playersOnSameTile.isEmpty()) {
            if (isMessenger) {
                application.getStatusPanel().setStatus("没有其他玩家可以接收卡牌！");
            } else {
                application.getStatusPanel().setStatus("同一板块上没有其他玩家！");
            }
            return;
        }
        
        // 首先选择玩家
        Dialog<Player> playerDialog = new Dialog<>();
        playerDialog.setTitle("选择玩家");
        playerDialog.setHeaderText("选择要给予卡牌的玩家");
        
        ButtonType selectButtonType = new ButtonType("选择", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        playerDialog.getDialogPane().getButtonTypes().addAll(selectButtonType, cancelButtonType);
        
        ListView<Player> playerListView = new ListView<>();
        playerListView.getItems().addAll(playersOnSameTile);
        
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
        
        playerDialog.getDialogPane().setContent(playerListView);
        
        playerDialog.setResultConverter(dialogButton -> {
            if (dialogButton == selectButtonType) {
                return playerListView.getSelectionModel().getSelectedItem();
            }
            return null;
        });
        
        // 显示玩家选择对话框
        playerDialog.showAndWait().ifPresent(selectedPlayer -> {
            // 然后选择卡牌
            Dialog<TreasureCard> cardDialog = new Dialog<>();
            cardDialog.setTitle("选择卡牌");
            cardDialog.setHeaderText("选择要给予 " + selectedPlayer.getName() + " 的卡牌");
            
            ButtonType giveButtonType = new ButtonType("给予", ButtonBar.ButtonData.OK_DONE);
            cardDialog.getDialogPane().getButtonTypes().addAll(giveButtonType, cancelButtonType);
            
            ListView<TreasureCard> cardListView = new ListView<>();
            cardListView.getItems().addAll(treasureCards);
            
            cardListView.setCellFactory(param -> new ListCell<TreasureCard>() {
                @Override
                protected void updateItem(TreasureCard card, boolean empty) {
                    super.updateItem(card, empty);
                    if (empty || card == null) {
                        setText(null);
                    } else {
                        setText(card.getName() + " - " + card.getTreasureType().getDisplayName());
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
            
            // 显示卡牌选择对话框
            cardDialog.showAndWait().ifPresent(selectedCard -> {
                if (game.spendAction()) {
                    // 从当前玩家手中移除卡牌
                    currentPlayer.removeCardFromHand(selectedCard);
                    
                    // 将卡牌添加到目标玩家手中
                    selectedPlayer.addCardToHand(selectedCard);
                    
                    application.getStatusPanel().setStatus(currentPlayer.getName() + " 将 " + 
                        selectedCard.getTreasureType().getDisplayName() + " 卡给予了 " + selectedPlayer.getName());
                    application.updateGameState();
                } else {
                    application.getStatusPanel().setStatus("尝试送卡失败：没有行动点。");
                }
            });
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
        
        application.checkAndHandleHandLimit(currentPlayer, () -> {
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
        if (game == null) {
            disableAllButtons();
            actionCountLabel.setText("剩余行动点: -");
            waterLevelLabel.setText("水位: -");
            treasureStatusLabel.setText("宝藏: -");
            return;
        }

        Player currentPlayer = game.getCurrentPlayer();
        updateActionCountDisplay();

        // Update water level display
        if (game.getWaterMeter() != null) {
            waterLevelLabel.setText("当前水位: " + game.getWaterMeter().getCurrentWaterLevel() +
                                    " (抽 " + game.getWaterMeter().getNumberOfFloodCardsToDraw() + " 张洪水牌)");
        } else {
            waterLevelLabel.setText("水位: -");
        }

        // Update treasure status display
        if (game.getTreasures() != null) {
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
            treasureStatusLabel.setText(treasureText.toString().trim());
        } else {
            treasureStatusLabel.setText("宝藏: -");
        }

        if (currentPlayer != null) {
            Game.GamePhase currentPhase = game.getCurrentPhase();
            boolean hasActions = game.getActionsRemainingInTurn() > 0;

            // Determine button states based on phase
            if (currentPhase == Game.GamePhase.ACTION_PHASE) {
                moveButton.setDisable(!hasActions);
                // TODO: Implement actual logic for these buttons beyond just action points
                shoreUpButton.setDisable(!hasActions); // Placeholder, enable when implemented
                giveCardButton.setDisable(!hasActions); // Placeholder
                captureTreasureButton.setDisable(!hasActions); // Placeholder
                specialActionButton.setDisable(!hasActions); // Placeholder
                
                endActionsAndDrawTreasureButton.setDisable(false); // ALWAYS enabled in action phase to proceed
                drawFloodCardsButton.setDisable(true); // Disabled during action phase

            } else if (currentPhase == Game.GamePhase.DRAW_TREASURE_CARDS_PHASE) {
                disableActionButtons(); // Move, Shore Up, etc.
                endActionsAndDrawTreasureButton.setDisable(true); // Can't end action phase again
                drawFloodCardsButton.setDisable(true); // Not yet time for flood cards
                 // Note: enableDrawFloodCardsButton(true) is called by ForbiddenIslandGame when it's time

            } else if (currentPhase == Game.GamePhase.DRAW_FLOOD_CARDS_PHASE) {
                disableActionButtons();
                endActionsAndDrawTreasureButton.setDisable(true);
                // drawFloodCardsButton is typically enabled by ForbiddenIslandGame.playerProceedsToDrawFloodCardsPhase
                // and will be disabled by its own handler after click.
                // Here, we ensure it's explicitly managed if already in this phase upon an update call.
                // If actionPanel.enableDrawFloodCardsButton() was called, it might be true here.

            } else { // Unknown phase or game ended
                disableAllButtons();
            }
        } else { // currentPlayer == null
            disableAllButtons();
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