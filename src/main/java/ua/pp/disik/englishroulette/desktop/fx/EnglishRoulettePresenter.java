package ua.pp.disik.englishroulette.desktop.fx;

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

import java.util.List;

@Component
@Slf4j
public class EnglishRoulettePresenter {
    @Autowired
    private ExerciseService exerciseService;

    @FXML
    private TableView<ExerciseDao> exerciseTable;

    @FXML
    private TableColumn<ExerciseDao, List<Phrase>> exerciseTableColumnForeign;

    @FXML
    private TableColumn<ExerciseDao, List<Phrase>> exerciseTableColumnNative;

    @FXML
    private TableColumn<ExerciseDao, Integer> exerciseTableColumnReading;

    @FXML
    private TableColumn<ExerciseDao, Integer> exerciseTableColumnMemory;

    @FXML
    private TableColumn<ExerciseDao, Integer> exerciseTableColumnPriority;

    @FXML
    private TableColumn<ExerciseDao, Long> exerciseTableColumnChecked;

    @FXML
    private void initialize() {
        exerciseTable.setItems(FXCollections.observableArrayList(exerciseService.findAll()));
        exerciseTableColumnForeign.setCellValueFactory(new PropertyValueFactory<>("getForeignPhrases"));
        exerciseTableColumnNative.setCellValueFactory(new PropertyValueFactory<>("getNativePhrases"));
        exerciseTableColumnReading.setCellValueFactory(new PropertyValueFactory<>("getReadingCount"));
        exerciseTableColumnMemory.setCellValueFactory(new PropertyValueFactory<>("getMemoryCount"));
        exerciseTableColumnPriority.setCellValueFactory(new PropertyValueFactory<>("getPriority"));
        exerciseTableColumnChecked.setCellValueFactory(new PropertyValueFactory<>("getCheckedAt"));
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
