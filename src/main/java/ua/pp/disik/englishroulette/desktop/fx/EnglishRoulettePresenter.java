package ua.pp.disik.englishroulette.desktop.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.entity.*;
import ua.pp.disik.englishroulette.desktop.fx.entity.*;
import ua.pp.disik.englishroulette.desktop.service.ExerciseService;

import java.time.Instant;

@Component
@Slf4j
public class EnglishRoulettePresenter {
    @Autowired
    private ExerciseService exerciseService;

    @FXML
    private TableView<ExerciseReadDto> exerciseTable;

    @FXML
    private TableColumn<ExerciseReadDto, String> exerciseTableColumnForeign;

    @FXML
    private TableColumn<ExerciseReadDto, String> exerciseTableColumnNative;

    @FXML
    private TableColumn<ExerciseReadDto, Integer> exerciseTableColumnReading;

    @FXML
    private TableColumn<ExerciseReadDto, Integer> exerciseTableColumnMemory;

    @FXML
    private TableColumn<ExerciseReadDto, String> exerciseTableColumnPriority;

    @FXML
    private TableColumn<ExerciseReadDto, String> exerciseTableColumnChecked;

    @FXML
    private void initialize() {
        exerciseTableColumnForeign.setCellValueFactory(new PropertyValueFactory<>("foreignPhrases"));
        exerciseTableColumnNative.setCellValueFactory(new PropertyValueFactory<>("nativePhrases"));
        exerciseTableColumnReading.setCellValueFactory(new PropertyValueFactory<>("readingCount"));
        exerciseTableColumnMemory.setCellValueFactory(new PropertyValueFactory<>("memoryCount"));
        exerciseTableColumnPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        exerciseTableColumnChecked.setCellValueFactory(new PropertyValueFactory<>("checkedAt"));

        exerciseTable.setItems(FXCollections.observableArrayList(exerciseService.findAll()));
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
