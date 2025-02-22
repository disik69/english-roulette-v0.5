package ua.pp.disik.englishroulette.desktop.fx;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.fx.entity.CurrentExercise;
import ua.pp.disik.englishroulette.desktop.service.ExerciseService;

@Component
public class ExercisePresenter {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private CurrentExercise currentExercise;

    @FXML
    private ListView<String> foreignList;

    @FXML
    private ListView<String> nativeList;

    @FXML
    private void initialize() {
        if (currentExercise.getId() != null) {
            Exercise exercise = exerciseService.repository()
                    .findById(currentExercise.getId())
                    .orElseThrow(() -> new RuntimeException("Exercise doesn't exist."));
            foreignList.setItems(
                    FXCollections.observableArrayList(
                            exercise.getForeignPhrases().stream().map(phrase -> phrase.getBody()).toList()
                    )
            );
            nativeList.setItems(
                    FXCollections.observableArrayList(
                            exercise.getNativePhrases().stream().map(phrase -> phrase.getBody()).toList()
                    )
            );
        }
    }
}
