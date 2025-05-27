package com.forbiddenisland.ui;

import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.Player;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
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
    
    public PlayerInfoPanel(Game game) {
        this.game = game;
        this.playerViews = new ArrayList<>();
        
        setPadding(new Insets(10));
        setSpacing(10);
        setPrefWidth(250);
        
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
    }
    
    private void initializePlayerViews() {
        for (Player player : game.getPlayers()) {
            PlayerView playerView = new PlayerView(player);
            playerViews.add(playerView);
            getChildren().add(playerView);
        }
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
            roleLabel.setText("角色: " + player.getRole().getDescription());
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