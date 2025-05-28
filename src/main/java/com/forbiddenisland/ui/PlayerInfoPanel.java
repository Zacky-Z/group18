package com.forbiddenisland.ui;

import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.Player;
import com.forbiddenisland.model.Card;
import com.forbiddenisland.model.TreasureCard;
import com.forbiddenisland.model.SpecialActionCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家信息面板 - 显示游戏中所有玩家的信息
 */
public class PlayerInfoPanel extends VBox {
    
    private Game game;
    private List<PlayerView> playerViews;
    private TextArea handCardsArea;
    private Button discardButton;
    private Button viewOtherPlayersButton;
    
    public PlayerInfoPanel(Game game) {
        this.game = game;
        this.playerViews = new ArrayList<>();
        
        setPadding(new Insets(10));
        setSpacing(10);
        setPrefWidth(300);  // 增加宽度以适应手牌区域
        
        setBorder(new Border(new BorderStroke(
                Color.GRAY, 
                BorderStrokeStyle.SOLID, 
                new CornerRadii(5), 
                BorderWidths.DEFAULT
        )));
        
        Label titleLabel = new Label("玩家信息");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getChildren().add(titleLabel);
        
        initializePlayerViews();
        createHandCardsArea();  // 添加手牌区域
    }
    
    private void initializePlayerViews() {
        for (Player player : game.getPlayers()) {
            PlayerView playerView = new PlayerView(player);
            playerViews.add(playerView);
            getChildren().add(playerView);
        }
    }
    
    /**
     * 创建手牌显示区域
     */
    private void createHandCardsArea() {
        // 创建手牌区域容器
        VBox handCardsContainer = new VBox(10);
        handCardsContainer.setPadding(new Insets(10));
        handCardsContainer.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5;");

        Label handLabel = new Label("手牌");
        handLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        // 设置手牌显示区域
        handCardsArea = new TextArea();
        handCardsArea.setEditable(false);
        handCardsArea.setPrefRowCount(12);  // 增加显示行数，平铺显示
        handCardsArea.setPrefColumnCount(30);
        handCardsArea.setWrapText(true);
        handCardsArea.setStyle("-fx-font-size: 14px; -fx-font-family: 'Arial';");
        
        // 创建按钮容器
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(5));
        buttonBox.setAlignment(Pos.CENTER);
        
        discardButton = new Button("弃牌");
        discardButton.setPrefWidth(120);
        discardButton.setMaxWidth(Double.MAX_VALUE);
        discardButton.setStyle("-fx-font-size: 13px;");
        discardButton.setOnAction(e -> handleDiscardAction());
        
        viewOtherPlayersButton = new Button("查看队友手牌");
        viewOtherPlayersButton.setPrefWidth(120);
        viewOtherPlayersButton.setMaxWidth(Double.MAX_VALUE);
        viewOtherPlayersButton.setStyle("-fx-font-size: 13px;");
        viewOtherPlayersButton.setOnAction(e -> handleViewOtherPlayersAction());
        
        buttonBox.getChildren().addAll(discardButton, viewOtherPlayersButton);
        
        // 将所有元素添加到手牌容器
        handCardsContainer.getChildren().addAll(handLabel, handCardsArea, buttonBox);
        
        // 设置手牌容器的对齐方式和大小
        handCardsContainer.setAlignment(Pos.CENTER);
        handCardsContainer.setPrefWidth(280);
        handCardsContainer.setMaxWidth(Double.MAX_VALUE);
        
        // 添加到主面板
        getChildren().add(handCardsContainer);
        
        // 初始更新手牌显示
        updateHandCardsDisplay();
    }
    
    /**
     * 更新所有玩家信息
     */
    public void update() {
        // 更新当前玩家突出显示
        Player currentPlayer = game.getCurrentPlayer();
        
        for (PlayerView playerView : playerViews) {
            playerView.update();
            playerView.setHighlighted(playerView.getPlayer().equals(currentPlayer));
        }
        
        // 更新手牌显示
        updateHandCardsDisplay();
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
            // 按卡牌类型分组显示
            List<TreasureCard> treasureCards = new ArrayList<>();
            List<SpecialActionCard> specialCards = new ArrayList<>();
            
            for (Card card : hand) {
                if (card instanceof TreasureCard) {
                    treasureCards.add((TreasureCard) card);
                } else if (card instanceof SpecialActionCard) {
                    specialCards.add((SpecialActionCard) card);
                }
            }
            
            // 显示宝藏卡
            if (!treasureCards.isEmpty()) {
                sb.append("宝藏卡：\n");
                for (TreasureCard card : treasureCards) {
                    sb.append("• ").append(card.getTreasureType().getDisplayName()).append("\n");
                }
                sb.append("\n");
            }
            
            // 显示特殊行动卡
            if (!specialCards.isEmpty()) {
                sb.append("特殊行动卡：\n");
                for (SpecialActionCard card : specialCards) {
                    sb.append("• ").append(card.getName()).append(" - ").append(card.getDescription()).append("\n");
                }
            }
        }
        
        handCardsArea.setText(sb.toString());
    }
    
    /**
     * 处理弃牌操作
     */
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
        
        ButtonType discardButtonType = new ButtonType("弃掉", ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("取消", ButtonData.CANCEL_CLOSE);
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
        
        ButtonType viewButtonType = new ButtonType("查看", ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("取消", ButtonData.CANCEL_CLOSE);
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
        
        ButtonType closeButtonType = new ButtonType("关闭", ButtonData.CANCEL_CLOSE);
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
     * 显示消息对话框
     */
    private void showMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("游戏信息");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * 设置游戏对象
     * @param game 新的游戏对象
     */
    public void setGame(Game game) {
        this.game = game;
        getChildren().clear();
        
        Label titleLabel = new Label("玩家信息");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getChildren().add(titleLabel);
        
        playerViews.clear();
        initializePlayerViews();
        createHandCardsArea();  // 重新创建手牌区域
    }
    
    /**
     * 内部类：单个玩家视图
     */
    private class PlayerView extends VBox {
        private Player player;
        private Label nameLabel;
        private Label roleLabel;
        private Label cardsLabel;
        private Label locationLabel;
        private Label colorLabel;
        
        public PlayerView(Player player) {
            this.player = player;
            
            setPadding(new Insets(5));
            setSpacing(3);
            
            nameLabel = new Label();
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            
            // 创建图标来显示玩家颜色
            String playerColor = player.getPawn().getColor();
            javafx.scene.paint.Color color = getColorFromString(playerColor);
            
            HBox nameBox = new HBox(5);
            javafx.scene.shape.Rectangle colorRect = new javafx.scene.shape.Rectangle(12, 12);
            colorRect.setFill(color);
            colorRect.setStroke(Color.BLACK);
            nameBox.getChildren().addAll(colorRect, nameLabel);
            
            roleLabel = new Label();
            cardsLabel = new Label();
            locationLabel = new Label();
            colorLabel = new Label();
            
            getChildren().addAll(nameBox, roleLabel, locationLabel, cardsLabel);
            
            update();
        }
        
        public void update() {
            nameLabel.setText(player.getName());
            roleLabel.setText("角色: " + player.getRole().getChineseName() + " - " + player.getRole().getDescription());
            cardsLabel.setText("手牌数: " + player.getHand().size());
            
            // 更新当前位置
            if (player.getPawn() != null && player.getPawn().getCurrentLocation() != null) {
                locationLabel.setText("位置: " + player.getPawn().getCurrentLocation().getName());
            } else {
                locationLabel.setText("位置: 未知");
            }
        }
        
        public void setHighlighted(boolean highlighted) {
            if (highlighted) {
                setStyle("-fx-background-color: lightyellow; -fx-border-color: gold; -fx-border-width: 2;");
                // 添加当前玩家标记
                if (!getChildren().contains(colorLabel)) {
                    colorLabel.setText("【当前玩家】");
                    colorLabel.setTextFill(Color.RED);
                    colorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    getChildren().add(0, colorLabel);
                }
            } else {
                setStyle("");
                // 移除当前玩家标记
                getChildren().remove(colorLabel);
            }
        }
        
        private javafx.scene.paint.Color getColorFromString(String colorName) {
            switch (colorName.toUpperCase()) {
                case "RED": return Color.RED;
                case "BLUE": return Color.BLUE;
                case "GREEN": return Color.GREEN;
                case "BLACK": return Color.BLACK;
                case "WHITE": return Color.WHITE;
                case "YELLOW": return Color.YELLOW;
                default: return Color.GRAY;
            }
        }
        
        public Player getPlayer() {
            return player;
        }
    }
} 