package ua.pp.disik.englishroulette.desktop.fx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ExerciseController {
    @FXML
    private Label label;

    @FXML
    private void initialize() {

    }

    public void setLabel(String text) {
        label.setText(text);
    }
}
