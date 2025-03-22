package ua.pp.disik.englishroulette.desktop.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.freetts.FreeTTSSpeaker;
import ua.pp.disik.englishroulette.desktop.fx.entity.CurrentLesson;
import ua.pp.disik.englishroulette.desktop.lesson.Lesson;

@Component
public class LessonController {
    private final String AVERS_STYLE = "" +
            "-fx-font-size: 30px; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px; " +
            "-fx-border-style: dotted; " +
            "-fx-border-color: black; ";
    private final String REVERS_STYLE = "" +
            "-fx-font-size: 30px; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px; " +
            "-fx-border-style: dotted; " +
            "-fx-border-color: blue; ";

    @Autowired
    private CurrentLesson currentLesson;

    @Autowired
    private FreeTTSSpeaker freeTTSSpeaker;

    private boolean exerciseRevers = false;

    @FXML
    private GridPane main;

    @FXML
    private Label numberLabel;

    @FXML
    private Label countLabel;

    @FXML
    private Label exerciseLabel;

    @FXML
    private void initialize() {
        setCurrentExercise();
    }

    private void setCurrentExercise() {
        Lesson lesson = currentLesson.getLesson();
        if (lesson.getAmmount() > 0) {
            numberLabel.setText(String.valueOf(lesson.getAmmount()));
            countLabel.setText(String.valueOf(lesson.getCurrentCount()));
            exerciseLabel.setText(String.valueOf(lesson.getCurrentAvers()));
            exerciseLabel.setStyle(AVERS_STYLE);
            exerciseRevers = false;
        } else {
            numberLabel.setText("");
            countLabel.setText("");
            exerciseLabel.setText("");
            exerciseLabel.setStyle("");
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

        main.getScene().getWindow().hide();
    }

    public void handleTurn(MouseEvent event) {
        Lesson lesson = currentLesson.getLesson();
        if (! exerciseRevers) {
            exerciseLabel.setText(String.valueOf(lesson.getCurrentRevers()));
            exerciseLabel.setStyle(REVERS_STYLE);
            exerciseRevers = true;
        } else {
            exerciseLabel.setText(String.valueOf(lesson.getCurrentAvers()));
            exerciseLabel.setStyle(AVERS_STYLE);
            exerciseRevers = false;
        }
    }

    public void handleSpeak(ActionEvent event) {
        freeTTSSpeaker.speak(exerciseLabel.getText());
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
