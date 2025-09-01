package ua.pp.disik.englishroulette.desktop.fx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
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
    private static final int FIRST_PAGE = 1;
    private static final int PAGE_SIZE = 30;
    private static final int MIN_FILTER_LENGTH = 3;

    private final IntegerProperty currentPageProperty = new SimpleIntegerProperty(FIRST_PAGE);
    private final IntegerProperty pageCountProperty = new SimpleIntegerProperty(1);

    private TablePaginator currentPaginator;

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
    private Label currentPageLabel;

    @FXML
    private Label pageCountLabel;

    @FXML
    private void initialize() {
        filterText.textProperty().addListener(this::handleChangeFilter);

        TableView.TableViewSelectionModel<ExerciseTableItem> tableViewSelectionModel =
                exerciseTable.getSelectionModel();
        tableViewSelectionModel.setSelectionMode(SelectionMode.MULTIPLE);

        exerciseTableColumnForeign.setCellValueFactory(new PropertyValueFactory<>("foreignPhrases"));
        exerciseTableColumnNative.setCellValueFactory(new PropertyValueFactory<>("nativePhrases"));
        exerciseTableColumnReading.setCellValueFactory(new PropertyValueFactory<>("readingCount"));
        exerciseTableColumnMemory.setCellValueFactory(new PropertyValueFactory<>("memoryCount"));
        exerciseTableColumnPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        exerciseTableColumnChecked.setCellValueFactory(new PropertyValueFactory<>("checkedAt"));

        currentPageLabel.textProperty().bind(currentPageProperty.asString());
        pageCountLabel.textProperty().bind(pageCountProperty.asString());

        currentPaginator = TablePaginator.ALL;
        updateTableView(true, true);
    }

    @FunctionalInterface
    private interface TableSourceFunction<Service, Filter, Page, Size, Result> {
        Result apply(Service service, Filter filter, Page page, Size size);
    }

    @FunctionalInterface
    private interface TableCountFunction<Service, Filter, Count> {
        Count apply(Service service, Filter filter);
    }

    @AllArgsConstructor
    private enum TablePaginator {
        ALL(
                (service, filter, page, size) -> {
                    return service.findAll(page, size);
                },
                (service, filter) -> {
                    return service.countAll();
                }
        ),
        FILTER(
                (service, filter, page, size) -> {
                    return service.findAllByFilter(filter, page, size);
                },
                (service, filter) -> {
                    return service.countAllByFilter(filter);
                }
        ),
        READING(
                (service, filter, page, size) -> {
                    return service.getReading(page, size);
                },
                (service, filter) -> {
                    return service.countReading();
                }
        ),
        MEMORY(
                (service, filter, page, size) -> {
                    return service.getMemory(page, size);
                },
                (service, filter) -> {
                    return service.countMemory();
                }
        ),
        REPEATING(
                (service, filter, page, size) -> {
                    return service.getRepeating(page, size);
                },
                (service, filter) -> {
                    return service.countRepeating();
                }
        );

        @Getter
        private final TableSourceFunction<ExerciseService, String, Integer, Integer, List<ExerciseDto>> source;

        @Getter
        private final TableCountFunction<ExerciseService, String, Integer> count;
    }

    public void handleCreate(ActionEvent event) {
        if (writeExercise(null)) {
            currentPaginator = TablePaginator.ALL;
            updateTableView(true, true);
        }
    }

    public void handleFilterReset(ActionEvent event) {
        currentPaginator = TablePaginator.ALL;
        updateTableView(true, true);
    }

    public void handleReadingFilter(ActionEvent event) {
        currentPaginator = TablePaginator.READING;
        updateTableView(true, true);
    }

    public void handleMemoryFilter(ActionEvent event) {
        currentPaginator = TablePaginator.MEMORY;
        updateTableView(true, true);
    }

    public void handleRepeatingFilter(ActionEvent event) {
        currentPaginator = TablePaginator.REPEATING;
        updateTableView(true, true);
    }

    public void handleExit(ActionEvent event) {
        System.exit(0);
    }

    public void handleReading(ActionEvent event) {
        Lesson lesson = new ReadingLesson(exerciseService, settingService);
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

    public void handleRepeating(ActionEvent event) {
        Lesson lesson = new RepeatingLesson(exerciseService, settingService);
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

        currentPaginator = TablePaginator.ALL;
        updateTableView(true, true);
    }

    private ObservableList<ExerciseTableItem> getSelectedExercises() {
        return exerciseTable.getSelectionModel().getSelectedItems();
    }

    private void handleChangeFilter(
            ObservableValue<? extends String> observable,
            String oldValue,
            String newValue
    ) {
        if (newValue.length() < MIN_FILTER_LENGTH) {
            currentPaginator = TablePaginator.ALL;
        } else {
            currentPaginator = TablePaginator.FILTER;
        }
        updateTableView(false, true);
    }

    public void handleUpdate(ActionEvent event) {
        ObservableList<ExerciseTableItem> selectedExercises = getSelectedExercises();
        if (selectedExercises.size() == 1) {
            if (writeExercise(selectedExercises.getFirst().getId())) {
                currentPaginator = TablePaginator.ALL;
                updateTableView(true, true);
            }
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

                currentPaginator = TablePaginator.ALL;
                updateTableView(true, true);
            }
        }
    }

    public void handleDelete(ActionEvent event) {
        List<Integer> ids = getSelectedExercises().stream()
                .map(exercise -> exercise.getId())
                .toList();
        exerciseService.repository().deleteAllById(ids);

        currentPaginator = TablePaginator.ALL;
        updateTableView(true, true);
    }

    private void updateTableView(boolean resetFilter, boolean resetPaginator) {
        if (resetFilter) {
            filterText.setText("");
        }

        if (resetPaginator) {
            currentPageProperty.set(FIRST_PAGE);
        }
        int itemCount = currentPaginator.getCount().apply(
                exerciseService, filterText.getText()
        );
        int pageCount = (itemCount / PAGE_SIZE) + (itemCount % PAGE_SIZE > 0 ? 1 : 0);
        this.pageCountProperty.set(pageCount);

        List<ExerciseDto> exercises = currentPaginator.getSource().apply(
            exerciseService, filterText.getText(), currentPageProperty.get() - 1, PAGE_SIZE
        );


        exerciseTable.setItems(FXCollections.observableArrayList(
                exercises.stream()
                        .map(exercise -> new ExerciseTableItem(exercise))
                        .toList()
        ));
    }

    @SneakyThrows
    private boolean writeExercise(Integer exerciseId) {
        currentExercise.setId(exerciseId);
        currentExercise.setChanged(false);

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

        return currentExercise.isChanged();
    }

    public void handleLeftScroll(ActionEvent event) {
        if (currentPageProperty.get() > FIRST_PAGE) {
            currentPageProperty.set(currentPageProperty.get() - 1);

            updateTableView(false, false);
        }
    }

    public void handleRightScroll(ActionEvent event) {
        if (currentPageProperty.get() < pageCountProperty.get()) {
            currentPageProperty.set(currentPageProperty.get() + 1);

            updateTableView(false, false);
        }
    }
}
