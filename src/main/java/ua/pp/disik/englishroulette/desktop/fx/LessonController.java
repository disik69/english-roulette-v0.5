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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.fx.entity.CurrentLesson;
import ua.pp.disik.englishroulette.desktop.lesson.Lesson;

@Component
public class LessonController {
    private final String AVERS_STYLE = "" +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px; " +
            "-fx-border-style: dotted; " +
            "-fx-border-color: black; ";
    private final String REVERS_STYLE = "" +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px; " +
            "-fx-border-style: dotted; " +
            "-fx-border-color: blue; ";

    private final Voice kevin = VoiceManager.getInstance().getVoice("kevin16");

    @Autowired
    private CurrentLesson currentLesson;


    private boolean exerciseRevers = false;

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
    private void initialize() {
        kevin.allocate();

        setCurrentExercise();
    }

    private void setCurrentExercise() {
        Lesson lesson = currentLesson.getLesson();
        if (lesson.getAmmount() > 0) {
            numberLabel.setText(String.valueOf(lesson.getAmmount()));
            countLabel.setText(String.valueOf(lesson.getCurrentCount()));

            setAvers();
        } else {
            numberLabel.setText("");
            countLabel.setText("");

            card.setStyle("");

            exerciseLabel.setText("");

            renderScore(lesson.getSuccessNumber(), lesson.getAllNumber());
        }
    }

    private void renderScore(int successNumber, int allNumber) {
        Label scoreLabel = new Label(
                successNumber +
                " from " +
                allNumber
        );
        Button okButton = new Button("OK");
        VBox vBox = new VBox(scoreLabel, okButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(5));
        vBox.setSpacing(5);

        Scene scene = new Scene(vBox);

        okButton.setOnAction(event -> scene.getWindow().hide());

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(main.getScene().getWindow());
        stage.setWidth(200);
        stage.setHeight(100);
        stage.setTitle("Result");
        stage.showAndWait();

        kevin.deallocate();

        main.getScene().getWindow().hide();
    }

    public void handleTurn(MouseEvent event) {
        if (exerciseRevers) {
            setAvers();
        } else {
            setRevers();
        }
    }

    private void setAvers() {
        card.setStyle(AVERS_STYLE);

        Lesson lesson = currentLesson.getLesson();
        Lesson.Side side = lesson.getCurrentAvers();
        exerciseLabel.setText(side.getText());
        speakSide(side);

        exerciseRevers = false;
    }

    private void setRevers() {
        card.setStyle(REVERS_STYLE);

        Lesson lesson = currentLesson.getLesson();
        Lesson.Side side = lesson.getCurrentRevers();
        exerciseLabel.setText(side.getText());
        speakSide(side);

        exerciseRevers = true;
    }

    private void speakSide(Lesson.Side side) {
        if (side.isSpoken()) {
            new Thread(() -> kevin.speak(side.getText())).start();
        }
    }

    public void handleYES(ActionEvent event) {
        currentLesson.getLesson().rememberCurrentAndNext();

        setCurrentExercise();
    }

    public void handleNO(ActionEvent event) {
        currentLesson.getLesson().dontRememberCurrentAndNext();

        setCurrentExercise();
    }
}
