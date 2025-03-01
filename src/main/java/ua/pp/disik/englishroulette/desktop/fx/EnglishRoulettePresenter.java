package ua.pp.disik.englishroulette.desktop.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.fx.entity.*;
import ua.pp.disik.englishroulette.desktop.service.ExerciseService;

import java.util.List;

@Component
@Slf4j
public class EnglishRoulettePresenter {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private CurrentExercise currentExercise;

    @FXML
    private VBox main;

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
        TableView.TableViewSelectionModel<ExerciseReadDto> tableViewSelectionModel =
                exerciseTable.getSelectionModel();
        tableViewSelectionModel.setSelectionMode(SelectionMode.MULTIPLE);

        exerciseTableColumnForeign.setCellValueFactory(new PropertyValueFactory<>("foreignPhrases"));
        exerciseTableColumnNative.setCellValueFactory(new PropertyValueFactory<>("nativePhrases"));
        exerciseTableColumnReading.setCellValueFactory(new PropertyValueFactory<>("readingCount"));
        exerciseTableColumnMemory.setCellValueFactory(new PropertyValueFactory<>("memoryCount"));
        exerciseTableColumnPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        exerciseTableColumnChecked.setCellValueFactory(new PropertyValueFactory<>("checkedAt"));

        updateTableView();
    }

    public void handleCreate(ActionEvent event) {
        writeExercise(null);
    }

    private ObservableList<ExerciseReadDto> getSelectedExercises() {
        return exerciseTable.getSelectionModel().getSelectedItems();
    }

    public void handleUpdate(ActionEvent event) {
        ObservableList<ExerciseReadDto> selectedExercises = getSelectedExercises();
        if (selectedExercises.size() == 1) {
            writeExercise(selectedExercises.get(0).getId());
        }
    }

    public void handleDelete(ActionEvent event) {
        List<Integer> ids = getSelectedExercises().stream()
                .map(exercise -> exercise.getId())
                .toList();
        exerciseService.repository().deleteAllById(ids);

        updateTableView();
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

    private void updateTableView() {
        exerciseTable.setItems(FXCollections.observableArrayList(exerciseService.findAll()));
    }

    @SneakyThrows
    private void writeExercise(Integer exerciseId) {
        currentExercise.setId(exerciseId);

        FXMLLoader viewLoader = new FXMLLoader(
                ExercisePresenter.class.getResource("ExerciseView.fxml")
        );
        viewLoader.setControllerFactory(clazz -> applicationContext.getBean(clazz));
        GridPane exerciseView = viewLoader.load();

        Scene scene = new Scene(exerciseView);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(main.getScene().getWindow());
        stage.setWidth(400);
        stage.setHeight(800);
        stage.setTitle("Exercise");
        stage.showAndWait();

        updateTableView();
    }
}
