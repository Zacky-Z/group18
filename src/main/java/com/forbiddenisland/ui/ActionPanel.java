package com.forbiddenisland.ui;

import com.forbiddenisland.model.Card;
import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.Player;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;
import com.forbiddenisland.ForbiddenIslandGame;
import java.util.Optional;

// 每回合3个动作
public class ActionPanel extends VBox {
    private Game game;
    private ForbiddenIslandGame application;
    private Label actionCountLabel;
    private Label waterLevelLabel;
    private Label treasureStatusLabel;
    private int remainingActions = 3;

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

        // 添加查看手牌按钮
        Button viewHandButton = new Button("查看手牌");
        viewHandButton.setOnAction(e -> showPlayerHand());
        getChildren().add(viewHandButton);

        initializeButtons();
        updateGameInfo();
    }

    private void showPlayerHand() {
        Player currentPlayer = game.getCurrentPlayer();
        java.util.List<Card> hand = currentPlayer.getHand();

        // 创建对话框
        Dialog<Card> dialog = new Dialog<>();
        dialog.setTitle("查看手牌");
        dialog.setHeaderText(currentPlayer.getName() + " 的手牌");

        // 设置按钮
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        // 创建手牌列表
        ListView<Card> handListView = new ListView<>();
        handListView.getItems().addAll(hand);
        handListView.setCellFactory(param -> new ListCell<Card>() {
            @Override
            protected void updateItem(Card card, boolean empty) {
                super.updateItem(card, empty);
                if (empty || card == null) {
                    setText(null);
                } else {
                    setText(card.getDescription());
                }
            }
        });

        // 将手牌列表添加到对话框
        dialog.getDialogPane().setContent(handListView);

        // 显示对话框
        dialog.showAndWait();
    }

    private void initializeButtons() {
        // 这里可以添加其他按钮的初始化代码
    }

    private void updateGameInfo() {
        // 这里可以添加更新游戏信息的代码
    }

    public void setGameBoardView(GameBoardView gameBoardView) {
        // 这里可以添加设置游戏板视图的代码
    }

    public void update() {
        // 这里可以添加更新面板的代码
    }

    public void disableActions() {
        // 这里可以添加禁用动作的代码
    }
}