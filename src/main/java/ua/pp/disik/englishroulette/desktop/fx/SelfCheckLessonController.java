package ua.pp.disik.englishroulette.desktop.fx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.fx.entity.CurrentLesson;
import ua.pp.disik.englishroulette.desktop.fx.stage.MessageStage;
import ua.pp.disik.englishroulette.desktop.lesson.Lesson;

import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SelfCheckLessonController {
    private final BooleanProperty disabledYesNoProperty = new SimpleBooleanProperty();

    private boolean reversExercise = false;

    @Autowired
    private CurrentLesson currentLesson;

    @FXML
    private GridPane main;

    @FXML
    private Label numberLabel;

    @FXML
    private Label countLabel;

    @FXML
    private StackPane card;

    @FXML
    private Label exerciseLabel;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    @FXML
    private void initialize() {
        yesButton.disableProperty().bind(disabledYesNoProperty);
        noButton.disableProperty().bind(disabledYesNoProperty);

        setCurrentExercise();
    }

    public void close() {}

    private void setCurrentExercise() {
        Lesson lesson = currentLesson.getLesson();
        if (lesson.getAmmount() > 0) {
            numberLabel.setText(String.valueOf(lesson.getAmmount()));
            countLabel.setText(String.valueOf(lesson.getCurrentCount()));

            setAvers();
        } else {
            numberLabel.setText("");
            countLabel.setText("");

            card.getStyleClass().removeAll("card", "revers-card");
            exerciseLabel.setText("");

            disabledYesNoProperty.set(true);

            MessageStage result = new MessageStage(
                    "Result",
                    lesson.getSuccessNumber() + " from " + lesson.getAllNumber(),
                    main
            );
            result.showAndWait();

            main.getScene().getWindow().hide();
        }
    }

    public void handleTurn(MouseEvent event) {
        if (reversExercise) {
            setAvers();
        } else {
            setRevers();
        }
    }

    private void setAvers() {
        disabledYesNoProperty.set(true);

        card.getStyleClass().remove("revers-card");

        Lesson lesson = currentLesson.getLesson();
        Lesson.Side side = lesson.getCurrentAvers();
        exerciseLabel.setText(convertToCard(side.getPhrases()));
        speakSide(side);

        reversExercise = false;
    }

    private void setRevers() {
        disabledYesNoProperty.set(false);

        card.getStyleClass().add("revers-card");

        Lesson lesson = currentLesson.getLesson();
        Lesson.Side side = lesson.getCurrentRevers();
        exerciseLabel.setText(convertToCard(side.getPhrases()));
        speakSide(side);

        reversExercise = true;
    }

    private String convertToCard(List<String> phrases) {
        return phrases.stream()
                .reduce((first, second) -> first + Constants.PHRASE_DIVIDER + second)
                .orElse("");
    }

    private void speakSide(Lesson.Side side) {}

    public void handleYES(ActionEvent event) {
        if (! disabledYesNoProperty.get()) {
            currentLesson.getLesson().rememberCurrent();
            currentLesson.getLesson().next();

            setCurrentExercise();
        }
    }

    public void handleNO(ActionEvent event) {
        if (! disabledYesNoProperty.get()) {
            currentLesson.getLesson().dontRememberCurrent();
            currentLesson.getLesson().next();

            setCurrentExercise();
        }
    }
}