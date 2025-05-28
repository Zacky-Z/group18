package com.forbiddenisland.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;

/**
 * 状态面板 - 显示游戏状态信息
 */
public class StatusPanel extends HBox {
    
    private Label statusLabel;
    
    public StatusPanel() {
        setPadding(new Insets(10));
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setMinHeight(50);
        setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        // 添加阴影效果
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5.0);
        shadow.setOffsetX(2.0);
        shadow.setOffsetY(2.0);
        shadow.setColor(Color.color(0, 0, 0, 0.3));
        setEffect(shadow);
        
        statusLabel = new Label("游戏准备就绪");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        statusLabel.setTextFill(Color.DARKBLUE);
        
        getChildren().add(statusLabel);
    }
    
    /**
     * 设置状态信息文本
     * @param status 状态文本
     */
    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    /**
     * 获取当前状态信息文本
     * @return 当前状态文本
     */
    public String getStatus() {
        return statusLabel.getText();
    }
} 