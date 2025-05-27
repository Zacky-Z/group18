package com.forbiddenisland.ui.view;

import com.forbiddenisland.ui.controller.GameControllerFX;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class GameMenu extends HBox {
    private final MenuBar menuBar;
    private final Button endTurnButton;
    private final Button helpButton;

    public GameMenu(GameControllerFX controller) {
        menuBar = createMenuBar(controller);
        endTurnButton = new Button("结束回合");
        helpButton = new Button("帮助");
        
        // 设置按钮事件
        endTurnButton.setOnAction(e -> controller.handleEndTurn());
        helpButton.setOnAction(e -> showHelpDialog());
        
        // 设置菜单样式
        setSpacing(10);
        setPadding(new javafx.geometry.Insets(5));
        getChildren().addAll(menuBar, endTurnButton, helpButton);
    }

    private MenuBar createMenuBar(GameControllerFX controller) {
        MenuBar menuBar = new MenuBar();
        
        // 文件菜单
        Menu fileMenu = new Menu("文件");
        MenuItem newGameItem = new MenuItem("新游戏");
        MenuItem saveGameItem = new MenuItem("保存游戏");
        MenuItem loadGameItem = new MenuItem("加载游戏");
        MenuItem exitItem = new MenuItem("退出");
        
        newGameItem.setOnAction(e -> controller.startGame());
        saveGameItem.setOnAction(e -> saveGame());
        loadGameItem.setOnAction(e -> loadGame());
        exitItem.setOnAction(e -> System.exit(0));
        
        fileMenu.getItems().addAll(newGameItem, saveGameItem, loadGameItem, new SeparatorMenuItem(), exitItem);
        
        // 游戏菜单
        Menu gameMenu = new Menu("游戏");
        MenuItem rulesItem = new MenuItem("游戏规则");
        MenuItem aboutItem = new MenuItem("关于");
        
        rulesItem.setOnAction(e -> showRulesDialog());
        aboutItem.setOnAction(e -> showAboutDialog());
        
        gameMenu.getItems().addAll(rulesItem, aboutItem);
        
        menuBar.getMenus().addAll(fileMenu, gameMenu);
        return menuBar;
    }

    private void showHelpDialog() {
        // 显示帮助对话框
    }

    private void showRulesDialog() {
        // 显示游戏规则对话框
    }

    private void showAboutDialog() {
        // 显示关于对话框
    }

    private void saveGame() {
        // 实现保存游戏功能
    }

    private void loadGame() {
        // 实现加载游戏功能
    }
}    