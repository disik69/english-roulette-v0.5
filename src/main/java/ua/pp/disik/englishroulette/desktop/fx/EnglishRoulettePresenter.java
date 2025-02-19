package ua.pp.disik.englishroulette.desktop.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.Phrase;
import ua.pp.disik.englishroulette.desktop.entity.Priority;
import ua.pp.disik.englishroulette.desktop.entity.SettingName;
import ua.pp.disik.englishroulette.desktop.repository.ExerciseRepository;
import ua.pp.disik.englishroulette.desktop.repository.PhraseRepository;
import ua.pp.disik.englishroulette.desktop.service.SettingService;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class EnglishRoulettePresenter {
    @Autowired
    private ExerciseRepository exerciseRepository;

    @FXML
    private MenuBar menu;

    @FXML
    private void initialize() {

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
