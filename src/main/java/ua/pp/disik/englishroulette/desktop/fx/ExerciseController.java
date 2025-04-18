package ua.pp.disik.englishroulette.desktop.fx;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.Phrase;
import ua.pp.disik.englishroulette.desktop.entity.Priority;
import ua.pp.disik.englishroulette.desktop.entity.SettingName;
import ua.pp.disik.englishroulette.desktop.fx.entity.CurrentExercise;
import ua.pp.disik.englishroulette.desktop.fx.entity.ExerciseWriteDto;
import ua.pp.disik.englishroulette.desktop.service.ExerciseService;
import ua.pp.disik.englishroulette.desktop.service.PhraseService;
import ua.pp.disik.englishroulette.desktop.service.SettingService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ExerciseController {
    private ExerciseWriteDto currentExerciseDto;
    private Stage phraseStage;
    private int phraseCommitCount = 0;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private PhraseService phraseService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private CurrentExercise currentExercise;

    @FXML
    private GridPane main;

    @FXML
    private ListView<Phrase> foreignList;

    @FXML
    private ListView<Phrase> nativeList;

    @FXML
    private VBox priorityBox;

    @FXML
    private Button saveButton;

    @FXML
    private void initialize() {
        if (currentExercise.getId() == null) {
            currentExerciseDto = new ExerciseWriteDto();
        } else {
            currentExerciseDto = exerciseService.findById(currentExercise.getId());
        }

        renderForeignList();

        renderNativeList();

        renderPriorityBox();

        phraseStage = new Stage();
        phraseStage.setOnShown(phraseWindowEvent -> {
            main.getScene().getWindow().setOnHiding(mainWindowEvent -> phraseStage.hide());
        });
    }

    private void renderForeignList() {
        foreignList.setEditable(true);
        foreignList.setCellFactory(TextFieldListCell.forListView(phraseStringConverter()));
        foreignList.setItems(currentExerciseDto.foreignPhraseProperty());

        // Setting of an empty phrase for addition new one.
        currentExerciseDto.foreignPhraseProperty().add(new Phrase(""));

        foreignList.setOnEditStart(this::handlePhraseListStart);
        foreignList.setOnEditCancel(this::handlePhraseListCancel);
        foreignList.setOnEditCommit(this::handlePhraseListCommit);
    }

    private void renderNativeList() {
        nativeList.setEditable(true);
        nativeList.setCellFactory(TextFieldListCell.forListView(phraseStringConverter()));
        nativeList.setItems(currentExerciseDto.nativePhraseProperty());

        // Setting of an empty phrase for addition new one.
        currentExerciseDto.nativePhraseProperty().add(new Phrase(""));

        nativeList.setOnEditStart(this::handlePhraseListStart);
        nativeList.setOnEditCancel(this::handlePhraseListCancel);
        nativeList.setOnEditCommit(this::handlePhraseListCommit);
    }

    private StringConverter<Phrase> phraseStringConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Phrase object) {
                return object.getBody();
            }

            @Override
            public Phrase fromString(String string) {
                return new Phrase(string);
            }
        };
    }

    private void handlePhraseListStart(ListView.EditEvent<Phrase> event) {
        phraseCommitCount++;
        saveButton.setDisable(true);
    }

    private void handlePhraseListCancel(ListView.EditEvent<Phrase> event) {
        enableSaveButton();
    }

    private void handlePhraseListCommit(ListView.EditEvent<Phrase> event) {
        ObservableList<Phrase> list = event.getSource().getItems();
        int lastIndex = list.size() - 1;
        if (event.getNewValue().getBody().isEmpty()) {
            if (event.getIndex() != lastIndex) {
                // Removing of an empty phrase.
                list.remove(event.getIndex());
            }
        } else {
            Phrase newPhrase = event.getNewValue();
            Phrase dbPhrase = phraseService.repository()
                    .findByBody(newPhrase.getBody())
                    .orElse(newPhrase);
            list.set(event.getIndex(), dbPhrase);

            if (event.getIndex() == lastIndex) {
                // Addition of new empty phrase for other new one.
                list.add(new Phrase(""));
            }
        }

        enableSaveButton();
    }

    private void enableSaveButton() {
        phraseCommitCount--;
        if (phraseCommitCount < 1) {
            saveButton.setDisable(false);
        }
    }

    private void renderPriorityBox() {
        List<RadioButton> priorityButtons = Arrays.stream(Priority.values()).map(priority -> {
            RadioButton button = new RadioButton(priority.name());
            if (priority.getIndex() == currentExerciseDto.getPriority()) {
                button.setSelected(true);
            }
            return button;
        }).toList();
        ToggleGroup group = new ToggleGroup();
        group.getToggles().setAll(priorityButtons);
        group.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
                    currentExerciseDto.setPriority(
                            Priority.valueOf(
                                    ((Labeled) newValue).getText()
                            ).getIndex());
                }
        );

        priorityBox.getChildren().addAll(priorityButtons);
    }

    public void handleSave(ActionEvent event) {
        // Unsetting of an empty phrase for addition new one.
        currentExerciseDto.foreignPhraseProperty().removeLast();
        currentExerciseDto.nativePhraseProperty().removeLast();

        Map<SettingName, String> settings = settingService.getMap();

        Exercise exercise = new Exercise();
        currentExerciseDto.fillExercise(exercise);
        exercise.setReadingCount(Integer.parseInt(settings.get(SettingName.READING_COUNT)));
        exercise.setMemoryCount(Integer.parseInt(settings.get(SettingName.MEMORY_COUNT)));
        exercise.setCheckedAt(null);
        exercise.setUpdatedAt(System.currentTimeMillis());
        // the cure for a new exercise "detached entity passed to persist"
        if (exercise.getId() == null) {
            exerciseService.save(exercise);
        }
        currentExerciseDto.fillForeignNative(exercise);
        exerciseService.save(exercise);

        main.getScene().getWindow().hide();
    }

    @SneakyThrows
    public void handlePhrase(ActionEvent event) {
        if (! phraseStage.isShowing()) {
            ApplicationContextFXMLLoader viewLoader = new ApplicationContextFXMLLoader(
                    PhraseController.class.getResource("PhraseView.fxml"),
                    applicationContext
            );
            GridPane phraseView = viewLoader.load();

            Scene scene = new Scene(phraseView);

            phraseStage.setScene(scene);
            phraseStage.setWidth(main.getScene().getWindow().getWidth());
            phraseStage.setHeight(main.getScene().getWindow().getHeight());
            phraseStage.setX(
                    main.getScene().getWindow().getX() +
                    main.getScene().getWindow().getWidth() +
                    Constants.WINDOW_OFFSET
            );
            phraseStage.setY(main.getScene().getWindow().getY());
            phraseStage.setTitle("Phrase");
            phraseStage.showAndWait();
        }
    }
}
