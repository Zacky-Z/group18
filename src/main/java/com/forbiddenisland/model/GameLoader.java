package com.forbiddenisland.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class GameLoader {

    /**
     * Load the game state from a file
     * @param filePath The path of the save file
     * @return The loaded game object
     */
    public static Game loadGame(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("Save file does not exist: " + filePath);
            return null;
        }

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (Game) in.readObject();
        } catch (IOException e) {
            System.err.println("An I/O error occurred while reading the save file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Game class not found: " + e.getMessage());
        }
        return null;
    }
}