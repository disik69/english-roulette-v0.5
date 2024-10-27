package ua.pp.disik.englishroulette.desktop.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class LayoutController {
    @FXML
    private Menu vocabulary;

    @FXML
    private MenuItem reading;

    @FXML
    private MenuItem memory;

    @FXML
    private MenuItem repeating;

    @FXML
    private AnchorPane layout;

    @FXML
    private void initialize() {

    }

    public void handleVocabulary(ActionEvent event) {
        setLabel("Vocabulary");
    }

    public void handleReading(ActionEvent event) {
        setLabel("Reading");
    }

    public void handleMemory(ActionEvent event) {
        setLabel("Memory");
    }

    public void handleRepeating(ActionEvent event) {
        setLabel("Repeating");
    }

    private void setLabel(String text) {
        int size = layout.getChildren().size();
        layout.getChildren().clear();
        layout.getChildren().add(new Label(text));
    }
}
