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
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;

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
    private static final double PAWN_SIZE = 15;
    
    public TileView(IslandTile tile) {
        this(tile, null);
    }
    
    public TileView(IslandTile tile, Game game) {
        this.tile = tile;
        this.game = game;
        
        setPrefSize(TILE_SIZE, TILE_SIZE);
        setMinSize(TILE_SIZE, TILE_SIZE);
        setMaxSize(TILE_SIZE, TILE_SIZE);
        
        // 添加阴影效果
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0, 0, 0, 0.3));
        setEffect(dropShadow);
        
        setBorder(new Border(new BorderStroke(
                Color.BLACK, 
                BorderStrokeStyle.SOLID, 
                new CornerRadii(10), // 更圆润的边角
                new BorderWidths(2)
        )));
        
        contentBox = new VBox(5);
        contentBox.setPadding(new Insets(8));
        contentBox.setAlignment(Pos.CENTER);
        
        nameLabel = new Label();
        nameLabel.setWrapText(true);
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", 12));
        
        // 添加棋子显示区域
        pawnsPane = new FlowPane();
        pawnsPane.setHgap(6);
        pawnsPane.setVgap(6);
        pawnsPane.setPadding(new Insets(5));
        pawnsPane.setAlignment(Pos.CENTER);
        
        contentBox.getChildren().addAll(nameLabel, statusLabel, pawnsPane);
        getChildren().add(contentBox);
        
        // 添加鼠标悬停效果
        setOnMouseEntered(e -> {
            // 轻微放大效果
            setScaleX(1.05);
            setScaleY(1.05);
            // 增强阴影
            dropShadow.setRadius(8.0);
            dropShadow.setOffsetX(4.0);
            dropShadow.setOffsetY(4.0);
        });
        
        setOnMouseExited(e -> {
            // 恢复正常大小
            setScaleX(1.0);
            setScaleY(1.0);
            // 恢复正常阴影
            dropShadow.setRadius(5.0);
            dropShadow.setOffsetX(3.0);
            dropShadow.setOffsetY(3.0);
        });
        
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
            
            // 水域使用渐变蓝色背景
            setBackground(createWaterBackground());
            
            // 添加水波纹效果
            InnerShadow innerShadow = new InnerShadow();
            innerShadow.setRadius(5.0);
            innerShadow.setColor(Color.color(0, 0, 0.5, 0.3));
            contentBox.setEffect(innerShadow);
            
            pawnsPane.getChildren().clear();
            return;
        }
        
        nameLabel.setText(tile.getName());
        
        if (tile.isFlooded()) {
            // 被淹没的板块
            setBackground(createFloodedBackground());
            statusLabel.setText("已淹没");
            statusLabel.setTextFill(Color.DARKBLUE);
            
            // 添加水波纹效果
            Lighting lighting = new Lighting();
            lighting.setDiffuseConstant(1.0);
            lighting.setSpecularConstant(0.0);
            lighting.setSpecularExponent(0.0);
            lighting.setSurfaceScale(5.0);
            
            Light.Distant light = new Light.Distant();
            light.setAzimuth(45.0);
            light.setElevation(45.0);
            lighting.setLight(light);
            
            contentBox.setEffect(lighting);
        } else {
            // 正常板块
            setBackground(createNormalBackground(tile));
            
            if (tile.getAssociatedTreasure() != null) {
                statusLabel.setText("宝藏: " + tile.getAssociatedTreasure().getDisplayName());
                statusLabel.setTextFill(Color.DARKGREEN);
            } else {
                statusLabel.setText("正常");
                statusLabel.setTextFill(Color.BLACK);
            }
            
            // 移除特殊效果
            contentBox.setEffect(null);
        }
        
        // 更新玩家棋子显示
        updatePawns();
    }
    
    /**
     * 创建水域背景
     */
    private Background createWaterBackground() {
        return new Background(new BackgroundFill(
                Color.rgb(100, 180, 255), // 更亮的蓝色
                new CornerRadii(8),
                Insets.EMPTY
        ));
    }
    
    /**
     * 创建被淹没板块的背景
     */
    private Background createFloodedBackground() {
        return new Background(new BackgroundFill(
                Color.rgb(135, 206, 250), // 天蓝色
                new CornerRadii(8),
                Insets.EMPTY
        ));
    }
    
    /**
     * 创建正常板块的背景
     */
    private Background createNormalBackground(IslandTile tile) {
        Color backgroundColor;
        
        // 根据是否有宝藏来设置不同的背景颜色
        if (tile.getAssociatedTreasure() != null) {
            // 有宝藏的板块使用更鲜艳的绿色
            backgroundColor = Color.rgb(144, 238, 144); // 淡绿色
        } else {
            backgroundColor = Color.rgb(152, 251, 152); // 浅绿色
        }
        
        return new Background(new BackgroundFill(
                backgroundColor,
                new CornerRadii(8),
                Insets.EMPTY
        ));
    }
    
    /**
     * 更新板块上的玩家棋子显示
     */
    private void updatePawns() {
        pawnsPane.getChildren().clear();
        
        if (game == null || tile == null || tile.getName() == null) return; // Ensure current tile is valid and has a name
        
        List<Player> players = game.getPlayers();
        for (Player player : players) {
            if (player.getPawn() != null && player.getPawn().getCurrentLocation() != null) {
                // Compare by tile name instead of object reference
                if (tile.getName().equals(player.getPawn().getCurrentLocation().getName())) {
                    createPawnCircle(player);
                }
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
        
        Color fillColor;
        switch (pawnColor.toUpperCase()) {
            case "RED":
                fillColor = Color.RED;
                break;
            case "BLUE":
                fillColor = Color.BLUE;
                break;
            case "GREEN":
                fillColor = Color.GREEN;
                break;
            case "BLACK":
                fillColor = Color.BLACK;
                break;
            case "WHITE":
                fillColor = Color.WHITE;
                break;
            case "YELLOW":
                fillColor = Color.YELLOW;
                break;
            default:
                fillColor = Color.GRAY;
        }
        
        circle.setFill(fillColor);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1.5);
        
        // 添加阴影效果
        DropShadow pawnShadow = new DropShadow();
        pawnShadow.setRadius(3.0);
        pawnShadow.setOffsetX(2.0);
        pawnShadow.setOffsetY(2.0);
        pawnShadow.setColor(Color.color(0, 0, 0, 0.5));
        circle.setEffect(pawnShadow);
        
        // 在棋子上添加玩家编号文本
        StackPane pawnWithLabel = new StackPane();
        Label numberLabel = new Label(playerNumber);
        numberLabel.setTextFill(pawnColor.equalsIgnoreCase("WHITE") || pawnColor.equalsIgnoreCase("YELLOW") ? 
                                Color.BLACK : Color.WHITE);
        numberLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        
        pawnWithLabel.getChildren().addAll(circle, numberLabel);
        pawnsPane.getChildren().add(pawnWithLabel);
        
        // 添加玩家信息提示
        pawnWithLabel.setOnMouseEntered(e -> {
            setTooltip(player.getName() + " (" + player.getRole() + ")");
            e.consume();
        });
        
        pawnWithLabel.setOnMouseExited(e -> {
            clearTooltip();
            e.consume();
        });
    }
    
    private void setTooltip(String text) {
        javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(text);
        javafx.scene.control.Tooltip.install(this, tooltip);
    }
    
    private void clearTooltip() {
        javafx.scene.control.Tooltip.uninstall(this, null);
    }
    
    /**
     * 获取此视图关联的板块
     */
    public IslandTile getTile() {
        return tile;
    }
} 