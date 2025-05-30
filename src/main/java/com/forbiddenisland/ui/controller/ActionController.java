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
                throw new InvalidActionException("You can only move during the Action Phase.");
            }

            Adventurer currentPlayer = gameController.getCurrentPlayer();
            if (moveAction.execute(currentPlayer, targetTile)) {
                gameController.useAction();
                showSuccessAlert("Move successful");
            } else {
                showErrorAlert("Cannot move to this location");
            }
        } catch (InvalidActionException e) {
            showErrorAlert(e.getMessage());
        }
    }

    public void handleShoreUp(IslandTile targetTile) {
        try {
            if (gameController.getCurrentPhase() != TurnPhase.ACTION) {
                throw new InvalidActionException("You can only shore up during the Action Phase.");
            }

            Adventurer currentPlayer = gameController.getCurrentPlayer();
            if (shoreUpAction.execute(currentPlayer, targetTile)) {
                gameController.useAction();
                showSuccessAlert("Shore up successful");
            } else {
                showErrorAlert("Cannot shore up this location");
            }
        } catch (InvalidActionException e) {
            showErrorAlert(e.getMessage());
        }
    }

    public void handleGiveCard(Adventurer receiver, TreasureCard card) {
        try {
            if (gameController.getCurrentPhase() != TurnPhase.ACTION) {
                throw new InvalidActionException("You can only give cards during the Action Phase.");
            }

            Adventurer currentPlayer = gameController.getCurrentPlayer();
            if (giveCardAction.execute(currentPlayer, receiver, card)) {
                gameController.useAction();
                showSuccessAlert("Card given successfully");
            } else {
                showErrorAlert("Cannot give this card");
            }
        } catch (InvalidActionException e) {
            showErrorAlert(e.getMessage());
        }
    }

    public void handleCaptureTreasure() {
        try {
            if (gameController.getCurrentPhase() != TurnPhase.ACTION) {
                throw new InvalidActionException("You can only capture treasure during the Action Phase.");
            }

            Adventurer currentPlayer = gameController.getCurrentPlayer();
            if (captureTreasureAction.execute(currentPlayer)) {
                gameController.useAction();
                showSuccessAlert("Treasure captured successfully!");
            } else {
                showErrorAlert("Cannot capture treasure at this location");
            }
        } catch (InvalidActionException e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}