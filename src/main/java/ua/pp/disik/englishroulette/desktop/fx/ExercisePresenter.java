package ua.pp.disik.englishroulette.desktop.fx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ExercisePresenter {
    @Autowired
    private ApplicationContext applicationContext;

    private Integer exerciseId;

    @FXML
    private Label label;

    @FXML
    private void initialize() {
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
        label.setText(String.valueOf(exerciseId));
    }
}
