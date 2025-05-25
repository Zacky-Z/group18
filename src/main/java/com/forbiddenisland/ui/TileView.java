package com.forbiddenisland.ui;

import com.forbiddenisland.model.Game;
import com.forbiddenisland.model.IslandTile;
import com.forbiddenisland.model.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.List;

/**
 * 岛屿板块视图 - 显示单个岛屿板块的状态
 */
public class TileView extends StackPane {
    
    private IslandTile tile;
    private Label nameLabel;
    private Label statusLabel;
    private VBox contentBox;
    private FlowPane pawnsPane;
    private Game game;
    
    private static final double TILE_SIZE = 120;
    private static final double PAWN_SIZE = 12;
    
    public TileView(IslandTile tile) {
        this(tile, null);
    }
    
    public TileView(IslandTile tile, Game game) {
        this.tile = tile;
        this.game = game;
        
        setPrefSize(TILE_SIZE, TILE_SIZE);
        setMinSize(TILE_SIZE, TILE_SIZE);
        setMaxSize(TILE_SIZE, TILE_SIZE);
        
        setBorder(new Border(new BorderStroke(
                Color.BLACK, 
                BorderStrokeStyle.SOLID, 
                new CornerRadii(5), 
                BorderWidths.DEFAULT
        )));
        
        contentBox = new VBox(5);
        contentBox.setPadding(new Insets(5));
        contentBox.setAlignment(Pos.CENTER);
        
        nameLabel = new Label();
        nameLabel.setWrapText(true);
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", 10));
        
        // 添加棋子显示区域
        pawnsPane = new FlowPane();
        pawnsPane.setHgap(4);
        pawnsPane.setVgap(4);
        pawnsPane.setAlignment(Pos.CENTER);
        
        contentBox.getChildren().addAll(nameLabel, statusLabel, pawnsPane);
        getChildren().add(contentBox);
        
        update(tile);
    }
    
    /**
     * 更新板块视图
     * @param tile 岛屿板块，null表示水域
     */
    public void update(IslandTile tile) {
        this.tile = tile;
        
        if (tile == null) {
            // 水域
            nameLabel.setText("水域");
            statusLabel.setText("");
            setBackground(new Background(new BackgroundFill(
                    Color.LIGHTBLUE, 
                    new CornerRadii(5),
                    Insets.EMPTY
            )));
            pawnsPane.getChildren().clear();
            return;
        }
        
        nameLabel.setText(tile.getName());
        
        if (tile.isFlooded()) {
            // 检查游戏板中此位置是否仍有板块
            // 如果已沉没，会被从游戏板上移除，我们这里只显示被淹没状态
            setBackground(new Background(new BackgroundFill(
                    Color.SKYBLUE, 
                    new CornerRadii(5),
                    Insets.EMPTY
            )));
            statusLabel.setText("已淹没");
        } else {
            // 正常
            setBackground(new Background(new BackgroundFill(
                    Color.LIGHTGREEN, 
                    new CornerRadii(5),
                    Insets.EMPTY
            )));
            
            if (tile.getAssociatedTreasure() != null) {
                statusLabel.setText("宝藏: " + tile.getAssociatedTreasure().name());
            } else {
                statusLabel.setText("正常");
            }
        }
        
        // 更新玩家棋子显示
        updatePawns();
    }
    
    /**
     * 更新板块上的玩家棋子显示
     */
    private void updatePawns() {
        pawnsPane.getChildren().clear();
        
        if (game == null || tile == null) return;
        
        List<Player> players = game.getPlayers();
        for (Player player : players) {
            if (player.getPawn().getCurrentLocation() == tile) {
                createPawnCircle(player);
            }
        }
    }
    
    /**
     * 根据玩家创建棋子图形
     */
    private void createPawnCircle(Player player) {
        Circle circle = new Circle(PAWN_SIZE);
        String pawnColor = player.getPawn().getColor();
        
        // 获取玩家编号（从玩家名称中提取数字）
        String playerName = player.getName();
        String playerNumber = playerName.replaceAll("\\D+", ""); // 提取数字部分
        
        switch (pawnColor.toUpperCase()) {
            case "RED":
                circle.setFill(Color.RED);
                break;
            case "BLUE":
                circle.setFill(Color.BLUE);
                break;
            case "GREEN":
                circle.setFill(Color.GREEN);
                break;
            case "BLACK":
                circle.setFill(Color.BLACK);
                break;
            case "WHITE":
                circle.setFill(Color.WHITE);
                circle.setStroke(Color.BLACK);
                break;
            case "YELLOW":
                circle.setFill(Color.YELLOW);
                break;
            default:
                circle.setFill(Color.GRAY);
        }
        
        // 在棋子上添加玩家编号文本
        StackPane pawnWithLabel = new StackPane();
        Label numberLabel = new Label(playerNumber);
        numberLabel.setTextFill(pawnColor.equalsIgnoreCase("WHITE") || pawnColor.equalsIgnoreCase("YELLOW") ? 
                                Color.BLACK : Color.WHITE);
        numberLabel.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        
        pawnWithLabel.getChildren().addAll(circle, numberLabel);
        pawnsPane.getChildren().add(pawnWithLabel);
        
        // 添加玩家信息提示
        pawnWithLabel.setOnMouseEntered(e -> {
            setTooltip(player.getName() + " (" + player.getRole() + ")");
        });
        
        pawnWithLabel.setOnMouseExited(e -> {
            clearTooltip();
        });
    }
    
    private void setTooltip(String text) {
        // 简单实现，实际应使用JavaFX的Tooltip类
        statusLabel.setText(text);
    }
    
    private void clearTooltip() {
        // 恢复原状态文本
        if (tile != null) {
            if (tile.isFlooded()) {
                statusLabel.setText("已淹没");
            } else if (tile.getAssociatedTreasure() != null) {
                statusLabel.setText("宝藏: " + tile.getAssociatedTreasure().name());
            } else {
                statusLabel.setText("正常");
            }
        }
    }
    
    public IslandTile getTile() {
        return tile;
    }
} 