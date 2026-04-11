package ua.pp.disik.englishroulette.desktop.fx;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SelfCheckLessonController {
    private final BooleanProperty disabledYesNoProperty = new SimpleBooleanProperty();

    private Voice voice;
    private boolean reversExercise = false;
    private boolean turnableExercise = true;

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
        voice = VoiceManager.getInstance().getVoice("kevin16");
        voice.allocate();

        yesButton.disableProperty().bind(disabledYesNoProperty);
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

            if (lesson.rewind()) {
                card.getStyleClass().add("card");

                setCurrentExercise();
            } else {
                main.getScene().getWindow().hide();
            }
        }
    }

    public void handleTurn(MouseEvent event) {
        if (turnableExercise) {
            if (reversExercise) {
                setAvers();
            } else {
                setRevers();
            }
        }
    }

    private void setAvers() {
        disabledYesNoProperty.set(true);

        card.getStyleClass().remove("revers-card");

        Lesson lesson = currentLesson.getLesson();
        ExerciseSide side = lesson.getCurrentAvers();
        exerciseLabel.setText(convertToCard(side.getPhrases()));
        speakSide(side);

        reversExercise = false;
    }

    private void setRevers() {
        disabledYesNoProperty.set(false);

        card.getStyleClass().add("revers-card");

        Lesson lesson = currentLesson.getLesson();
        ExerciseSide side = lesson.getCurrentRevers();
        exerciseLabel.setText(convertToCard(side.getPhrases()));
        speakSide(side);

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
            new Thread(() -> voice.speak(speakingLine)).start();
        }
    }

    public void handleYES(ActionEvent event) {
        if (! disabledYesNoProperty.get()) {
            currentLesson.getLesson().rememberCurrent();
            currentLesson.getLesson().next();

            setCurrentExercise();
        }
    }

    public void handleNO(ActionEvent actionEvent) {
        if (! disabledYesNoProperty.get()) {
            disabledYesNoProperty.set(true);
            turnableExercise = false;
            exerciseLabel.getStyleClass().add("exercise-error");

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        log.debug("NO handler was interrupted.");
                    } finally {
                        succeeded();
                    }
                    return null;
                }
            };
            task.setOnSucceeded(workStateEvent -> {
                exerciseLabel.getStyleClass().remove("exercise-error");
                turnableExercise = true;

                currentLesson.getLesson().dontRememberCurrent();
                currentLesson.getLesson().next();

                setCurrentExercise();
            });
            new Thread(task).start();
        }
    }
}