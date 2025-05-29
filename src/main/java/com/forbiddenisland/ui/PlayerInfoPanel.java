package com.forbiddenisland.ui;

import com.forbiddenisland.ForbiddenIslandGame;
import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.Player;
import com.forbiddenisland.model.Card;
import com.forbiddenisland.model.TreasureCard;
import com.forbiddenisland.model.SpecialActionCard;
import com.forbiddenisland.model.AdventurerRole;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.effect.DropShadow;

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
    private ForbiddenIslandGame mainApp;
    
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
        VBox playersContainer = new VBox(10); // 添加10像素的间距
        playersContainer.setPadding(new Insets(5));
        playersContainer.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label playersHeader = new Label("游戏玩家");
        playersHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        playersHeader.setPadding(new Insets(0, 0, 5, 0));
        playersContainer.getChildren().add(playersHeader);
        
        for (Player player : game.getPlayers()) {
            PlayerView playerView = new PlayerView(player);
            playerViews.add(playerView);
            playersContainer.getChildren().add(playerView);
        }
        
        getChildren().add(playersContainer);
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
        
        // 确保玩家视图中的手牌数量也更新
        for (PlayerView playerView : playerViews) {
            playerView.update();
        }
    }
    
    /**
     * 处理弃牌操作
     */
    private void handleDiscardAction() {
        Player currentPlayer = game.getCurrentPlayer();
        List<Card> hand = currentPlayer.getHand();
        
        if (hand.isEmpty()) {
            showMessage("你没有手牌可以弃掉。");
            return;
        }
        
        boolean isOverLimit = currentPlayer.isHandOverLimit();
        int cardsToDiscard = isOverLimit ? hand.size() - Player.MAX_HAND_SIZE : 1; // 如果超过上限，必须弃到上限，否则默认弃1张
        
        Dialog<List<Card>> dialog = new Dialog<>();
        dialog.setTitle("弃牌");
        
        String headerText;
        if (isOverLimit) {
            headerText = "你必须弃掉 " + cardsToDiscard + " 张卡牌\n当前手牌: " + hand.size() + "/" + Player.MAX_HAND_SIZE;
        } else {
            headerText = "请选择要弃掉的卡牌\n当前手牌: " + hand.size() + "/" + Player.MAX_HAND_SIZE;
        }
        dialog.setHeaderText(headerText);
        
        ButtonType discardButtonType = new ButtonType("弃掉", ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("取消", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(discardButtonType, cancelButtonType);
        
        // 如果是强制弃牌（手牌超过上限），则不允许取消
        if (isOverLimit && game.getCurrentPhase() == Game.GamePhase.DRAW_TREASURE_CARDS_PHASE) {
            dialog.getDialogPane().getButtonTypes().remove(cancelButtonType);
        }
        
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
            if (isOverLimit && selectedCards.size() != cardsToDiscard) {
                showMessage("你必须弃掉 " + cardsToDiscard + " 张卡牌");
                handleDiscardAction(); // 重新打开弃牌对话框
            } else if (selectedCards.isEmpty()) {
                showMessage("请至少选择一张卡牌弃掉");
                // 如果是超过上限的强制弃牌，则重新打开对话框
                if (isOverLimit) {
                    handleDiscardAction();
                }
            } else {
                for (Card card : selectedCards) {
                    currentPlayer.removeCardFromHand(card);
                    game.getTreasureDeck().discardCard(card);
                    System.out.println(currentPlayer.getName() + " 弃掉了: " + card.getName());
                }
                
                // 更新UI显示
                updateHandCardsDisplay();
                
                // 检查是否仍然超过手牌上限
                if (currentPlayer.isHandOverLimit()) {
                    showMessage("你仍然需要弃掉 " + (currentPlayer.getHand().size() - Player.MAX_HAND_SIZE) + " 张卡牌");
                    handleDiscardAction(); // 如果仍然超过，继续弃牌
                } else {
                    // 如果在抽宝藏牌阶段弃牌完成，自动进入抽洪水牌阶段
                    if (game.getCurrentPhase() == Game.GamePhase.DRAW_TREASURE_CARDS_PHASE) {
                        System.out.println("弃牌完成，进入抽洪水牌阶段");
                        game.setCurrentPhase(Game.GamePhase.DRAW_FLOOD_CARDS_PHASE);
                        
                        // 更新整个游戏界面，确保所有按钮状态正确
                        if (mainApp != null) {
                            mainApp.updateGameState();
                        }
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
     * 强制打开弃牌对话框
     * 当玩家手牌超过上限时，会被调用
     */
    public void forceDiscardAction() {
        if (game != null && game.getCurrentPlayer() != null) {
            Player currentPlayer = game.getCurrentPlayer();
            if (currentPlayer.isHandOverLimit()) {
                handleDiscardAction();
            }
        }
    }
    
    /**
     * 设置主应用引用
     * @param mainApp ForbiddenIslandGame实例
     */
    public void setMainApp(ForbiddenIslandGame mainApp) {
        this.mainApp = mainApp;
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
        private ImageView pawnImageView;
        
        public PlayerView(Player player) {
            this.player = player;
            
            setPadding(new Insets(8));
            setSpacing(5);
            
            // 设置边框和圆角
            setBorder(new Border(new BorderStroke(
                Color.LIGHTGRAY, 
                BorderStrokeStyle.SOLID, 
                new CornerRadii(8), 
                BorderWidths.DEFAULT
            )));
            
            // 创建标题栏，包含玩家编号和角色
            HBox headerBox = new HBox(8);
            headerBox.setAlignment(Pos.CENTER_LEFT);
            
            // 提取玩家编号
            String playerName = player.getName();
            String playerNumber = playerName.replaceAll("\\D+", ""); // 提取数字部分
            
            // 创建棋子图片
            int pawnImageNumber = 1;
            try {
                pawnImageNumber = Integer.parseInt(playerNumber);
                // 确保图片编号在1-7范围内
                if (pawnImageNumber < 1 || pawnImageNumber > 7) {
                    pawnImageNumber = 1;
                }
            } catch (NumberFormatException e) {
                // 如果解析失败，使用默认值1
            }
            
            // 加载棋子图片
            String imagePath = "/images/pawns/" + pawnImageNumber + ".png";
            pawnImageView = new ImageView();
            try {
                Image pawnImage = new Image(getClass().getResourceAsStream(imagePath), 
                                          30, 30, true, true);
                pawnImageView.setImage(pawnImage);
                
                // 添加阴影效果
                DropShadow pawnShadow = new DropShadow();
                pawnShadow.setRadius(3.0);
                pawnShadow.setOffsetX(2.0);
                pawnShadow.setOffsetY(2.0);
                pawnShadow.setColor(Color.color(0, 0, 0, 0.5));
                pawnImageView.setEffect(pawnShadow);
            } catch (Exception e) {
                // 如果图片加载失败，创建一个颜色方块代替
                String playerColor = player.getPawn().getColor();
                javafx.scene.paint.Color color = getColorFromString(playerColor);
                
                javafx.scene.shape.Rectangle colorRect = new javafx.scene.shape.Rectangle(24, 24);
                colorRect.setFill(color);
                colorRect.setStroke(Color.BLACK);
                colorRect.setStrokeWidth(1);
                colorRect.setArcWidth(5);
                colorRect.setArcHeight(5);
                
                // 创建一个StackPane来包含方块和玩家编号
                StackPane pawnStack = new StackPane();
                Label numLabel = new Label(playerNumber);
                numLabel.setTextFill(playerColor.equalsIgnoreCase("WHITE") || 
                                    playerColor.equalsIgnoreCase("YELLOW") ? 
                                    Color.BLACK : Color.WHITE);
                numLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                pawnStack.getChildren().addAll(colorRect, numLabel);
                
                headerBox.getChildren().add(pawnStack);
                pawnImageView = null;
            }
            
            // 如果成功加载了图片，添加到headerBox
            if (pawnImageView != null) {
                headerBox.getChildren().add(pawnImageView);
            }
            
            // 创建玩家名称和角色标签
            nameLabel = new Label();
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            
            // 创建带背景色的玩家角色标签
            VBox playerInfoBox = new VBox(2);
            playerInfoBox.setAlignment(Pos.CENTER_LEFT);
            
            // 添加玩家名称到headerBox
            headerBox.getChildren().add(nameLabel);
            
            // 创建角色标签，使用背景色来区分不同角色
            roleLabel = new Label();
            roleLabel.setFont(Font.font("Arial", 12));
            roleLabel.setPadding(new Insets(3, 6, 3, 6));
            roleLabel.setTextFill(Color.WHITE);
            
            // 根据角色设置不同的背景色
            String roleBgColor = getRoleBackgroundColor(player.getRole());
            roleLabel.setStyle("-fx-background-color: " + roleBgColor + "; -fx-background-radius: 3;");
            
            // 创建位置和手牌标签
            locationLabel = new Label();
            locationLabel.setFont(Font.font("Arial", 12));
            
            cardsLabel = new Label();
            cardsLabel.setFont(Font.font("Arial", 12));
            
            // 当前玩家标记
            colorLabel = new Label();
            colorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            
            // 将所有组件添加到玩家信息框
            playerInfoBox.getChildren().addAll(roleLabel, locationLabel, cardsLabel);
            
            // 将所有组件添加到主容器
            getChildren().addAll(headerBox, playerInfoBox);
            
            // 初始更新
            update();
        }
        
        public void update() {
            nameLabel.setText(player.getName());
            
            // 获取角色的简短描述（第一个括号前的内容）
            String roleDesc = player.getRole().getDescription();
            int bracketIndex = roleDesc.indexOf('(');
            String shortDesc = bracketIndex > 0 ? roleDesc.substring(0, bracketIndex).trim() : roleDesc;
            
            // 设置角色名称和简短描述
            roleLabel.setText(player.getRole().getChineseName() + " - " + shortDesc);
            
            // 更新手牌信息
            cardsLabel.setText("手牌数: " + player.getHand().size() + "/" + Player.MAX_HAND_SIZE);
            
            // 更新当前位置
            if (player.getPawn() != null && player.getPawn().getCurrentLocation() != null) {
                locationLabel.setText("位置: " + player.getPawn().getCurrentLocation().getName());
            } else {
                locationLabel.setText("位置: 未知");
            }
        }
        
        public void setHighlighted(boolean highlighted) {
            if (highlighted) {
                // 使用更明显的高亮效果
                setStyle("-fx-background-color: #FFFFE0; -fx-border-color: #FFD700; -fx-border-width: 2; -fx-effect: dropshadow(three-pass-box, #FFD700, 10, 0.5, 0, 0);");
                
                // 添加当前玩家标记
                if (!getChildren().contains(colorLabel)) {
                    colorLabel.setText("【当前玩家】");
                    colorLabel.setTextFill(Color.RED);
                    colorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                    getChildren().add(0, colorLabel);
                    
                    // 添加箭头指示器
                    Label arrowLabel = new Label("➤");
                    arrowLabel.setTextFill(Color.RED);
                    arrowLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                    HBox arrowBox = new HBox(arrowLabel);
                    arrowBox.setAlignment(Pos.CENTER_LEFT);
                    getChildren().add(1, arrowBox);
                }
            } else {
                setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-width: 1;");
                
                // 移除当前玩家标记
                getChildren().removeIf(node -> node == colorLabel || (node instanceof HBox && ((HBox) node).getChildren().size() == 1 && ((HBox) node).getChildren().get(0) instanceof Label && ((Label) ((HBox) node).getChildren().get(0)).getText().equals("➤")));
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
        
        private String getRoleBackgroundColor(AdventurerRole role) {
            switch (role) {
                case DIVER: return "#0066CC"; // 潜水员：蓝色
                case ENGINEER: return "#CC6600"; // 工程师：棕色
                case EXPLORER: return "#009933"; // 探险家：绿色
                case MESSENGER: return "#CC33FF"; // 信使：紫色
                case NAVIGATOR: return "#FFCC00"; // 领航员：黄色
                case PILOT: return "#FF3300"; // 飞行员：红色
                default: return "#666666"; // 默认：灰色
            }
        }
        
        public Player getPlayer() {
            return player;
        }
    }
} 