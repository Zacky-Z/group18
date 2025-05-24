package com.forbiddenisland;

import com.forbiddenisland.model.*;
import com.forbiddenisland.ui.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

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

    /**
     * The main entry point for all JavaFX applications.
     * 所有 JavaFX 应用程序的主入口点。
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     *                     应用程序的主舞台，应用程序场景可以设置在此舞台上。
     */
    @Override
    public void start(Stage primaryStage) {
        // 初始化游戏
        List<String> playerNames = Arrays.asList("玩家1", "玩家2", "玩家3", "玩家4");
        game = new Game(playerNames, 1); // 新手难度

        // 创建主布局
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // 创建标题和工具栏
        HBox titleBar = new HBox(15);
        titleBar.setPadding(new Insets(10));
        titleBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("禁闭岛 (Forbidden Island)");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Button helpButton = new Button("游戏帮助");
        helpButton.setOnAction(e -> showGameHelp());
        
        titleBar.getChildren().addAll(titleLabel, helpButton);
        root.setTop(titleBar);

        // 创建游戏板视图
        gameBoardView = new GameBoardView(game);
        root.setCenter(gameBoardView);

        // 创建右侧面板 - 玩家信息和操作
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));

        playerInfoPanel = new PlayerInfoPanel(game);
        actionPanel = new ActionPanel(game, this);
        
        // 连接游戏板视图和操作面板
        actionPanel.setGameBoardView(gameBoardView);
        
        rightPanel.getChildren().addAll(playerInfoPanel, actionPanel);
        root.setRight(rightPanel);

        // 创建底部状态面板
        statusPanel = new StatusPanel();
        root.setBottom(statusPanel);

        // 设置场景
        Scene scene = new Scene(root, 1024, 768);
        
        // 设置窗口标题
        primaryStage.setTitle("禁闭岛游戏 (Forbidden Island Game)");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // 更新状态
        updateGameState();
    }

    /**
     * 更新游戏状态并刷新界面
     */
    public void updateGameState() {
        gameBoardView.update();
        playerInfoPanel.update();
        actionPanel.update();
        
        Player currentPlayer = game.getCurrentPlayer();
        statusPanel.setStatus("当前玩家: " + currentPlayer.getName() + " (" + 
                     currentPlayer.getRole().getDescription() + ")");
                     
        // 检查游戏是否结束
        if (game.checkGameOverConditions()) {
            statusPanel.setStatus("游戏结束 - 玩家失败!");
            actionPanel.disableActions();
            showGameOverDialog(false, "玩家失败了！");
        } else if (game.checkWinConditions()) {
            statusPanel.setStatus("游戏结束 - 玩家胜利!");
            actionPanel.disableActions();
            showGameOverDialog(true, "玩家胜利了！");
        }
    }

    /**
     * 显示游戏结束对话框
     * @param isWin 是否胜利
     * @param message 结束信息
     */
    private void showGameOverDialog(boolean isWin, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                isWin ? javafx.scene.control.Alert.AlertType.INFORMATION : 
                        javafx.scene.control.Alert.AlertType.WARNING);
        
        alert.setTitle(isWin ? "游戏胜利" : "游戏失败");
        alert.setHeaderText(message);
        
        String contentText = isWin ? 
                "恭喜！所有玩家成功逃离了禁闭岛！" : 
                "很遗憾，岛屿沉没，冒险失败了。";
        
        alert.setContentText(contentText + "\n\n是否要重新开始游戏？");
        
        // 添加重新开始按钮
        javafx.scene.control.ButtonType restartButton = 
                new javafx.scene.control.ButtonType("重新开始", 
                        javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        javafx.scene.control.ButtonType exitButton = 
                new javafx.scene.control.ButtonType("退出", 
                        javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(restartButton, exitButton);
        
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == restartButton) {
                restartGame();
            } else {
                javafx.application.Platform.exit();
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
        alert.getDialogPane().setPrefWidth(500);
        alert.showAndWait();
    }

    /**
     * The main method is needed for IDEs that do not support JavaFX launchers directly.
     * 对于不直接支持 JavaFX 启动器的 IDE，需要 main 方法。
     * @param args command line arguments
     *             命令行参数
     */
    public static void main(String[] args) {
        launch(args);
    }
} 