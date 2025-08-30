package ua.pp.disik.englishroulette.desktop.fx;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.ExerciseDto;
import ua.pp.disik.englishroulette.desktop.entity.SettingName;
import ua.pp.disik.englishroulette.desktop.fx.entity.*;
import ua.pp.disik.englishroulette.desktop.lesson.Lesson;
import ua.pp.disik.englishroulette.desktop.lesson.MemoryLesson;
import ua.pp.disik.englishroulette.desktop.lesson.ReadingLesson;
import ua.pp.disik.englishroulette.desktop.lesson.RepeatingLesson;
import ua.pp.disik.englishroulette.desktop.service.ExerciseService;
import ua.pp.disik.englishroulette.desktop.service.SettingService;

import java.util.List;
import java.util.Map;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class EnglishRouletteController {
    private static final int FIRST_PAGE = 0;
    private static final int PAGE_SIZE = 30;
    private static final int MIN_FILTER_LENGTH = 3;

    private int currentPage = FIRST_PAGE;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private CurrentExercise currentExercise;

    @Autowired
    private CurrentLesson currentLesson;

    @FXML
    private BorderPane main;

    @FXML
    private TextField filterText;

    @FXML
    private TableView<ExerciseTableItem> exerciseTable;

    @FXML
    private TableColumn<ExerciseTableItem, String> exerciseTableColumnForeign;

    @FXML
    private TableColumn<ExerciseTableItem, String> exerciseTableColumnNative;

    @FXML
    private TableColumn<ExerciseTableItem, Integer> exerciseTableColumnReading;

    @FXML
    private TableColumn<ExerciseTableItem, Integer> exerciseTableColumnMemory;

    @FXML
    private TableColumn<ExerciseTableItem, String> exerciseTableColumnPriority;

    @FXML
    private TableColumn<ExerciseTableItem, String> exerciseTableColumnChecked;

    @FXML
    private void initialize() {
        filterText.textProperty().addListener(this::handleInvalidateFilter);

        TableView.TableViewSelectionModel<ExerciseTableItem> tableViewSelectionModel =
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

    public void handleExit(ActionEvent event) {
        System.exit(0);
    }

    public void handleRepeating(ActionEvent event) {
        Lesson lesson = new RepeatingLesson(exerciseService, settingService);
        if (lesson.getAmmount() > 0) {
            currentLesson.setLesson(lesson);

            renderLesson();
        }
    }

    public void handleMemory(ActionEvent event) {
        Lesson lesson = new MemoryLesson(exerciseService, settingService);
        if (lesson.getAmmount() > 0) {
            currentLesson.setLesson(lesson);

            renderLesson();
        }
    }

    public void handleReading(ActionEvent event) {
        Lesson lesson = new ReadingLesson(exerciseService);
        if (lesson.getAmmount() > 0) {
            currentLesson.setLesson(lesson);

            renderLesson();
        }
    }

    @SneakyThrows
    public void renderLesson() {
        ApplicationContextFXMLLoader viewLoader = new ApplicationContextFXMLLoader(
                LessonController.class.getResource("LessonView.fxml"),
                applicationContext
        );
        GridPane LessonView = viewLoader.load();

        Scene scene = new Scene(LessonView);
        scene.getStylesheets().add(
                LessonController.class.getResource("lesson-controller.css").toExternalForm()
        );

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(main.getScene().getWindow());
        stage.setWidth(main.getScene().getWindow().getWidth());
        stage.setHeight(main.getScene().getWindow().getHeight());
        stage.setX(main.getScene().getWindow().getX() + Constants.WINDOW_OFFSET);
        stage.setY(main.getScene().getWindow().getY() + Constants.WINDOW_OFFSET);
        stage.setTitle("Lesson");
        stage.showAndWait();

        updateTableView();
    }

    private ObservableList<ExerciseTableItem> getSelectedExercises() {
        return exerciseTable.getSelectionModel().getSelectedItems();
    }

    private void handleInvalidateFilter(Observable property) {
        updateTableView();
    }

    public void handleUpdate(ActionEvent event) {
        ObservableList<ExerciseTableItem> selectedExercises = getSelectedExercises();
        if (selectedExercises.size() == 1) {
            writeExercise(selectedExercises.getFirst().getId());
        }
    }

    public void handleReset(ActionEvent event) {
        ObservableList<ExerciseTableItem> selectedExercises = getSelectedExercises();
        if (selectedExercises.size() == 1) {
            ExerciseDto exerciseDto = exerciseService.findById(selectedExercises.getFirst().getId());

            if (exerciseDto.getCheckedAt() != null) {
                Map<SettingName, String> settings = settingService.getMap();

                exerciseDto.setReadingCount(Integer.parseInt(settings.get(SettingName.READING_COUNT)));
                exerciseDto.setMemoryCount(Integer.parseInt(settings.get(SettingName.MEMORY_COUNT)));
                exerciseDto.setCheckedAt(null);
                exerciseDto.setUpdatedAt(System.currentTimeMillis());

                exerciseService.save(exerciseDto);

                updateTableView();
            }
        }
    }

    public void handleDelete(ActionEvent event) {
        List<Integer> ids = getSelectedExercises().stream()
                .map(exercise -> exercise.getId())
                .toList();
        exerciseService.repository().deleteAllById(ids);

        updateTableView();
    }

    private void updateTableView() {
        String filter = filterText.getText();
        List<ExerciseTableItem> exerciseTableItems;
        if (filter.length() >= MIN_FILTER_LENGTH) {
            exerciseTableItems =
                    exerciseService.findAllByFilter(filter, currentPage, PAGE_SIZE)
                            .stream()
                            .map(dto -> new ExerciseTableItem(dto))
                            .toList();
        } else {
            exerciseTableItems =
                    exerciseService.findAll(currentPage, PAGE_SIZE)
                            .stream()
                            .map(dto -> new ExerciseTableItem(dto))
                            .toList();
        }

        exerciseTable.setItems(FXCollections.observableArrayList(exerciseTableItems));
    }

    @SneakyThrows
    private void writeExercise(Integer exerciseId) {
        currentExercise.setId(exerciseId);

        ApplicationContextFXMLLoader viewLoader = new ApplicationContextFXMLLoader(
                ExerciseController.class.getResource("ExerciseView.fxml"),
                applicationContext
        );
        GridPane exerciseView = viewLoader.load();

        Scene scene = new Scene(exerciseView);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(main.getScene().getWindow());
        stage.setWidth(main.getScene().getWindow().getWidth() / 2);
        stage.setHeight(main.getScene().getWindow().getHeight());
        stage.setX(
                main.getScene().getWindow().getX() +
                (main.getScene().getWindow().getWidth() / 2) +
                Constants.WINDOW_OFFSET
        );
        stage.setY(main.getScene().getWindow().getY() + Constants.WINDOW_OFFSET);
        stage.setTitle("Exercise");
        stage.showAndWait();

        updateTableView();
    }

    public void handleLeftScroll(ActionEvent event) {
        if (currentPage > FIRST_PAGE) {
            currentPage--;

            updateTableView();
        }
    }

    public void handleRightScroll(ActionEvent event) {
        if (exerciseTable.getItems().size() == PAGE_SIZE) {
            currentPage++;

            updateTableView();
        }
    }
}
