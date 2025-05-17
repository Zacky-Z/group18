package com.forbiddenisland.core.model;

import com.forbiddenisland.enums.AdventurerType;
import com.forbiddenisland.core.model.IslandTile;
import com.forbiddenisland.ui.util.PositionUtils;

import java.util.ArrayList;
import java.util.List;

public class Adventurer extends Player {
    private final AdventurerType type;
    private List<Treasure> capturedFigurines; // collection of treasures this adventurer has captured
    private boolean currentPlayer; // indicates if this is the active player (for UI)

    public Adventurer(String name, AdventurerType type) {
        super(name);
        this.type = type;
        this.capturedFigurines = new ArrayList<>();
        this.currentPlayer = false;
    }

    // Method to check if this is the current active player
    public boolean isCurrentPlayer() {
        return currentPlayer;
    }
    
    // Method for the game to set the current player
    public void setCurrentPlayer(boolean isCurrentPlayer) {
        this.currentPlayer = isCurrentPlayer;
    }

    public AdventurerType getType() {
        return type;
    }
    
    public List<Treasure> getCapturedFigurines() {
        return capturedFigurines;
    }
    
    // Returns the image path for pawn based on adventurer type
    public String getPawnImg() {
        // Some types need special handling due to different naming in image files
        switch (type) {
            case PILOT:
                return "/Adventurers/Pilot.png";  // note: capitals are important in file paths
            case ENGINEER:
                return "/Adventurers/Engineer.png";
            case MESSENGER:
                return "/Adventurers/Messenger.png";
            case DIVER:
                return "/Adventurers/Diver.png";
            case EXPLORER:
                return "/Adventurers/Explorer.png";
            case NAVIGATOR:
                return "/Adventurers/Navigator.png";
            default:
                // This won't happen but compiler wants it
                return "/Adventurers/Default.png";
        }
    }
    
    // Handles special movement abilities for different adventurer types
    // Validates move based on the adventurer's special abilities
    public boolean canMoveTo(IslandTile targetTile, List<IslandTile> allTiles) {
        if (targetTile == null || targetTile.isSunk()) {
            return false;
        }
        
        IslandTile currentTile = getCurrentTile();
        if (currentTile == null) {
            return false; // can't move if not on any tile
        }
        
        // get adjacent tiles
        List<IslandTile> adjacentTiles = PositionUtils.getAdjacentTiles(currentTile, allTiles);
        
        // Check special abilities
        switch (type) {
            case EXPLORER: // Explorer can move diagonally
                return adjacentTiles.contains(targetTile) || isDiagonalTo(currentTile, targetTile);
                
            case DIVER: // Diver can move through sunk tiles
                return canDiverMoveTo(targetTile, allTiles);
                
            case PILOT: // Pilot can fly to any tile once per turn
                // code shoud take account if already used pilot power in the turn
                return !targetTile.isSunk(); // Pilot can fly to any non-sunk tile
            
            case NAVIGATOR: // Can move other players up to 2 tiles
                /* This special ability affects other players, not this adventurer's movement
                   Will be handled in a different method */
                return adjacentTiles.contains(targetTile);
                
            case ENGINEER: // Engineer can shore up 2 tiles for 1 action
                // The special ability is for shore up action, not movement
                return adjacentTiles.contains(targetTile);
                
            case MESSENGER: // Messenger can give treasure cards to other players not on same tile
                // The special ability is for giving cards, not movement
                return adjacentTiles.contains(targetTile);
                
            default:
                return adjacentTiles.contains(targetTile);
        }
    }

    // Determines if a tile is diagonal to current position
    private boolean isDiagonalTo(IslandTile current, IslandTile target) {
        // Need to check if dx and dy are both 1
        int dx = Math.abs(current.getX() - target.getX());
        int dy = Math.abs(current.getY() - target.getY());
        return dx == 1 && dy == 1 && !target.isSunk();
    }
    
    // Special movement logic for Diver - finds path through sunken tiles
    private boolean canDiverMoveTo(IslandTile targetTile, List<IslandTile> allTiles) {
        if (getCurrentTile().equals(targetTile)) {
            return true; // Already on the target tile
        }
        
        // Direct adjacent non-sunken tiles
        List<IslandTile> adjacentTiles = PositionUtils.getAdjacentTiles(getCurrentTile(), allTiles);
        if (adjacentTiles.contains(targetTile)) {
            return true;
        }
        
        // Check for path through sunken tiles using BFS
        List<IslandTile> visited = new ArrayList<>();
        List<IslandTile> queue = new ArrayList<>();
        
        // Start with adjacent tiles, even if sunken
        for (IslandTile tile : allTiles) {
            if (isAdjacentEvenIfSunken(getCurrentTile(), tile)) {
                queue.add(tile);
            }
        }
        
        while (!queue.isEmpty()) {
            IslandTile current = queue.remove(0);
            
            if (visited.contains(current)) {
                continue;
            }
            
            visited.add(current);
            
            // If we found a non-sunken tile and it's our target, we're done
            if (!current.isSunk() && current.equals(targetTile)) {
                return true;
            }
            
            // If it's a non-sunken tile, don't explore further from here
            if (!current.isSunk() && !current.equals(getCurrentTile())) {
                continue; 
            }
            
            // Add adjacent tiles to this one to the queue
            for (IslandTile tile : allTiles) {
                if (isAdjacentEvenIfSunken(current, tile) && !visited.contains(tile)) {
                    queue.add(tile);
                }
            }
        }
        
        return false; // No path found
    }

    // Checks if tiles are adjacent, regardless of sunken status
    private boolean isAdjacentEvenIfSunken(IslandTile a, IslandTile b) {
        return (Math.abs(a.getX() - b.getX()) == 1 && a.getY() == b.getY()) ||
               (Math.abs(a.getY() - b.getY()) == 1 && a.getX() == b.getX());
    }
    
    // Validates if adventurer can shore up the specified tile
    public boolean canShoreUp(IslandTile targetTile, List<IslandTile> allTiles) {
        if (targetTile == null || !targetTile.isFlooded()) {
            return false; // Can't shore up a null tile or one that isn't flooded
        }
        
        IslandTile currentTile = getCurrentTile();
        if (currentTile == null) {
            return false;
        }
        
        // Explorer can shore up diagonally
        if (type == AdventurerType.EXPLORER) {
            return currentTile.equals(targetTile) || 
                   PositionUtils.getAdjacentTiles(currentTile, allTiles).contains(targetTile) ||
                   isDiagonalTo(currentTile, targetTile);
        }
        
        // All other adventurers need to be on the tile or adjacent
        return currentTile.equals(targetTile) || 
               PositionUtils.getAdjacentTiles(currentTile, allTiles).contains(targetTile);
    }
    
    // Determines if this adventurer can give a card to receiver
    public boolean canGiveCard(Adventurer receiver) {
        if (receiver == null) {
            return false;
        }
        
        // Messenger can give cards to players regardless of location
        if (type == AdventurerType.MESSENGER) {
            return true;
        }
        
        // Other adventurers must be on the same tile
        return getCurrentTile() != null && 
               getCurrentTile().equals(receiver.getCurrentTile());
    }
    
    // Checks if adventurer can capture treasure at current location
    public boolean canCaptureTreasure(int treasureCardsRequired) {
        IslandTile currentTile = getCurrentTile();
        if (currentTile == null || currentTile.isFlooded() || currentTile.isSunk() || currentTile.getTreasure() == null) {
            return false;
        }
        
        // Count how many matching treasure cards we have
        int matchingCards = 0;
        for (TreasureCard card : getTreasureCards()) {
            if (card.getTreasureType() == currentTile.getTreasure()) {
                matchingCards++;
            }
        }
        
        return matchingCards >= treasureCardsRequired;
    }
    
    // Navigator's special ability implementation
    public boolean canNavigate(Adventurer targetPlayer, IslandTile destination, List<IslandTile> allTiles) {
        if (type != AdventurerType.NAVIGATOR || targetPlayer == null || destination == null) {
            return false;
        }
        
        // Navigator can move other players up to 2 adjacent tiles
        int distance = PositionUtils.manhattanDistance(targetPlayer.getCurrentTile(), destination);
        return distance <= 2 && !destination.isSunk();
    }
}    