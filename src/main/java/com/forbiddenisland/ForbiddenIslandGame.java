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
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList; // For discard dialog
import javafx.scene.control.Dialog; // For discard dialog
import javafx.scene.control.ListView; // For discard dialog
import javafx.scene.control.ListCell; // For discard dialog
import javafx.scene.control.ButtonType; // For discard dialog
import javafx.scene.control.Alert; // For alerts
import javafx.scene.control.ButtonBar; // For button types
import javafx.scene.control.SelectionMode; // For ListView selection mode

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

    // Getter for StatusPanel
    public StatusPanel getStatusPanel() {
        return statusPanel;
    }

    @Override
    public void start(Stage primaryStage) {
        createInitialScreen(primaryStage);
    }

    private void createInitialScreen(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label titleLabel = new Label("禁闭岛 (Forbidden Island)");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        root.setTop(titleLabel);

        VBox buttonPanel = new VBox(10);
        buttonPanel.setPadding(new Insets(10));

        Button startButton = new Button("开始游戏");
        startButton.setOnAction(e -> createDifficultyScreen(primaryStage));

        Button exitButton = new Button("退出");
        exitButton.setOnAction(e -> primaryStage.close());

        buttonPanel.getChildren().addAll(startButton, exitButton);
        root.setCenter(buttonPanel);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("禁闭岛游戏 (Forbidden Island Game)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createDifficultyScreen(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label titleLabel = new Label("选择难度");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        root.setTop(titleLabel);

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

        Button startGameButton = new Button("开始游戏");
        startGameButton.setOnAction(e -> {
            int difficulty = 1;
            if (normalButton.isSelected()) {
                difficulty = 2;
            } else if (expertButton.isSelected()) {
                difficulty = 3;
            } else if (legendButton.isSelected()) {
                difficulty = 4;
            }
            initializeGameAndUI(primaryStage, difficulty);
        });

        difficultyPanel.getChildren().addAll(noviceButton, normalButton, expertButton, legendButton, startGameButton);
        root.setCenter(difficultyPanel);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("选择难度 - 禁闭岛游戏 (Forbidden Island Game)");
        primaryStage.setScene(scene);
    }

    private void initializeGameAndUI(Stage primaryStage, int difficulty) {
        List<String> playerNames = Arrays.asList("玩家1", "玩家2", "玩家3", "玩家4");
        game = new Game(playerNames, difficulty);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox titleBar = new HBox(15);
        titleBar.setPadding(new Insets(10));
        titleBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label titleLabel = new Label("禁闭岛 (Forbidden Island)");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button helpButton = new Button("游戏帮助");
        helpButton.setOnAction(e -> showGameHelp());

        titleBar.getChildren().addAll(titleLabel, helpButton);
        root.setTop(titleBar);

        gameBoardView = new GameBoardView(game);
        root.setCenter(gameBoardView);

        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));

        playerInfoPanel = new PlayerInfoPanel(game);
        actionPanel = new ActionPanel(game, this);
        actionPanel.setGameBoardView(gameBoardView);

        rightPanel.getChildren().addAll(playerInfoPanel, actionPanel);
        root.setRight(rightPanel);

        statusPanel = new StatusPanel();
        root.setBottom(statusPanel);

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setTitle("禁闭岛游戏 (Forbidden Island Game)");
        primaryStage.setScene(scene);
        primaryStage.show();

        updateGameState(); // Initial display and status update
        actionPanel.refreshActionState(); // Ensure action panel specifically reflects initial actions for the first player
        statusPanel.setStatus(game.getCurrentPlayer().getName() + " 的回合开始，有 " + game.getActionsRemainingInTurn() + " 个行动点。");
    }

    /**
     * 更新游戏状态并刷新界面
     */
    public void updateGameState() {
        if (game == null) return; // Guard against null game object during initial setup or restart

        gameBoardView.update();
        playerInfoPanel.update();
        actionPanel.update(); // This will also update action count display based on Game model

        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer != null) {
            // Avoid overriding specific messages set by ActionPanel during phase transitions
            if (!statusPanel.getStatus().contains("抽取宝藏牌") && !statusPanel.getStatus().contains("抽取洪水牌")) {
        statusPanel.setStatus("当前玩家: " + currentPlayer.getName() + " (" +
                    currentPlayer.getRole().getDescription() + ") - 剩余行动: " + game.getActionsRemainingInTurn());
            }
        } else {
            statusPanel.setStatus("等待游戏开始...");
        }

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
     * Called from ActionPanel when player has finished drawing treasure cards.
     * This method will prepare the UI for the flood card drawing phase.
     */
    public void playerProceedsToDrawFloodCardsPhase() {
        System.out.println("[DEBUG] Entering playerProceedsToDrawFloodCardsPhase.");
        if (game == null || game.getCurrentPlayer() == null) {
            System.err.println("[DEBUG] Game or current player is null in playerProceedsToDrawFloodCardsPhase. Aborting.");
            return;
        }
        Player currentPlayer = game.getCurrentPlayer();
        
        game.setCurrentPhase(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE); // Set game phase FIRST
        
        // Update status panel for the new phase
        statusPanel.setStatus(currentPlayer.getName() + " 请抽取 " + game.getWaterMeter().getNumberOfFloodCardsToDraw() + " 张洪水牌。");
        
        // Enable the specific button required for this phase
        actionPanel.enableDrawFloodCardsButton(true); 
        
        // Call updateGameState() to refresh the entire UI based on the new phase and button states.
        // updateGameState() will call actionPanel.update() internally.
        updateGameState(); 
    }

    /**
     * Called from ActionPanel when the player clicks the "Draw Flood Cards" button.
     * This method handles drawing flood cards, updating game state, and advancing to the next turn.
     */
    public void processFloodCardDrawAndAdvance(){
        if (game == null || game.getCurrentPlayer() == null) return;
        
        List<FloodActionReport> floodReports = game.playerDrawsFloodCards_REVISED();
        
        // Display flood card results in StatusPanel
        if (statusPanel != null && floodReports != null && !floodReports.isEmpty()) {
            StringBuilder floodMessages = new StringBuilder("洪水牌抽取结果:\n");
            for (FloodActionReport report : floodReports) {
                floodMessages.append("抽到卡: ").append(report.getFloodCardName());
                floodMessages.append(", 目标: ").append(report.getAffectedTileName());
                floodMessages.append(", 结果: ").append(report.getOutcomeDescription()).append("\n");
            }
            String messageToShow = floodMessages.toString().trim();
            statusPanel.setStatus(messageToShow);
            System.out.println("[DEBUG] StatusPanel updated with flood results: " + messageToShow);
        } else {
            System.out.println("[DEBUG] No flood reports to show or StatusPanel/floodReports are null/empty.");
            if (statusPanel != null && (floodReports == null || floodReports.isEmpty())) {
                statusPanel.setStatus("未抽取到洪水牌，或无结果报告。");
            }
        }

        updateGameState(); // Refresh UI (GameBoardView will update based on model changes)

        // After flood cards are drawn (and assuming no game over), advance to next turn.
        if (!game.checkGameOverConditions() && !game.checkWinConditions()) {
            advanceToNextTurn();
        }
        // If game is over, updateGameState() inside checkGameOverConditions will handle status and dialogs.
    }

    /**
     * Advances to the next player's turn and updates the UI accordingly.
     * This method is intended to be called after the current player completes all their phases
     * (actions, draw treasure cards, draw flood cards).
     */
    public void advanceToNextTurn() {
        if (game == null || game.checkGameOverConditions() || game.checkWinConditions()) {
            return; // Don't advance if game is over or not started
        }
        game.nextTurn(); // Advances player, resets actions and pilot ability in Game model
        actionPanel.refreshActionState(); // Ensures ActionPanel UI updates for the new player's actions
        playerInfoPanel.update(); // Update for new current player info
        statusPanel.setStatus(game.getCurrentPlayer().getName() + " 的回合开始，有 " + game.getActionsRemainingInTurn() + " 个行动点。");
        updateGameState(); // General UI refresh
    }

    /**
     * Shows the results of drawing treasure cards in the status panel.
     * 在状态面板中显示抽取宝藏牌的结果。
     * @param results A list of strings describing the drawn cards/events. (描述所抽卡牌/事件的字符串列表)
     */
    public void showTreasureDrawResults(List<String> results) {
        if (statusPanel != null && results != null) {
            StringBuilder message = new StringBuilder();
            for (String result : results) {
                if (result.toLowerCase().contains("洪水上涨") || result.toLowerCase().contains("waters rise")) {
                    // Potentially highlight or make this message more prominent
                    message.append("** ").append(result).append(" **\n"); // Markdown-like bold
                } else {
                    message.append(result).append("\n");
                }
            }
            statusPanel.setStatus(message.toString().trim()); 
            // Consider a brief pause or append to existing status if needed
            // For now, it replaces the status.
        }
    }

    /**
     * Checks if the player's hand is over the limit and prompts for discard if necessary.
     * Calls the onCompletionCallback after the hand limit is resolved.
     * 检查玩家手牌是否超限，如果需要则提示弃牌。
     * 手牌上限问题解决后调用 onCompletionCallback。
     * @param player The player whose hand to check. (要检查手牌的玩家)
     * @param onCompletionCallback Callback to run after discard process is complete. (弃牌流程完成后运行的回调)
     */
    public void checkAndHandleHandLimit(Player player, Runnable onCompletionCallback) {
        System.out.println("[DEBUG] Entering checkAndHandleHandLimit for player: " + player.getName() + " Hand size: " + player.getHand().size() + " Limit: " + Player.MAX_HAND_SIZE);
        boolean handOverLimit = player.isHandOverLimit();
        System.out.println("[DEBUG] player.isHandOverLimit() returned: " + handOverLimit);

        if (handOverLimit) {
            System.out.println("[DEBUG] Hand is over limit. Calling showDiscardDialog.");
            statusPanel.setStatus(player.getName() + " 手牌超限，请弃牌...");
            showDiscardDialog(player, onCompletionCallback);
        } else {
            System.out.println("[DEBUG] Hand is NOT over limit. Preparing to run onCompletionCallback.");
            statusPanel.setStatus(player.getName() + " 手牌数量正常，准备进入下一阶段...");
            try {
                onCompletionCallback.run();
                System.out.println("[DEBUG] onCompletionCallback.run() executed successfully.");
            } catch (Exception e) {
                System.err.println("[DEBUG] Exception during onCompletionCallback.run(): " + e.getMessage());
                e.printStackTrace();
                statusPanel.setStatus("错误：无法进入下一阶段！详情请查看控制台日志。");
            }
        }
    }

    private void showDiscardDialog(Player player, Runnable onCompletionCallback) {
        Dialog<Card> dialog = new Dialog<>();
        dialog.setTitle(player.getName() + " 手牌超限 - 请弃牌");
        dialog.setHeaderText("你的手牌数量 (" + player.getHand().size() + ") 已超过上限 (" + Player.MAX_HAND_SIZE + ").\n请选择要弃掉的牌，直到手牌数量不大于 " + Player.MAX_HAND_SIZE + ".");

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK); // OK button to confirm discard
        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true); // Initially disable OK

        ListView<Card> handListView = new ListView<>();
        handListView.getItems().addAll(player.getHand());
        handListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Allow multiple discards if needed in one go, though rules are one by one

        // Update OK button disable state based on selection and remaining cards to discard
        handListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            int cardsToDiscardCount = player.getHand().size() - Player.MAX_HAND_SIZE;
            int selectedToDiscardCount = handListView.getSelectionModel().getSelectedItems().size();
            // This simple logic is for discarding exactly the number of cards needed.
            // A more complex UI might allow discarding one at a time until limit is met.
            // For now, player must select (hand.size - MAX_HAND_SIZE) cards.
            // We will enforce this by only enabling OK when enough are selected OR if we make it one-by-one discard.
            // Let's simplify: player discards one card at a time until limit is met.
            dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(selectedToDiscardCount != 1);
        });

        handListView.setCellFactory(lv -> new ListCell<Card>() {
            @Override
            protected void updateItem(Card card, boolean empty) {
                super.updateItem(card, empty);
                setText(empty || card == null ? null : card.getName() + " (" + card.getDescription() + ")");
            }
        });

        dialog.getDialogPane().setContent(handListView);

        // This dialog will be modal
        dialog.showAndWait().ifPresent(selectedCardToDiscard -> {
            // This is tricky because showAndWait() with ButtonType.OK doesn't directly return the selected item from ListView
            // if we are using the generic Dialog<Card> and setting result converter.
            // Instead, we process the selection when OK is clicked.
            // The above ButtonType.OK is a bit misleading. For a true discard loop, we need custom buttons.
            // Let's restructure this to a loop that shows the dialog until hand limit is met.
        });
        // --- REVISED DIALOG LOGIC --- 
        // Loop until hand size is correct
        // We need a new dialog instance each time if player makes a mistake or we want one-by-one discard confirmation.
        // Simpler: one dialog, player selects cards, clicks OK. We validate. If wrong, show error and re-prompt or clear selection.

        // Corrected loop for discard dialog:
        Runnable[] recursiveDiscard = new Runnable[1]; // Array hack for lambdas
        recursiveDiscard[0] = () -> {
            if (!player.isHandOverLimit()) {
                onCompletionCallback.run();
                return;
            }

            Dialog<Card> discardDialog = new Dialog<>();
            discardDialog.setTitle(player.getName() + " 手牌超限 - 请弃牌");
            discardDialog.setHeaderText("你的手牌数量 (" + player.getHand().size() + ") 超过上限 (" + Player.MAX_HAND_SIZE + ").\n" +
                                    "请选择一张要弃掉的牌.");
            discardDialog.getDialogPane().getButtonTypes().clear(); // Clear default buttons
            ButtonType discardButtonType = new ButtonType("弃掉选中牌", ButtonBar.ButtonData.OK_DONE);
            discardDialog.getDialogPane().getButtonTypes().addAll(discardButtonType, ButtonType.CANCEL);
            
            ListView<Card> currentHandView = new ListView<>();
            currentHandView.getItems().addAll(player.getHand());
            currentHandView.setCellFactory(lv -> new ListCell<Card>() {
                @Override
                protected void updateItem(Card card, boolean empty) {
                    super.updateItem(card, empty);
                    setText(empty || card == null ? null : card.getName() + " (" + card.getDescription() + ")");
                }
            });
            discardDialog.getDialogPane().setContent(currentHandView);
            discardDialog.getDialogPane().lookupButton(discardButtonType).setDisable(true);
            currentHandView.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
                discardDialog.getDialogPane().lookupButton(discardButtonType).setDisable(nv == null);
            });

            discardDialog.setResultConverter(dialogButton -> {
                if (dialogButton == discardButtonType) {
                    return currentHandView.getSelectionModel().getSelectedItem();
                }
                return null;
            });

            discardDialog.showAndWait().ifPresent(cardToDiscard -> {
                if (cardToDiscard != null) {
                    game.discardCardAndPlaceOnTreasureDiscardPile(player, cardToDiscard);
                    statusPanel.setStatus(player.getName() + " 弃掉了 " + cardToDiscard.getName());
                    updateGameState(); // Refresh UI (hand view, etc.)
                    recursiveDiscard[0].run(); // Check again if more discards are needed
                } else {
                    // Player cancelled or closed dialog - what to do? 
                    // For now, re-prompt by calling the recursive call again.
                    // This could be an issue if they can't proceed.
                    // A better way would be to not allow cancelling if discard is mandatory.
                    // Or, if cancelled, select a default card to discard.
                    // For now, if they cancel, we just re-trigger the dialog process for discard.
                    statusPanel.setStatus("必须弃牌才能继续！");
                    recursiveDiscard[0].run(); 
                }
            });
        };
        recursiveDiscard[0].run(); // Start the discard process
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
        Stage primaryStage = (Stage) gameBoardView.getScene().getWindow();
        createInitialScreen(primaryStage); // Restart from the very beginning
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