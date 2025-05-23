package com.forbiddenisland.ui;

import com.forbiddenisland.ForbiddenIslandGame;
import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.IslandTile;
import com.forbiddenisland.model.Player;
import com.forbiddenisland.model.Treasure;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 游戏动作面板 - 提供游戏中可执行的操作按钮
 */
public class ActionPanel extends VBox {
    
    private Game game;
    private ForbiddenIslandGame application;
    private GameBoardView gameBoardView;
    
    private Button moveButton;
    private Button shoreUpButton;
    private Button giveCardButton;
    private Button captureTreasureButton;
    private Button specialActionButton;
    private Button endTurnButton;
    
    private Label actionCountLabel;
    private Label waterLevelLabel;
    private Label treasureStatusLabel;
    private int remainingActions = 3; // 每回合3个动作
    
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
        
        Label titleLabel = new Label("行动");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getChildren().add(titleLabel);
        
        // 添加剩余动作点数显示
        actionCountLabel = new Label("剩余行动点: " + remainingActions);
        actionCountLabel.setFont(Font.font("Arial", 12));
        getChildren().add(actionCountLabel);
        
        // 添加水位显示
        waterLevelLabel = new Label();
        waterLevelLabel.setFont(Font.font("Arial", 12));
        getChildren().add(waterLevelLabel);
        
        // 添加宝藏状态显示
        Label treasureLabel = new Label("宝藏状态:");
        treasureLabel.setFont(Font.font("Arial", 12));
        getChildren().add(treasureLabel);
        
        // 宝藏列表显示
        treasureStatusLabel = new Label();
        treasureStatusLabel.setWrapText(true);
        getChildren().add(treasureStatusLabel);
        
        initializeButtons();
        updateGameInfo();
    }
    
    /**
     * 更新游戏信息显示
     */
    private void updateGameInfo() {
        // 更新水位显示
        int level = game.getWaterMeter().getCurrentWaterLevel();
        String levelLabel = game.getWaterMeter().getWaterLevelLabel();
        waterLevelLabel.setText(String.format("当前水位: %d (%s)", level, levelLabel));
        
        // 根据水位设置颜色
        if (level <= 2) {
            waterLevelLabel.setTextFill(Color.GREEN);
        } else if (level <= 5) {
            waterLevelLabel.setTextFill(Color.ORANGE);
        } else {
            waterLevelLabel.setTextFill(Color.RED);
        }
        
        // 更新宝藏状态
        StringBuilder treasureStatus = new StringBuilder();
        for (Treasure treasure : game.getTreasures()) {
            String status = treasure.isCollected() ? "已收集" : "未收集";
            Color textColor = treasure.isCollected() ? Color.GREEN : Color.BLACK;
            treasureStatus.append(treasure.getType().name()).append(": ").append(status).append("\n");
        }
        treasureStatusLabel.setText(treasureStatus.toString());
    }
    
    public void setGameBoardView(GameBoardView gameBoardView) {
        this.gameBoardView = gameBoardView;
    }
    
    private void initializeButtons() {
        moveButton = createActionButton("移动", e -> performMove());
        shoreUpButton = createActionButton("加固", e -> performShoreUp());
        giveCardButton = createActionButton("交换卡牌", e -> performGiveCard());
        captureTreasureButton = createActionButton("获取宝藏", e -> performCaptureTreasure());
        specialActionButton = createActionButton("特殊能力", e -> performSpecialAction());
        endTurnButton = createActionButton("结束回合", e -> endTurn());
        
        getChildren().addAll(
                moveButton,
                shoreUpButton,
                giveCardButton,
                captureTreasureButton,
                specialActionButton,
                endTurnButton
        );
    }
    
    private Button createActionButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setOnAction(handler);
        return button;
    }
    
    /**
     * 更新按钮状态
     */
    public void update() {
        // 根据剩余动作点数更新按钮状态
        boolean hasActions = remainingActions > 0;
        moveButton.setDisable(!hasActions);
        shoreUpButton.setDisable(!hasActions);
        giveCardButton.setDisable(!hasActions);
        captureTreasureButton.setDisable(!hasActions);
        specialActionButton.setDisable(!hasActions);
        
        // 更新显示文本
        actionCountLabel.setText("剩余行动点: " + remainingActions);
        
        // 更新游戏信息
        updateGameInfo();
    }
    
    /**
     * 禁用所有动作按钮（游戏结束时）
     */
    public void disableActions() {
        moveButton.setDisable(true);
        shoreUpButton.setDisable(true);
        giveCardButton.setDisable(true);
        captureTreasureButton.setDisable(true);
        specialActionButton.setDisable(true);
        endTurnButton.setDisable(true);
    }
    
    /**
     * 重置回合状态
     */
    public void resetTurn() {
        remainingActions = 3;
        update();
    }
    
    /**
     * 设置游戏对象
     * @param game 新的游戏对象
     */
    public void setGame(Game game) {
        this.game = game;
        resetTurn();
        updateGameInfo();
    }
    
    /**
     * 消耗一个动作点
     */
    private void useActionPoint() {
        if (remainingActions > 0) {
            remainingActions--;
            update();
        }
    }
    
    // 动作实现方法
    
    private void performMove() {
        if (gameBoardView == null) {
            showAlert("错误", "游戏板视图未初始化");
            return;
        }
        
        showInfoAlert("选择移动", "请点击要移动到的目标板块");
        
        // 设置板块选择回调
        gameBoardView.setTileSelectionCallback(targetTile -> {
            Player currentPlayer = game.getCurrentPlayer();
            IslandTile currentTile = currentPlayer.getPawn().getCurrentLocation();
            
            // 检查是否是相邻板块
            boolean isAdjacent = isAdjacentTile(currentTile, targetTile);
            
            if (isAdjacent) {
                // 执行移动
                currentPlayer.getPawn().moveTo(targetTile);
                System.out.println(currentPlayer.getName() + " 移动到 " + targetTile.getName());
                useActionPoint();
                application.updateGameState();
                gameBoardView.clearSelection();
                gameBoardView.setTileSelectionCallback(null);
            } else {
                showAlert("无效移动", "只能移动到相邻板块");
            }
        });
    }
    
    private boolean isAdjacentTile(IslandTile currentTile, IslandTile targetTile) {
        // 获取当前板块的坐标
        int[] currentCoords = game.getTileCoordinates(currentTile);
        int[] targetCoords = game.getTileCoordinates(targetTile);
        
        if (currentCoords == null || targetCoords == null) {
            return false;
        }
        
        int currentRow = currentCoords[0];
        int currentCol = currentCoords[1];
        int targetRow = targetCoords[0];
        int targetCol = targetCoords[1];
        
        // 检查是否是正交相邻
        boolean isOrthogonallyAdjacent = 
            (Math.abs(currentRow - targetRow) == 1 && currentCol == targetCol) ||
            (Math.abs(currentCol - targetCol) == 1 && currentRow == targetRow);
            
        // 检查是否是对角线相邻（探险家角色可以对角线移动）
        boolean isDiagonallyAdjacent = 
            (Math.abs(currentRow - targetRow) == 1 && Math.abs(currentCol - targetCol) == 1);
            
        return isOrthogonallyAdjacent || 
              (isDiagonallyAdjacent && game.getCurrentPlayer().getRole().toString().equals("EXPLORER"));
    }
    
    private void performShoreUp() {
        if (gameBoardView == null) {
            showAlert("错误", "游戏板视图未初始化");
            return;
        }
        
        showInfoAlert("选择加固", "请点击要加固的板块");
        
        // 设置板块选择回调
        gameBoardView.setTileSelectionCallback(targetTile -> {
            Player currentPlayer = game.getCurrentPlayer();
            IslandTile currentTile = currentPlayer.getPawn().getCurrentLocation();
            
            // 检查是否是当前板块或相邻板块
            boolean isCurrentOrAdjacent = targetTile == currentTile || isAdjacentTile(currentTile, targetTile);
            
            if (isCurrentOrAdjacent && targetTile.isFlooded()) {
                // 执行加固
                targetTile.shoreUp();
                System.out.println(currentPlayer.getName() + " 加固了 " + targetTile.getName());
                useActionPoint();
                application.updateGameState();
                gameBoardView.clearSelection();
                gameBoardView.setTileSelectionCallback(null);
            } else if (!targetTile.isFlooded()) {
                showAlert("无效操作", "只能加固已淹没的板块");
            } else {
                showAlert("无效操作", "只能加固当前或相邻的板块");
            }
        });
    }
    
    private void performGiveCard() {
        Player currentPlayer = game.getCurrentPlayer();
        
        if (currentPlayer.getHand().isEmpty()) {
            showAlert("无牌可给", "您当前没有卡牌可以交换");
            return;
        }
        
        // 创建对话框选择目标玩家
        Dialog<Player> dialog = new Dialog<>();
        dialog.setTitle("交换卡牌");
        dialog.setHeaderText("选择要交换卡牌的目标玩家");
        
        // 设置按钮
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // 创建玩家下拉列表
        ComboBox<Player> playerComboBox = new ComboBox<>();
        
        // 获取可交换的玩家（相同板块上的玩家）
        List<Player> eligiblePlayers = new ArrayList<>();
        IslandTile currentTile = currentPlayer.getPawn().getCurrentLocation();
        
        for (Player player : game.getPlayers()) {
            if (player != currentPlayer && player.getPawn().getCurrentLocation() == currentTile) {
                eligiblePlayers.add(player);
            }
        }
        
        if (eligiblePlayers.isEmpty()) {
            showAlert("无法交换", "当前板块上没有其他玩家，无法交换卡牌");
            return;
        }
        
        playerComboBox.getItems().addAll(eligiblePlayers);
        playerComboBox.setConverter(new javafx.util.StringConverter<Player>() {
            @Override
            public String toString(Player player) {
                return player.getName() + " (" + player.getRole() + ")";
            }
            
            @Override
            public Player fromString(String string) {
                return null;
            }
        });
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("选择玩家:"), 0, 0);
        grid.add(playerComboBox, 1, 0);
        
        dialog.getDialogPane().setContent(grid);
        
        // 转换结果
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return playerComboBox.getValue();
            }
            return null;
        });
        
        Optional<Player> result = dialog.showAndWait();
        
        result.ifPresent(targetPlayer -> {
            // 执行交换卡牌逻辑
            System.out.println(currentPlayer.getName() + " 与 " + targetPlayer.getName() + " 交换卡牌");
            useActionPoint();
            application.updateGameState();
        });
    }
    
    private void performCaptureTreasure() {
        Player currentPlayer = game.getCurrentPlayer();
        IslandTile currentTile = currentPlayer.getPawn().getCurrentLocation();
        
        // 检查当前板块是否有宝藏
        if (currentTile.getAssociatedTreasure() == null) {
            showAlert("无法获取宝藏", "当前位置没有宝藏");
            return;
        }
        
        // 检查玩家是否有足够的宝藏卡牌
        // 简化实现 - 实际应检查特定类型的宝藏卡牌
        if (currentPlayer.getHand().size() >= 4) {
            System.out.println(currentPlayer.getName() + " 获取了宝藏: " + currentTile.getAssociatedTreasure());
            useActionPoint();
            application.updateGameState();
        } else {
            showAlert("无法获取宝藏", "需要4张相同类型的宝藏卡才能获取宝藏");
        }
    }
    
    private void performSpecialAction() {
        Player currentPlayer = game.getCurrentPlayer();
        String role = currentPlayer.getRole().toString();
        
        switch (role) {
            case "PILOT":
                performPilotFly();
                break;
            case "ENGINEER":
                performEngineerAbility();
                break;
            case "NAVIGATOR":
                performNavigatorAbility();
                break;
            case "DIVER":
                performDiverAbility();
                break;
            case "MESSENGER":
                performMessengerAbility();
                break;
            case "EXPLORER":
                showAlert("探险家能力", "探险家可以斜向移动和加固，使用普通的移动和加固操作即可");
                break;
            default:
                showAlert("特殊能力", "此角色没有可用的特殊能力");
        }
    }
    
    private void performPilotFly() {
        if (gameBoardView == null) {
            showAlert("错误", "游戏板视图未初始化");
            return;
        }
        
        showInfoAlert("飞行员特殊能力", "选择要飞行到的任意板块");
        
        gameBoardView.setTileSelectionCallback(targetTile -> {
            Player currentPlayer = game.getCurrentPlayer();
            
            // 执行飞行
            currentPlayer.getPawn().moveTo(targetTile);
            System.out.println(currentPlayer.getName() + " (飞行员) 飞行到 " + targetTile.getName());
            useActionPoint();
            application.updateGameState();
            gameBoardView.clearSelection();
            gameBoardView.setTileSelectionCallback(null);
        });
    }
    
    private void performEngineerAbility() {
        showInfoAlert("工程师特殊能力", "可以花费1个行动点加固2个板块。先选择第一个板块");
        
        if (gameBoardView == null) {
            showAlert("错误", "游戏板视图未初始化");
            return;
        }
        
        final IslandTile[] selectedTiles = new IslandTile[2];
        final int[] index = {0};
        
        gameBoardView.setTileSelectionCallback(targetTile -> {
            Player currentPlayer = game.getCurrentPlayer();
            IslandTile currentTile = currentPlayer.getPawn().getCurrentLocation();
            
            // 检查是否是当前板块或相邻板块且已淹没
            boolean isCurrentOrAdjacent = targetTile == currentTile || isAdjacentTile(currentTile, targetTile);
            
            if (isCurrentOrAdjacent && targetTile.isFlooded()) {
                selectedTiles[index[0]] = targetTile;
                index[0]++;
                
                if (index[0] == 1) {
                    // 选择第二个板块
                    showInfoAlert("工程师特殊能力", "请选择第二个要加固的板块");
                } else {
                    // 执行加固
                    selectedTiles[0].shoreUp();
                    selectedTiles[1].shoreUp();
                    System.out.println(currentPlayer.getName() + " (工程师) 加固了 " + selectedTiles[0].getName() 
                                    + " 和 " + selectedTiles[1].getName());
                    useActionPoint();
                    application.updateGameState();
                    gameBoardView.clearSelection();
                    gameBoardView.setTileSelectionCallback(null);
                }
            } else if (!targetTile.isFlooded()) {
                showAlert("无效操作", "只能加固已淹没的板块");
            } else {
                showAlert("无效操作", "只能加固当前或相邻的板块");
            }
        });
    }
    
    private void performNavigatorAbility() {
        // 领航员能力 - 可以移动其他玩家
        if (gameBoardView == null) {
            showAlert("错误", "游戏板视图未初始化");
            return;
        }

        // 创建对话框选择目标玩家
        Dialog<Player> dialog = new Dialog<>();
        dialog.setTitle("领航员特殊能力");
        dialog.setHeaderText("选择要移动的玩家");
        
        // 设置按钮
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // 创建玩家下拉列表
        ComboBox<Player> playerComboBox = new ComboBox<>();
        
        // 获取可移动的玩家（当前玩家除外）
        List<Player> otherPlayers = new ArrayList<>();
        Player currentPlayer = game.getCurrentPlayer();
        
        for (Player player : game.getPlayers()) {
            if (player != currentPlayer) {
                otherPlayers.add(player);
            }
        }
        
        playerComboBox.getItems().addAll(otherPlayers);
        playerComboBox.setConverter(new javafx.util.StringConverter<Player>() {
            @Override
            public String toString(Player player) {
                return player.getName() + " (" + player.getRole() + ")";
            }
            
            @Override
            public Player fromString(String string) {
                return null;
            }
        });
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("选择玩家:"), 0, 0);
        grid.add(playerComboBox, 1, 0);
        
        dialog.getDialogPane().setContent(grid);
        
        // 转换结果
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return playerComboBox.getValue();
            }
            return null;
        });
        
        Optional<Player> result = dialog.showAndWait();
        
        result.ifPresent(targetPlayer -> {
            // 选择移动目标
            showInfoAlert("选择移动目标", "请点击要将" + targetPlayer.getName() + "移动到的目标板块");
            
            gameBoardView.setTileSelectionCallback(targetTile -> {
                IslandTile currentTile = targetPlayer.getPawn().getCurrentLocation();
                
                // 领航员可以将其他玩家移动最多2格
                boolean canMove = isWithinTwoTiles(currentTile, targetTile);
                
                if (canMove) {
                    targetPlayer.getPawn().moveTo(targetTile);
                    System.out.println(currentPlayer.getName() + " (领航员) 将 " + 
                            targetPlayer.getName() + " 移动到 " + targetTile.getName());
                    useActionPoint();
                    application.updateGameState();
                    gameBoardView.clearSelection();
                    gameBoardView.setTileSelectionCallback(null);
                } else {
                    showAlert("无效移动", "只能将玩家移动到2格以内的板块");
                }
            });
        });
    }
    
    private boolean isWithinTwoTiles(IslandTile currentTile, IslandTile targetTile) {
        // 获取坐标
        int[] currentCoords = game.getTileCoordinates(currentTile);
        int[] targetCoords = game.getTileCoordinates(targetTile);
        
        if (currentCoords == null || targetCoords == null) {
            return false;
        }
        
        int currentRow = currentCoords[0];
        int currentCol = currentCoords[1];
        int targetRow = targetCoords[0];
        int targetCol = targetCoords[1];
        
        // 计算曼哈顿距离
        int distance = Math.abs(currentRow - targetRow) + Math.abs(currentCol - targetCol);
        
        // 领航员可以移动最多2格
        return distance <= 2;
    }
    
    private void performDiverAbility() {
        // 潜水员能力 - 可以穿过淹没和沉没的板块
        if (gameBoardView == null) {
            showAlert("错误", "游戏板视图未初始化");
            return;
        }
        
        showInfoAlert("潜水员特殊能力", "选择要潜水到的板块（可以穿过淹没/沉没的板块）");
        
        gameBoardView.setTileSelectionCallback(targetTile -> {
            Player currentPlayer = game.getCurrentPlayer();
            
            // 潜水员的移动逻辑比较复杂，这里简化实现
            // 实际上应该检查是否能通过淹没/沉没的板块路径到达目标
            // 这里简化为只要目标是有效板块即可移动
            
            currentPlayer.getPawn().moveTo(targetTile);
            System.out.println(currentPlayer.getName() + " (潜水员) 潜水到 " + targetTile.getName());
            useActionPoint();
            application.updateGameState();
            gameBoardView.clearSelection();
            gameBoardView.setTileSelectionCallback(null);
        });
    }
    
    private void performMessengerAbility() {
        // 信使能力 - 可以给任何位置的玩家卡牌
        Player currentPlayer = game.getCurrentPlayer();
        
        if (currentPlayer.getHand().isEmpty()) {
            showAlert("无牌可给", "您当前没有卡牌可以交换");
            return;
        }
        
        // 创建对话框选择目标玩家
        Dialog<Player> dialog = new Dialog<>();
        dialog.setTitle("信使特殊能力");
        dialog.setHeaderText("选择要给予卡牌的目标玩家（可以在任何位置）");
        
        // 设置按钮
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // 创建玩家下拉列表
        ComboBox<Player> playerComboBox = new ComboBox<>();
        
        // 获取其他玩家
        List<Player> otherPlayers = new ArrayList<>();
        
        for (Player player : game.getPlayers()) {
            if (player != currentPlayer) {
                otherPlayers.add(player);
            }
        }
        
        playerComboBox.getItems().addAll(otherPlayers);
        playerComboBox.setConverter(new javafx.util.StringConverter<Player>() {
            @Override
            public String toString(Player player) {
                return player.getName() + " (" + player.getRole() + ")";
            }
            
            @Override
            public Player fromString(String string) {
                return null;
            }
        });
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("选择玩家:"), 0, 0);
        grid.add(playerComboBox, 1, 0);
        
        dialog.getDialogPane().setContent(grid);
        
        // 转换结果
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return playerComboBox.getValue();
            }
            return null;
        });
        
        Optional<Player> result = dialog.showAndWait();
        
        result.ifPresent(targetPlayer -> {
            // 执行交换卡牌逻辑
            // 简化实现：显示成功消息
            System.out.println(currentPlayer.getName() + " (信使) 给了 " + 
                    targetPlayer.getName() + " 一张卡牌");
            useActionPoint();
            application.updateGameState();
        });
    }
    
    private void endTurn() {
        // 结束当前回合
        game.runTurn();
        resetTurn();
        application.updateGameState();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
} 