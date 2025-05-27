module com.forbiddenisland {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.forbiddenisland to javafx.fxml;
    exports com.forbiddenisland;
}