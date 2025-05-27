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

    @Override
    public void start(Stage primaryStage) {
        // 创建初始界面
        createInitialScreen(primaryStage);
    }

    private void createInitialScreen(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // 创建标题
        Label titleLabel = new Label("禁闭岛 (Forbidden Island)");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        root.setTop(titleLabel);

        // 创建按钮面板
        VBox buttonPanel = new VBox(10);
        buttonPanel.setPadding(new Insets(10));

        Button startButton = new Button("开始游戏");
        startButton.setOnAction(e -> createDifficultyScreen(primaryStage));

        Button exitButton = new Button("退出");
        exitButton.setOnAction(e -> primaryStage.close());

        buttonPanel.getChildren().addAll(startButton, exitButton);
        root.setCenter(buttonPanel);

        // 设置场景
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("禁闭岛游戏 (Forbidden Island Game)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createDifficultyScreen(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // 创建标题
        Label titleLabel = new Label("选择难度");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        root.setTop(titleLabel);

        // 创建难度选择面板
        VBox difficultyPanel = new VBox(10);
        difficultyPanel.setPadding(new Insets(10));

        ToggleGroup difficultyGroup = new ToggleGroup();

        RadioButton noviceButton = new RadioButton("新手 (难度 1)");
        noviceButton.setToggleGroup(difficultyGroup);
        noviceButton.setSelected(true);

        RadioButton normalButton = new RadioButton("正常 (难度 2)");
        normalButton.setToggleGroup(difficultyGroup);

        RadioButton expertButton = new RadioButton("精英 (难度 3)");
        expertButton.setToggleGroup(difficultyGroup);

        RadioButton legendButton = new RadioButton("传奇 (难度 4)");
        legendButton.setToggleGroup(difficultyGroup);

        Button startGameButton = new Button("下一步");
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

        difficultyPanel.getChildren().addAll(noviceButton, normalButton, expertButton, legendButton, startGameButton);
        root.setCenter(difficultyPanel);

        // 设置场景
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("选择难度 - 禁闭岛游戏 (Forbidden Island Game)");
        primaryStage.setScene(scene);
    }

    private void createRoleSelectionScreen(Stage primaryStage, int difficulty) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label titleLabel = new Label("角色选择");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        root.setTop(titleLabel);

        VBox selectionPanel = new VBox(10);
        selectionPanel.setPadding(new Insets(10));

        ToggleGroup selectionGroup = new ToggleGroup();

        RadioButton randomButton = new RadioButton("随机分配角色 (官方规则)");
        randomButton.setToggleGroup(selectionGroup);
        randomButton.setSelected(true);

        RadioButton chooseButton = new RadioButton("自选角色");
        chooseButton.setToggleGroup(selectionGroup);

        Button continueButton = new Button("继续");
        continueButton.setOnAction(e -> {
            if (randomButton.isSelected()) {
                startGame(primaryStage, difficulty);
            } else {
                createPlayerRoleSelectionScreen(primaryStage, difficulty);
            }
        });

        selectionPanel.getChildren().addAll(randomButton, chooseButton, continueButton);
        root.setCenter(selectionPanel);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("角色选择 - 禁闭岛游戏 (Forbidden Island Game)");
        primaryStage.setScene(scene);
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
        gameLayout.setPadding(new Insets(10));

        // 初始化UI组件
        gameBoardView = new GameBoardView(game);
        statusPanel = new StatusPanel();
        playerInfoPanel = new PlayerInfoPanel(game);
        actionPanel = new ActionPanel(game, this);
        actionPanel.setGameBoardView(gameBoardView);

        // 布局UI组件
        gameLayout.setCenter(gameBoardView);
        gameLayout.setLeft(playerInfoPanel);
        gameLayout.setRight(actionPanel);
        gameLayout.setBottom(statusPanel);

        // 菜单栏
        HBox menuBar = new HBox(10);
        menuBar.setPadding(new Insets(5));
        Button restartButton = new Button("重新开始");
        restartButton.setOnAction(e -> restartGame());
        Button helpButton = new Button("游戏帮助");
        helpButton.setOnAction(e -> showGameHelp());
        Button saveButton = new Button("保存游戏");
        saveButton.setOnAction(e -> saveGame(primaryStage));
        Button loadButton = new Button("加载游戏");
        loadButton.setOnAction(e -> loadGame(primaryStage));
        Button exitButton = new Button("退出游戏");
        exitButton.setOnAction(e -> primaryStage.close());
        menuBar.getChildren().addAll(restartButton, helpButton, saveButton, loadButton, exitButton);
        gameLayout.setTop(menuBar);

        Scene scene = new Scene(gameLayout, 1200, 800);
        primaryStage.setTitle("禁闭岛 (Forbidden Island)");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);

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
        
        if (statusPanel != null) {
            statusPanel.setStatus(game.getCurrentPlayer().getName() + " 的回合. 水位: " + game.getWaterMeter().getWaterLevelLabel());
        }

        if (game.checkGameOverConditions()) {
            showGameOverDialog(false, "游戏结束！失败了。");
        } else if (game.checkWinConditions()) {
            showGameOverDialog(true, "游戏结束！胜利！");
        }
    }

    /**
     * 显示游戏结束对话框
     * @param isWin 是否胜利
     * @param message 结束信息
     */
    private void showGameOverDialog(boolean isWin, String message) {
        if (statusPanel != null) statusPanel.setStatus(message);
        if (actionPanel != null) actionPanel.disableAllButtons();

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(isWin ? "胜利！" : "游戏结束");
        alert.setHeaderText(message);
        alert.setContentText("点击确定重新开始游戏或退出。");

        javafx.scene.control.ButtonType restartButtonType = new javafx.scene.control.ButtonType("重新开始");
        javafx.scene.control.ButtonType exitButtonType = new javafx.scene.control.ButtonType("退出", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(restartButtonType, exitButtonType);

        alert.showAndWait().ifPresent(type -> {
            if (type == restartButtonType) {
                restartGame();
            } else {
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
            }
        });
    }

    /**
     * 重新开始游戏
     */
    private void restartGame() {
        // 清除舞台上的所有内容，完全重建
        Stage primaryStage = (Stage) gameBoardView.getScene().getWindow();

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
