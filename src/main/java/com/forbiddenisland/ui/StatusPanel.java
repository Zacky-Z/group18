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
 * Status Panel - Displays game status information
 */
public class StatusPanel extends HBox {

    private Label statusLabel;

    public StatusPanel() {
        setPadding(new Insets(10));
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setMinHeight(50);
        setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #ddd; -fx-border-radius: 5;");

        // Add shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5.0);
        shadow.setOffsetX(2.0);
        shadow.setOffsetY(2.0);
        shadow.setColor(Color.color(0, 0, 0, 0.3));
        setEffect(shadow);

        statusLabel = new Label("Game ready");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        statusLabel.setTextFill(Color.DARKBLUE);

        getChildren().add(statusLabel);
    }

    /**
     * Sets the status text
     * @param status Status text to display
     */
    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    /**
     * Gets the current status text
     * @return Current status text
     */
    public String getStatus() {
        return statusLabel.getText();
    }
}