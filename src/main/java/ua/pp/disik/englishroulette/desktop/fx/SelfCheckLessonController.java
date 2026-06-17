package ua.pp.disik.englishroulette.desktop.fx;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
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
import ua.pp.disik.englishroulette.desktop.lesson.exercise.ExerciseSide;

import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SelfCheckLessonController {
    private final BooleanProperty disabledYesNoProperty = new SimpleBooleanProperty();
    private final BooleanProperty disabledNextProperty = new SimpleBooleanProperty();

    private Voice voice;
    private boolean reversExercise = false;
    private boolean yesAction = false;
    private boolean noAction = false;

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
    private Button nextButton;

    @FXML
    private Button noButton;

    @FXML
    private void initialize() {
        voice = VoiceManager.getInstance().getVoice("kevin16");
        voice.allocate();

        yesButton.disableProperty().bind(disabledYesNoProperty);
        nextButton.disableProperty().bind(disabledNextProperty);
        noButton.disableProperty().bind(disabledYesNoProperty);

        setCurrentExercise();
    }

    public void close() {
        voice.deallocate();
    }

    private void setCurrentExercise() {
        Lesson lesson = currentLesson.getLesson();
        if (lesson.getAmount() > 0) {
            numberLabel.setText(String.valueOf(lesson.getAmount()));
            countLabel.setText(String.valueOf(lesson.getCurrentCount()));

            disabledNextProperty.set(true);

            yesAction = false;
            noAction = false;

            setAvers();
        } else {
            numberLabel.setText("");
            countLabel.setText("");

            card.getStyleClass().removeAll("card", "revers-card");
            exerciseLabel.setText("");

            disabledYesNoProperty.set(true);
            disabledNextProperty.set(true);

            MessageStage result = new MessageStage(
                    "Result",
                    lesson.getSuccessNumber() + " from " + lesson.getAllNumber(),
                    main
            );
            result.showAndWait();

            if (lesson.rewind()) {
                card.getStyleClass().add("card");

                setCurrentExercise();
            } else {
                main.getScene().getWindow().hide();
            }
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
        if ((! yesAction) && (! noAction)) {
            disabledYesNoProperty.set(true);
        }

        card.getStyleClass().remove("revers-card");

        Lesson lesson = currentLesson.getLesson();
        ExerciseSide side = lesson.getCurrentAvers();
        exerciseLabel.setText(convertToCard(side.getPhrases()));
        speakSide(side);

        if (exerciseLabel.getStyleClass().contains("exercise-error")) {
            exerciseLabel.getStyleClass().remove("exercise-error");
        }

        reversExercise = false;
    }

    private void setRevers() {
        if ((! yesAction) && (! noAction)) {
            disabledYesNoProperty.set(false);
        }

        card.getStyleClass().add("revers-card");

        Lesson lesson = currentLesson.getLesson();
        ExerciseSide side = lesson.getCurrentRevers();
        exerciseLabel.setText(convertToCard(side.getPhrases()));
        speakSide(side);

        if (noAction) {
            disabledNextProperty.set(false);

            if (! exerciseLabel.getStyleClass().contains("exercise-error")) {
                exerciseLabel.getStyleClass().add("exercise-error");
            }
        }

        reversExercise = true;
    }

    private String convertToCard(List<String> phrases) {
        return phrases.stream()
                .reduce((first, second) -> first + Constants.PHRASE_DIVIDER + second)
                .orElse("");
    }

    private void speakSide(ExerciseSide side) {
        if (side.isSpoken()) {
            String speakingLine = side.getPhrases().stream()
                    .reduce((first, second) -> first + "; " + second)
                    .orElse("");
            Thread speakingThread =
                    new Thread(() -> voice.speak(speakingLine));
            speakingThread.setDaemon(true);
            speakingThread.start();
        }
    }

    public void handleYes(ActionEvent event) {
        if (! disabledYesNoProperty.get()) {
            disabledYesNoProperty.set(true);
            yesAction = true;

            currentLesson.getLesson().rememberCurrent();

            disabledNextProperty.set(false);
        }
    }

    public void handleNext(ActionEvent event) {
        if (! disabledNextProperty.get()) {
            currentLesson.getLesson().next();

            if (exerciseLabel.getStyleClass().contains("exercise-error")) {
                exerciseLabel.getStyleClass().remove("exercise-error");
            }

            setCurrentExercise();
        }
    }

    public void handleNo(ActionEvent actionEvent) {
        if (! disabledYesNoProperty.get()) {
            disabledYesNoProperty.set(true);
            noAction = true;

            currentLesson.getLesson().dontRememberCurrent();

            if (! exerciseLabel.getStyleClass().contains("exercise-error")) {
                exerciseLabel.getStyleClass().add("exercise-error");
            }
        }
    }
}