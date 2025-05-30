/**
 * Forbidden Island Game Module
 * Core module configuration for the Forbidden Island game implementation
 * 
 * Dependencies:
 * - JavaFX Controls: UI components and controls
 * - JavaFX FXML: Layout and view management
 * 
 * Module Structure:
 * - Core Logic (Week 11): Player management and movement mechanics
 * - GUI Integration (Week 12): User interface and interaction
 * - Game Flow Control (Week 13): Turn management and multiplayer logic
 */
module com.forbiddenisland {
    requires javafx.controls;  // JavaFX UI components
    requires javafx.fxml;      // FXML support for layouts

    // Open the main package to JavaFX FXML for view management
    opens com.forbiddenisland to javafx.fxml;
    // Export the main package for external access
    exports com.forbiddenisland;
}