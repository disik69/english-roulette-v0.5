package ua.pp.disik.englishroulette.desktop.fx;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.fx.entity.CurrentLesson;
import ua.pp.disik.englishroulette.desktop.lesson.Lesson;

import java.util.Arrays;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LessonController {
    private Voice voice;
    private boolean reversExercise = false;
    private boolean checkedExercise = false;

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
    private StackPane check;

    @FXML
    private TextArea checkText;

    @FXML
    private Button nextButton;

    @FXML
    private void initialize() {
        voice = VoiceManager.getInstance().getVoice("kevin16");
        voice.allocate();

        setCurrentExercise();
    }

    private void setCurrentExercise() {
        Lesson lesson = currentLesson.getLesson();
        if (lesson.getAmmount() > 0) {
            numberLabel.setText(String.valueOf(lesson.getAmmount()));
            countLabel.setText(String.valueOf(lesson.getCurrentCount()));

            checkedExercise = false;

            checkText.setText("");

            nextButton.setDisable(true);

            setAvers();
        } else {
            numberLabel.setText("");
            countLabel.setText("");

            card.getStyleClass().removeAll("card", "revers-card");
            exerciseLabel.setText("");

            check.getStyleClass().removeAll("card", "revers-card");
            checkText.setText("");

            nextButton.setDisable(true);

            MessageStage result = new MessageStage(
                    "Result",
                    lesson.getSuccessNumber() + " from " + lesson.getAllNumber(),
                    main
            );
            result.showAndWait();

            voice.deallocate();

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
        card.getStyleClass().remove("revers-card");

        Lesson lesson = currentLesson.getLesson();
        Lesson.Side side = lesson.getCurrentAvers();
        exerciseLabel.setText(convertToCard(side.getPhrases()));
        speakSide(side);

        if (exerciseLabel.getStyleClass().contains("exercise-error")) {
            exerciseLabel.getStyleClass().remove("exercise-error");
        }

        reversExercise = false;
    }

    private void setRevers() {
        card.getStyleClass().add("revers-card");

        Lesson lesson = currentLesson.getLesson();
        Lesson.Side side = lesson.getCurrentRevers();
        exerciseLabel.setText(convertToCard(side.getPhrases()));
        speakSide(side);

        boolean wordComparative = compareWords(side.getPhrases(), convertFromCard(checkText.getText()));

        if (! checkedExercise) {
            if (wordComparative) {
                currentLesson.getLesson().rememberCurrent();
            } else {
                currentLesson.getLesson().dontRememberCurrent();
            }
            checkedExercise = true;
        }

        if (wordComparative) {
            if (exerciseLabel.getStyleClass().contains("exercise-error")) {
                exerciseLabel.getStyleClass().remove("exercise-error");
            }
            nextButton.setDisable(false);
        } else {
            if (! exerciseLabel.getStyleClass().contains("exercise-error")) {
                exerciseLabel.getStyleClass().add("exercise-error");
            }
            nextButton.setDisable(true);
        }

        reversExercise = true;
    }

    private void speakSide(Lesson.Side side) {
        if (side.isSpoken()) {
            String card = convertToCard(side.getPhrases());
            new Thread(() -> voice.speak(card)).start();
        }
    }

    public void handleNext(ActionEvent event) {
        currentLesson.getLesson().next();

        setCurrentExercise();
    }

    private String convertToCard(List<String> phrases) {
        return phrases.stream()
                .reduce((first, second) -> first + ",\n" + second)
                .orElse("");
    }

    private List<String> convertFromCard(String card) {
        return Arrays.stream(card.split(","))
                .map(word -> word.trim())
                .toList();
    }

    private boolean compareWords(List<String> cardPharases, List<String> checkPhrases) {
        for (String cardPhrase : cardPharases) {
            boolean found = false;
            for (String checkPhrase : checkPhrases) {
                if (cardPhrase.equals(checkPhrase)) {
                    found = true;
                    break;
                }
            }
            if (! found) {
                return false;
            }
        }
        return true;
    }
}
