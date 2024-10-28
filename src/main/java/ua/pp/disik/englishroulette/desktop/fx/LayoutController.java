package ua.pp.disik.englishroulette.desktop.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class LayoutController {
    private AnchorPane vocabulary;

    private AnchorPane exercise;
    private ExerciseController exerciseController;

    @FXML
    private VBox layout;

    @FXML
    private void initialize() throws Exception {
        FXMLLoader vocabularyLoader = new FXMLLoader(
                FxApplicationRunner.class.getResource("VocabularyController.fxml")
        );
        vocabulary = vocabularyLoader.load();

        FXMLLoader exerciseLoader = new FXMLLoader(
                FxApplicationRunner.class.getResource("ExerciseController.fxml")
        );
        exercise = exerciseLoader.load();
        exerciseController = exerciseLoader.getController();
    }

    public void handleVocabulary(ActionEvent event) {
        setLayout(vocabulary);
    }

    public void handleReading(ActionEvent event) {
        exerciseController.setLabel("Reading");
        setLayout(exercise);
    }

    public void handleMemory(ActionEvent event) {
        exerciseController.setLabel("Memory");
        setLayout(exercise);
    }

    public void handleRepeating(ActionEvent event) {
        exerciseController.setLabel("Repeating");
        setLayout(exercise);
    }

    private void setLayout(AnchorPane anchorPane) {
        layout.getChildren().clear();
        layout.getChildren().add(anchorPane);
    }
}
