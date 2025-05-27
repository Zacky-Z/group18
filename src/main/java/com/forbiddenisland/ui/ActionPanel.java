package com.forbiddenisland.ui;

import com.forbiddenisland.ForbiddenIslandGame;
import com.forbiddenisland.model.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.forbiddenisland.model.GameMode;

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
    private Button specialAbilityButton;
    private Button endTurnButton;
    private VBox actionButtonsBox;
    private ScrollPane scrollPane;

    public ActionPanel(Game game, ForbiddenIslandGame mainApp) {
        this.game = game;
        this.mainApp = mainApp;

        // 设置面板样式
        setPadding(new Insets(10));
        setSpacing(10);
        setPrefWidth(250);
        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999; -fx-border-width: 1px;");

        // 创建标题
        Label titleLabel = new Label("行动面板");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16)); // 修改为使用 FontWeight.BOLD
        getChildren().add(titleLabel);

// 修改显示当前行动点数的部分
        actionPointsLabel = new Label("行动点数: " + game.getCurrentPlayer().getActionPoints());
        actionPointsLabel.setFont(Font.font("Arial", 14));
        getChildren().add(actionPointsLabel);

        // 创建可滚动的按钮面板
        actionButtonsBox = new VBox(8);
        actionButtonsBox.setPadding(new Insets(5));

        scrollPane = new ScrollPane(actionButtonsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200); // 设置初始高度，但允许滚动
        scrollPane.setStyle("-fx-background-color: transparent;");

        getChildren().add(scrollPane);

        // 创建行动按钮
        createActionButtons();

        // 添加弃牌区域
        createDiscardArea();
    }

    private void createActionButtons() {
        // 移动按钮
        moveButton = new Button("移动");
        moveButton.setPrefWidth(200);
        moveButton.setOnAction(e -> handleMoveAction());

        // 加固按钮
        shoreUpButton = new Button("加固");
        shoreUpButton.setPrefWidth(200);
        shoreUpButton.setOnAction(e -> handleShoreUpAction());

        // 给卡按钮
        giveCardButton = new Button("给予卡牌");
        giveCardButton.setPrefWidth(200);
        giveCardButton.setOnAction(e -> handleGiveCardAction());

        // 收集宝藏按钮
        captureTreasureButton = new Button("收集宝藏");
        captureTreasureButton.setPrefWidth(200);
        captureTreasureButton.setOnAction(e -> handleCaptureTreasureAction());

        // 特殊能力按钮
        specialAbilityButton = new Button("特殊能力");
        specialAbilityButton.setPrefWidth(200);
        specialAbilityButton.setOnAction(e -> handleSpecialAbilityAction());

        // 结束回合按钮
        endTurnButton = new Button("结束回合");
        endTurnButton.setPrefWidth(200);
        endTurnButton.setOnAction(e -> handleEndTurnAction());

        // 添加所有按钮到面板
        actionButtonsBox.getChildren().addAll(
                moveButton, shoreUpButton, giveCardButton,
                captureTreasureButton, specialAbilityButton, endTurnButton
        );
    }

    private void createDiscardArea() {
        Label discardLabel = new Label("手牌");
        discardLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // 修改为使用 FontWeight.BOLD
        getChildren().add(discardLabel);

        // 这里简化处理，实际应显示玩家手牌
        TextArea handCardsArea = new TextArea();
        handCardsArea.setEditable(false);
        handCardsArea.setPrefRowCount(6);
        handCardsArea.setPrefColumnCount(15);
        handCardsArea.setWrapText(true);
        handCardsArea.setStyle("-fx-font-size: 12px;");
        getChildren().add(handCardsArea);

        // 添加弃牌按钮
        Button discardButton = new Button("弃牌");
        discardButton.setPrefWidth(200);
        discardButton.setOnAction(e -> handleDiscardAction());
        getChildren().add(discardButton);
    }

    // 处理各种行动的方法
    private void handleMoveAction() {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer.getActionPoints() > 0) {
            gameBoardView.setGameMode(GameMode.MOVE);
            actionPointsLabel.setText("行动点数: " + currentPlayer.getActionPoints());
        } else {
            showMessage("没有足够的行动点数！");
        }
    }

    private void handleShoreUpAction() {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer.getActionPoints() > 0) {
            gameBoardView.setGameMode(GameMode.SHORE_UP);
            actionPointsLabel.setText("行动点数: " + currentPlayer.getActionPoints());
        } else {
            showMessage("没有足够的行动点数！");
        }
    }

    private void handleGiveCardAction() {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer.getActionPoints() > 0) {
            gameBoardView.setGameMode(GameMode.GIVE_CARD);
            actionPointsLabel.setText("行动点数: " + currentPlayer.getActionPoints());
        } else {
            showMessage("没有足够的行动点数！");
        }
    }

    private void handleCaptureTreasureAction() {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer.getActionPoints() > 0) {
            gameBoardView.setGameMode(GameMode.CAPTURE_TREASURE);
            actionPointsLabel.setText("行动点数: " + currentPlayer.getActionPoints());
        } else {
            showMessage("没有足够的行动点数！");
        }
    }

    private void handleSpecialAbilityAction() {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer.getActionPoints() > 0) {
            // 特殊能力逻辑
            showMessage("使用特殊能力: " + currentPlayer.getRole().getDescription());
            gameBoardView.setGameMode(GameMode.SPECIAL_ABILITY);
            actionPointsLabel.setText("行动点数: " + currentPlayer.getActionPoints());
        } else {
            showMessage("没有足够的行动点数！");
        }
    }

    private void handleEndTurnAction() {
        game.endTurn();
        mainApp.updateGameState();
    }

    private void handleDiscardAction() {
        showMessage("弃牌功能将在后续版本中实现");
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
    }

    /**
     * 更新面板显示
     */
    public void update() {
        Player currentPlayer = game.getCurrentPlayer();
        actionPointsLabel.setText("行动点数: " + currentPlayer.getActionPoints());

        // 根据当前游戏状态启用/禁用按钮
        boolean canAct = currentPlayer.getActionPoints() > 0;
        moveButton.setDisable(!canAct);
        shoreUpButton.setDisable(!canAct);
        giveCardButton.setDisable(!canAct);
        captureTreasureButton.setDisable(!canAct);
        specialAbilityButton.setDisable(!canAct);
    }

    /**
     * 禁用所有行动按钮
     */
    public void disableActions() {
        moveButton.setDisable(true);
        shoreUpButton.setDisable(true);
        giveCardButton.setDisable(true);
        captureTreasureButton.setDisable(true);
        specialAbilityButton.setDisable(true);
        endTurnButton.setDisable(true);
    }
}
