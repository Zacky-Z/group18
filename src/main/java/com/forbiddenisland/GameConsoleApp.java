package com.forbiddenisland;

import com.forbiddenisland.model.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Console version of the Forbidden Island game for testing game logic.
 */
public class GameConsoleApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = null;

        System.out.println("=== Forbidden Island Game (Console Version) ===");
        System.out.println("1. Start New Game");
        System.out.println("2. Load Game");
        System.out.print("Please choose: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            System.out.println("Initializing game...");

            // Create 2-4 players
            List<String> playerNames = Arrays.asList("Player 1", "Player 2", "Player 3", "Player 4");

            // Difficulty level (1-4): Novice, Normal, Elite, Legendary
            int difficulty = 1; // Novice difficulty

            // Create game instance
            game = new Game(playerNames, difficulty);
        } else if (choice == 2) {
            System.out.print("Please enter the save file path: ");
            String filePath = scanner.nextLine();
            game = GameLoader.loadGame(filePath);
            if (game == null) {
                System.out.println("Failed to load save file, starting new game.");
                // Create 2-4 players
                List<String> playerNames = Arrays.asList("Player 1", "Player 2", "Player 3", "Player 4");
                // Difficulty level (1-4): Novice, Normal, Elite, Legendary
                int difficulty = 1; // Novice difficulty
                game = new Game(playerNames, difficulty);
            }
        } else {
            System.out.println("Invalid choice, starting new game.");
            // Create 2-4 players
            List<String> playerNames = Arrays.asList("Player 1", "Player 2", "Player 3", "Player 4");
            // Difficulty level (1-4): Novice, Normal, Elite, Legendary
            int difficulty = 1; // Novice difficulty
            game = new Game(playerNames, difficulty);
        }

        System.out.println("\nGame initialization complete!");
        System.out.println("Press Enter to continue...");
        waitForEnter();

        while (true) {
            System.out.println("\n1. Take Turn");
            System.out.println("2. Save Game");
            System.out.println("3. Exit Game");
            System.out.print("Please choose: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.println("\n===== Turn =====");
                game.runTurn();

                // Check if game is over
                if (game.checkGameOverConditions()) {
                    System.out.println("\nGame Over - Players Lost!");
                    break;
                }

                if (game.checkWinConditions()) {
                    System.out.println("\nGame Over - Players Won!");
                    break;
                }

                System.out.println("\nTurn ended. Press Enter to continue...");
                waitForEnter();
            } else if (choice == 2) {
                System.out.print("Please enter the save file path: ");
                String filePath = scanner.nextLine();
                game.saveGame(filePath);
            } else if (choice == 3) {
                System.out.println("\nExiting game. Thanks for playing!");
                break;
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void waitForEnter() {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}