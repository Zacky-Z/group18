package com.forbiddenisland;

import com.forbiddenisland.model.*;
import com.forbiddenisland.ui.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Forbidden Island Game main application class.
 * 禁闭岛游戏主应用程序类。
 */
public class ForbiddenIslandGame extends Application {

    private Game game;
    private GameBoardView gameBoardView;
    private PlayerInfoPanel playerInfoPanel;
    private ActionPanel actionPanel;
    private StatusPanel statusPanel;
    private WaterMeterView waterMeterView;

    // 添加标志，用于防止重复显示游戏结束对话框
    private boolean isGameOver = false;

    @Override
    public void start(Stage primaryStage) {
        // 创建初始界面
        createInitialScreen(primaryStage);
    }

    private void createInitialScreen(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #1E90FF);");

        // 创建标题
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        Label titleLabel = new Label("禁闭岛");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        titleLabel.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.BLACK));
        
        Label subtitleLabel = new Label("Forbidden Island");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        subtitleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);
        root.setTop(titleBox);
        BorderPane.setAlignment(titleBox, javafx.geometry.Pos.CENTER);
        BorderPane.setMargin(titleBox, new Insets(0, 0, 50, 0));

        // 创建按钮面板
        VBox buttonPanel = new VBox(20);
        buttonPanel.setAlignment(javafx.geometry.Pos.CENTER);
        buttonPanel.setPadding(new Insets(20));
        buttonPanel.setMaxWidth(300);
        buttonPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 10;");

        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                             "-fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;";
        String exitButtonStyle = "-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 16px; " +
                                "-fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;";

        Button startButton = new Button("开始游戏");
        startButton.setStyle(buttonStyle);
        startButton.setMaxWidth(Double.MAX_VALUE);
        startButton.setOnAction(e -> createDifficultyScreen(primaryStage));

        Button exitButton = new Button("退出");
        exitButton.setStyle(exitButtonStyle);
        exitButton.setMaxWidth(Double.MAX_VALUE);
        exitButton.setOnAction(e -> primaryStage.close());

        buttonPanel.getChildren().addAll(startButton, exitButton);
        root.setCenter(buttonPanel);

        // 添加游戏简介
        Label infoLabel = new Label("禁闭岛是一款合作类桌游，玩家们需要共同合作收集四件宝藏并全员撤离岛屿。");
        infoLabel.setWrapText(true);
        infoLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        infoLabel.setFont(Font.font("Arial", 14));
        root.setBottom(infoLabel);
        BorderPane.setAlignment(infoLabel, javafx.geometry.Pos.CENTER);
        BorderPane.setMargin(infoLabel, new Insets(30, 0, 0, 0));

        // 设置场景
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("禁闭岛游戏 (Forbidden Island Game)");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void createDifficultyScreen(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #1E90FF);");

        // 创建标题
        Label titleLabel = new Label("选择难度");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        titleLabel.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.BLACK));
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, javafx.geometry.Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 30, 0));

        // 创建难度选择面板
        VBox difficultyPanel = new VBox(15);
        difficultyPanel.setPadding(new Insets(20));
        difficultyPanel.setAlignment(javafx.geometry.Pos.CENTER);
        difficultyPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 10;");
        difficultyPanel.setMaxWidth(400);

        ToggleGroup difficultyGroup = new ToggleGroup();
        
        String radioStyle = "-fx-font-size: 16px; -fx-text-fill: white;";

        RadioButton noviceButton = new RadioButton("新手 (难度 1)");
        noviceButton.setStyle(radioStyle);
        noviceButton.setToggleGroup(difficultyGroup);
        noviceButton.setSelected(true);
        noviceButton.setContentDisplay(javafx.scene.control.ContentDisplay.RIGHT);

        RadioButton normalButton = new RadioButton("正常 (难度 2)");
        normalButton.setStyle(radioStyle);
        normalButton.setToggleGroup(difficultyGroup);

        RadioButton expertButton = new RadioButton("精英 (难度 3)");
        expertButton.setStyle(radioStyle);
        expertButton.setToggleGroup(difficultyGroup);

        RadioButton legendButton = new RadioButton("传奇 (难度 4)");
        legendButton.setStyle(radioStyle);
        legendButton.setToggleGroup(difficultyGroup);

        // 添加难度说明
        Label descriptionLabel = new Label("选择游戏难度将影响初始水位和游戏进程");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        descriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        
        // 按钮样式
        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                            "-fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;";
        
        Button startGameButton = new Button("下一步");
        startGameButton.setStyle(buttonStyle);
        startGameButton.setMaxWidth(200);
        startGameButton.setOnAction(e -> {
            int difficulty = 1;
            if (normalButton.isSelected()) {
                difficulty = 2;
            } else if (expertButton.isSelected()) {
                difficulty = 3;
            } else if (legendButton.isSelected()) {
                difficulty = 4;
            }
            createRoleSelectionScreen(primaryStage, difficulty);
        });

        difficultyPanel.getChildren().addAll(
            descriptionLabel, 
            new javafx.scene.control.Separator(),
            noviceButton, normalButton, expertButton, legendButton, 
            new javafx.scene.control.Separator(),
            startGameButton
        );
        root.setCenter(difficultyPanel);

        // 设置场景
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("选择难度 - 禁闭岛游戏 (Forbidden Island Game)");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    private void createRoleSelectionScreen(Stage primaryStage, int difficulty) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #1E90FF);");

        Label titleLabel = new Label("角色选择");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        titleLabel.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.BLACK));
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, javafx.geometry.Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 30, 0));

        VBox selectionPanel = new VBox(15);
        selectionPanel.setPadding(new Insets(20));
        selectionPanel.setAlignment(javafx.geometry.Pos.CENTER);
        selectionPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 10;");
        selectionPanel.setMaxWidth(400);

        ToggleGroup selectionGroup = new ToggleGroup();
        
        String radioStyle = "-fx-font-size: 16px; -fx-text-fill: white;";

        RadioButton randomButton = new RadioButton("随机分配角色 (官方规则)");
        randomButton.setStyle(radioStyle);
        randomButton.setToggleGroup(selectionGroup);
        randomButton.setSelected(true);

        RadioButton chooseButton = new RadioButton("自选角色");
        chooseButton.setStyle(radioStyle);
        chooseButton.setToggleGroup(selectionGroup);
        
        // 添加说明文本
        Label descriptionLabel = new Label("选择角色分配方式。官方规则为随机分配，或者可以手动选择每个玩家的角色。");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        descriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        
        // 按钮样式
        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                            "-fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;";

        Button continueButton = new Button("继续");
        continueButton.setStyle(buttonStyle);
        continueButton.setMaxWidth(200);
        continueButton.setOnAction(e -> {
            if (randomButton.isSelected()) {
                startGame(primaryStage, difficulty);
            } else {
                createPlayerRoleSelectionScreen(primaryStage, difficulty);
            }
        });

        selectionPanel.getChildren().addAll(
            descriptionLabel,
            new javafx.scene.control.Separator(),
            randomButton, chooseButton,
            new javafx.scene.control.Separator(),
            continueButton
        );
        root.setCenter(selectionPanel);

        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("角色选择 - 禁闭岛游戏 (Forbidden Island Game)");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    private void createPlayerRoleSelectionScreen(Stage primaryStage, int difficulty) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label titleLabel = new Label("选择角色");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        root.setTop(titleLabel);

        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(10));

        // 创建玩家名称列表
        List<String> playerNames = Arrays.asList("玩家1", "玩家2", "玩家3", "玩家4");
        
        // 创建角色选择下拉框
        List<AdventurerRole> availableRoles = new ArrayList<>(Arrays.asList(AdventurerRole.values()));
        List<javafx.scene.control.ComboBox<AdventurerRole>> roleSelectors = new ArrayList<>();
        
        for (int i = 0; i < playerNames.size(); i++) {
            HBox playerBox = new HBox(10);
            playerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            
            Label playerLabel = new Label(playerNames.get(i) + ":");
            playerLabel.setMinWidth(60);
            
            javafx.scene.control.ComboBox<AdventurerRole> roleSelector = new javafx.scene.control.ComboBox<>();
            roleSelector.getItems().addAll(availableRoles);
            roleSelector.setValue(availableRoles.get(i % availableRoles.size())); // 默认选择
            roleSelector.setMinWidth(200);
            
            // 设置角色描述显示
            roleSelector.setCellFactory(param -> new javafx.scene.control.ListCell<AdventurerRole>() {
                @Override
                protected void updateItem(AdventurerRole item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.name() + " - " + item.getDescription().split("\\(")[0].trim());
                    }
                }
            });
            
            roleSelector.setButtonCell(new javafx.scene.control.ListCell<AdventurerRole>() {
                @Override
                protected void updateItem(AdventurerRole item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.name() + " - " + item.getDescription().split("\\(")[0].trim());
                    }
                }
            });
            
            // 当一个角色被选择后，从其他下拉框中移除该角色（防止重复选择）
            roleSelector.setOnAction(e -> {
                AdventurerRole selectedRole = roleSelector.getValue();
                if (selectedRole != null) {
                    for (javafx.scene.control.ComboBox<AdventurerRole> otherSelector : roleSelectors) {
                        if (otherSelector != roleSelector && otherSelector.getValue() == selectedRole) {
                            // 如果其他选择器选择了相同的角色，找一个可用的角色替换
                            List<AdventurerRole> availableForOther = new ArrayList<>(availableRoles);
                            for (javafx.scene.control.ComboBox<AdventurerRole> selector : roleSelectors) {
                                availableForOther.remove(selector.getValue());
                            }
                            if (!availableForOther.isEmpty()) {
                                otherSelector.setValue(availableForOther.get(0));
                            }
                        }
                    }
                }
            });
            
            roleSelectors.add(roleSelector);
            playerBox.getChildren().addAll(playerLabel, roleSelector);
            contentBox.getChildren().add(playerBox);
        }
        
        Button startGameButton = new Button("开始游戏");
        startGameButton.setOnAction(e -> {
            // 收集选择的角色
            List<AdventurerRole> selectedRoles = new ArrayList<>();
            for (javafx.scene.control.ComboBox<AdventurerRole> selector : roleSelectors) {
                selectedRoles.add(selector.getValue());
            }
            
            // 使用选择的角色初始化游戏
            initializeGameWithSelectedRoles(primaryStage, difficulty, playerNames, selectedRoles);
        });
        
        contentBox.getChildren().add(startGameButton);
        root.setCenter(contentBox);
        
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("选择角色 - 禁闭岛游戏 (Forbidden Island Game)");
        primaryStage.setScene(scene);
    }

    private void initializeGameWithSelectedRoles(Stage primaryStage, int difficulty, List<String> playerNames, List<AdventurerRole> selectedRoles) {
        // 创建游戏实例，并传入选择的角色
        game = new Game(playerNames, difficulty, selectedRoles);
        setupMainGameUI(primaryStage);
    }

    private void startGame(Stage primaryStage, int difficulty) {
        // 创建游戏实例 (随机分配角色)
        List<String> playerNames = Arrays.asList("玩家1", "玩家2", "玩家3", "玩家4"); // Default player names
        game = new Game(playerNames, difficulty); // Constructor for random roles
        setupMainGameUI(primaryStage);
    }

    private void setupMainGameUI(Stage primaryStage) {
        BorderPane gameLayout = new BorderPane();
        gameLayout.setPadding(new Insets(15));
        gameLayout.setStyle("-fx-background-color: #f0f8ff;"); // 添加淡蓝色背景

        // 初始化UI组件
        gameBoardView = new GameBoardView(game);
        statusPanel = new StatusPanel();
        playerInfoPanel = new PlayerInfoPanel(game);
        actionPanel = new ActionPanel(game, this);
        actionPanel.setGameBoardView(gameBoardView);
        waterMeterView = new WaterMeterView(game.getWaterMeter());

        // 设置最小宽度，确保面板不会被挤压
        playerInfoPanel.setMinWidth(280);
        actionPanel.setMinWidth(280);
        waterMeterView.setMinWidth(200);
        
        // 为面板添加样式
        playerInfoPanel.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ddd; -fx-border-radius: 5;");
        actionPanel.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ddd; -fx-border-radius: 5;");
        statusPanel.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ddd; -fx-border-radius: 5;");

        // 创建右侧面板，包含操作面板和水位计
        VBox rightPanel = new VBox(15);
        rightPanel.getChildren().addAll(actionPanel, waterMeterView);
        
        // 布局UI组件
        gameLayout.setCenter(gameBoardView);
        gameLayout.setLeft(playerInfoPanel);
        gameLayout.setRight(rightPanel);
        gameLayout.setBottom(statusPanel);

        // 设置边距
        BorderPane.setMargin(gameBoardView, new Insets(10));
        BorderPane.setMargin(playerInfoPanel, new Insets(10));
        BorderPane.setMargin(rightPanel, new Insets(10));
        BorderPane.setMargin(statusPanel, new Insets(10, 0, 0, 0));

        // 菜单栏
        HBox menuBar = new HBox(10);
        menuBar.setPadding(new Insets(10));
        menuBar.setStyle("-fx-background-color: #e6e6e6; -fx-border-color: #ccc; -fx-border-radius: 5;");
        menuBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        // 美化按钮样式
        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;";
        String exitButtonStyle = "-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;";
        
        Button restartButton = new Button("重新开始");
        restartButton.setStyle(buttonStyle);
        restartButton.setOnAction(e -> restartGame());
        
        Button helpButton = new Button("游戏帮助");
        helpButton.setStyle(buttonStyle);
        helpButton.setOnAction(e -> showGameHelp());
        
        Button saveButton = new Button("保存游戏");
        saveButton.setStyle(buttonStyle);
        saveButton.setOnAction(e -> saveGame(primaryStage));
        
        Button loadButton = new Button("加载游戏");
        loadButton.setStyle(buttonStyle);
        loadButton.setOnAction(e -> loadGame(primaryStage));
        
        Button exitButton = new Button("退出游戏");
        exitButton.setStyle(exitButtonStyle);
        exitButton.setOnAction(e -> primaryStage.close());
        
        menuBar.getChildren().addAll(restartButton, helpButton, saveButton, loadButton, exitButton);
        gameLayout.setTop(menuBar);

        // 创建场景并设置更大的初始尺寸
        Scene scene = new Scene(gameLayout, 1400, 900);
        primaryStage.setTitle("禁闭岛 (Forbidden Island)");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        
        // 设置窗口最小尺寸，确保UI元素不会被挤压
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(800);

        // 最大化窗口
        primaryStage.setMaximized(true);

        updateGameState(); // Initial UI update
    }

    /**
     * 更新游戏状态并刷新界面
     */
    public void updateGameState() {
        if (game == null) return;

        if (gameBoardView != null) gameBoardView.update();
        
        if (playerInfoPanel != null) playerInfoPanel.update();
        
        if (actionPanel != null) actionPanel.update();

        if (waterMeterView != null) waterMeterView.update();
        
        if (statusPanel != null) {
            statusPanel.setStatus(game.getCurrentPlayer().getName() + " 的回合. 水位: " + game.getWaterMeter().getWaterLevelLabel());
        }

        // 检查游戏结束条件并确定结束原因
        if (!isGameOver && game.checkGameOverConditions()) {
            isGameOver = true; // 设置标志，防止重复显示对话框
            String gameOverReason = determineGameOverReason();
            showGameOverDialog(false, "游戏结束！失败了。", gameOverReason);
        } else if (!isGameOver && game.checkWinConditions()) {
            isGameOver = true; // 设置标志，防止重复显示对话框
            showGameOverDialog(true, "游戏结束！胜利！", "恭喜！你们成功收集了所有宝藏并全员撤离了禁闭岛！");
        }
    }

    /**
     * 确定游戏结束的具体原因
     * @return 游戏结束原因的描述
     */
    private String determineGameOverReason() {
        System.out.println("正在确定游戏结束原因...");
        
        // 检查水位是否达到最高
        if (game.getWaterMeter().hasReachedMaxLevel()) {
            String reason = "水位已达到最高点！岛屿完全被淹没了。";
            System.out.println("游戏结束原因: " + reason);
            return reason;
        }
        
        // 检查愚者起飞点是否沉没
        if (game.getIslandTileByName("Fools' Landing") == null) {
            String reason = "愚者起飞点（Fools' Landing）已沉没！无法撤离岛屿。";
            System.out.println("游戏结束原因: " + reason);
            return reason;
        }
        
        // 检查是否有宝藏无法获取
        for (Treasure treasure : game.getTreasures()) {
            if (!treasure.isCollected()) {
                int sunkTreasureTiles = 0;
                for (String tileName : treasure.getIslandTileNames()) {
                    if (game.getIslandTileByName(tileName) == null) {
                        sunkTreasureTiles++;
                    }
                }
                if (sunkTreasureTiles >= 2) {
                    String reason = treasure.getType().getDisplayName() + "的两个板块都已沉没！无法收集该宝藏。";
                    System.out.println("游戏结束原因: " + reason);
                    return reason;
                }
            }
        }
        
        // 检查是否有玩家无法移动到安全位置
        for (Player player : game.getPlayers()) {
            if (player.getPawn().getCurrentLocation() == null) {
                String reason = player.getName() + "(" + player.getRole().getChineseName() + ")无法移动到安全位置！";
                System.out.println("游戏结束原因: " + reason);
                return reason;
            }
        }
        
        String reason = "未知原因导致游戏结束。";
        System.out.println("游戏结束原因: " + reason);
        return reason;
    }

    /**
     * 显示游戏结束对话框
     * @param isWin 是否胜利
     * @param message 结束信息
     * @param reason 游戏结束的具体原因
     */
    private void showGameOverDialog(boolean isWin, String message, String reason) {
        if (statusPanel != null) statusPanel.setStatus(message);
        if (actionPanel != null) actionPanel.disableAllButtons();

        // 直接创建对话框，不使用Platform.runLater
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(isWin ? "胜利！" : "游戏结束");
        alert.setHeaderText(message);
        
        // 创建一个文本区域来显示详细信息
        javafx.scene.control.TextArea textArea = new javafx.scene.control.TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefHeight(150);
        
        // 设置对话框内容，包括游戏结束原因
        String content = reason + "\n\n";
        
        if (isWin) {
            content += "你们成功地完成了禁闭岛的挑战！";
        } else {
            content += "禁闭岛的挑战失败了...";
        }
        
        textArea.setText(content);
        
        // 创建一个显示文本和按钮的容器
        javafx.scene.layout.VBox dialogContent = new javafx.scene.layout.VBox(10);
        dialogContent.getChildren().add(textArea);
        
        // 添加按钮
        javafx.scene.layout.HBox buttonBox = new javafx.scene.layout.HBox(10);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        javafx.scene.control.Button restartButton = new javafx.scene.control.Button("重新开始");
        javafx.scene.control.Button exitButton = new javafx.scene.control.Button("退出");
        
        restartButton.setOnAction(e -> {
            alert.close();
            restartGame();
        });
        
        exitButton.setOnAction(e -> {
            alert.close();
            Node anyNode = null;
            if (gameBoardView != null) anyNode = gameBoardView;
            else if (playerInfoPanel != null) anyNode = playerInfoPanel;
            else if (actionPanel != null) anyNode = actionPanel;
            else if (statusPanel != null) anyNode = statusPanel;

            if (anyNode != null && anyNode.getScene() != null && anyNode.getScene().getWindow() != null) {
                ((Stage) anyNode.getScene().getWindow()).close();
            } else {
                System.exit(0);
            }
        });
        
        buttonBox.getChildren().addAll(restartButton, exitButton);
        dialogContent.getChildren().add(buttonBox);
        
        // 设置对话框内容
        alert.getDialogPane().setContent(dialogContent);
        
        // 移除默认按钮
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(javafx.scene.control.ButtonType.CLOSE);
        
        // 隐藏关闭按钮
        javafx.scene.Node closeButton = alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.CLOSE);
        closeButton.setVisible(false);
        closeButton.setManaged(false);
        
        // 使对话框更大一些，以便显示更多内容
        alert.getDialogPane().setPrefWidth(500);
        alert.getDialogPane().setPrefHeight(300);
        
        System.out.println("显示游戏结束对话框: " + message + " - " + reason);
        
        // 显示对话框
        alert.show();
    }

    /**
     * 重新开始游戏
     */
    private void restartGame() {
        // 清除舞台上的所有内容，完全重建
        Stage primaryStage = (Stage) gameBoardView.getScene().getWindow();

        // 重置游戏结束标志
        isGameOver = false;
        
        // 完全重新创建游戏实例和UI组件
        start(primaryStage);
    }

    /**
     * 显示游戏帮助对话框
     */
    private void showGameHelp() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);

        alert.setTitle("禁闭岛游戏帮助");
        alert.setHeaderText("游戏规则与操作说明");

        String content =
                "禁闭岛是一个合作类桌游，玩家们需要共同合作收集四件宝藏并全员撤离岛屿。\n\n" +
                        "游戏操作:\n" +
                        "1. 每个玩家每回合有3个行动点\n" +
                        "2. 点击板块可以选择要移动到的位置或要加固的板块\n" +
                        "3. 每个玩家有特殊能力，可以通过\"特殊能力\"按钮使用\n\n" +
                        "角色说明:\n" +
                        "- 飞行员: 可以飞到任意板块\n" +
                        "- 工程师: 可以一次加固两个板块\n" +
                        "- 领航员: 可以移动其他玩家\n" +
                        "- 潜水员: 可以穿过已淹没的板块\n" +
                        "- 信使: 可以给任何位置的玩家卡牌\n" +
                        "- 探险家: 可以斜向移动和加固\n\n" +
                        "游戏目标:\n" +
                        "收集全部四个宝藏并让所有玩家撤离到愚人号起飞点。";

        alert.setContentText(content);
        alert.showAndWait();
    }

    private void saveGame(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存游戏");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game Save Files", "*.sav"));
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            game.saveGame(file.getAbsolutePath());
        }
    }

    private void loadGame(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("加载游戏");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game Save Files", "*.sav"));
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            game = GameLoader.loadGame(file.getAbsolutePath());
            if (game != null) {
                gameBoardView.setGame(game);
                playerInfoPanel.setGame(game);
                actionPanel.setGame(game);
                updateGameState();
            }
        }
    }

    public StatusPanel getStatusPanel() {
        return statusPanel;
    }

    public void setGamePhase(Game.GamePhase phase) {
        if (game != null) {
            game.setCurrentPhase(phase);
            actionPanel.update();
        }
    }
}