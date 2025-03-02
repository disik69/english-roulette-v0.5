package ua.pp.disik.englishroulette.desktop.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import org.springframework.stereotype.Component;

@Component
public class PhrasePresenter {
    @FXML
    private GridPane main;

    @FXML
    private void initialize() {

    }

    public void handleSearch(ActionEvent event) {

    }

    public void handleOK(ActionEvent event) {
        main.getScene().getWindow().hide();
    }
}
