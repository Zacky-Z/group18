package com.forbiddenisland.ui.controller;

import com.forbiddenisland.core.action.*;
import com.forbiddenisland.core.model.*;
import com.forbiddenisland.core.system.GameController;
import com.forbiddenisland.enums.TurnPhase;
import com.forbiddenisland.exceptions.InvalidActionException;
import javafx.scene.control.Alert;

import java.util.List;

public class ActionController {
    private final GameController gameController;
    private final MoveAction moveAction;
    private final ShoreUpAction shoreUpAction;
    private final GiveCardAction giveCardAction;
    private final CaptureTreasureAction captureTreasureAction;

    public ActionController(GameController gameController) {
        this.gameController = gameController;
        this.moveAction = new MoveAction(gameController);
        this.shoreUpAction = new ShoreUpAction(gameController);
        this.giveCardAction = new GiveCardAction(gameController);
        this.captureTreasureAction = new CaptureTreasureAction(gameController);
    }

    public void handleMove(IslandTile targetTile) {
        try {
            if (gameController.getCurrentPhase() != TurnPhase.ACTION) {
                throw new InvalidActionException("只能在行动阶段移动");
            }
            
            Adventurer currentPlayer = gameController.getCurrentPlayer();
            if (moveAction.execute(currentPlayer, targetTile)) {
                gameController.useAction();
                showSuccessAlert("移动成功");
            } else {
                showErrorAlert("无法移动到该位置");
            }
        } catch (InvalidActionException e) {
            showErrorAlert(e.getMessage());
        }
    }

    public void handleShoreUp(IslandTile targetTile) {
        try {
            if (gameController.getCurrentPhase() != TurnPhase.ACTION) {
                throw new InvalidActionException("只能在行动阶段加固");
            }
            
            Adventurer currentPlayer = gameController.getCurrentPlayer();
            if (shoreUpAction.execute(currentPlayer, targetTile)) {
                gameController.useAction();
                showSuccessAlert("加固成功");
            } else {
                showErrorAlert("无法加固该位置");
            }
        } catch (InvalidActionException e) {
            showErrorAlert(e.getMessage());
        }
    }

    public void handleGiveCard(Adventurer receiver, TreasureCard card) {
        try {
            if (gameController.getCurrentPhase() != TurnPhase.ACTION) {
                throw new InvalidActionException("只能在行动阶段赠卡");
            }
            
            Adventurer currentPlayer = gameController.getCurrentPlayer();
            if (giveCardAction.execute(currentPlayer, receiver, card)) {
                gameController.useAction();
                showSuccessAlert("赠卡成功");
            } else {
                showErrorAlert("无法赠送该卡牌");
            }
        } catch (InvalidActionException e) {
            showErrorAlert(e.getMessage());
        }
    }

    public void handleCaptureTreasure() {
        try {
            if (gameController.getCurrentPhase() != TurnPhase.ACTION) {
                throw new InvalidActionException("只能在行动阶段获取宝物");
            }
            
            Adventurer currentPlayer = gameController.getCurrentPlayer();
            if (captureTreasureAction.execute(currentPlayer)) {
                gameController.useAction();
                showSuccessAlert("成功获取宝物!");
            } else {
                showErrorAlert("无法在此位置获取宝物");
            }
        } catch (InvalidActionException e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("成功");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}    