package com.forbiddenisland.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * 状态面板 - 显示游戏当前状态信息
 */
public class StatusPanel extends HBox {
    
    private Label statusLabel;
    
    public StatusPanel() {
        setPadding(new Insets(10));
        setSpacing(10);
        
        setBorder(new Border(new BorderStroke(
                Color.GRAY, 
                BorderStrokeStyle.SOLID, 
                new CornerRadii(5), 
                BorderWidths.DEFAULT
        )));
        
        statusLabel = new Label("准备开始游戏");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD,
                12));
        
        getChildren().add(statusLabel);
    }
    
    /**
     * 设置状态信息文本
     * @param status 状态文本
     */
    public void setStatus(String status) {
        statusLabel.setText(status);
    }
} 