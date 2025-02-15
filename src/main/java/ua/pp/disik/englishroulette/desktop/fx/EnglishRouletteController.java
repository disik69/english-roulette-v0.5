package ua.pp.disik.englishroulette.desktop.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.GridPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnglishRouletteController {
    @FXML
    private MenuBar menu;

    @FXML
    private void initialize() throws Exception {

    }

    public void handleCreation(ActionEvent event) {
    }

    public void handleExit(ActionEvent event) {
        System.exit(0);
    }

    public void handleReading(ActionEvent event) {
    }

    public void handleMemory(ActionEvent event) {
    }

    public void handleRepeating(ActionEvent event) {
    }
}
