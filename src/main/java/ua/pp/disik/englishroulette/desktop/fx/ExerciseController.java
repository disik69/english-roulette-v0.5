package ua.pp.disik.englishroulette.desktop.fx;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import org.springframework.context.ApplicationContext;
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
@Slf4j
public class ExerciseController {
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

    private ExerciseWriteDto currentExerciseDto;

    @FXML
    private GridPane main;

    @FXML
    private ListView<Phrase> foreignList;

    @FXML
    private ListView<Phrase> nativeList;

    @FXML
    private VBox priorityBox;

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
    }

    private void renderForeignList() {
        foreignList.setEditable(true);
        foreignList.setCellFactory(TextFieldListCell.forListView(phraseStringConverter()));
        foreignList.setItems(currentExerciseDto.foreignPhraseProperty());

        // Setting of an empty phrase for addition new one.
        currentExerciseDto.foreignPhraseProperty().add(new Phrase(""));

        foreignList.setOnEditCommit(this::handlePhraseListCommit);
    }

    private void renderNativeList() {
        nativeList.setEditable(true);
        nativeList.setCellFactory(TextFieldListCell.forListView(phraseStringConverter()));
        nativeList.setItems(currentExerciseDto.nativePhraseProperty());

        // Setting of an empty phrase for addition new one.
        currentExerciseDto.nativePhraseProperty().add(new Phrase(""));

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

    @SneakyThrows
    public void handlePhrase(ActionEvent event) {
        FXMLLoader viewLoader = new FXMLLoader(
                PhraseController.class.getResource("PhraseView.fxml")
        );
        viewLoader.setControllerFactory(clazz -> applicationContext.getBean(clazz));
        GridPane phraseView = viewLoader.load();

        Scene scene = new Scene(phraseView);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setWidth(main.getScene().getWindow().getWidth());
        stage.setHeight(main.getScene().getWindow().getHeight());
        stage.setX(
                main.getScene().getWindow().getX() +
                main.getScene().getWindow().getWidth() +
                Constants.WINDOW_OFFSET
        );
        stage.setY(main.getScene().getWindow().getY());
        stage.setTitle("Phrase");
        stage.showAndWait();
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
}
