package ua.pp.disik.englishroulette.desktop.fx;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
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
public class ExercisePresenter {
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
    private VBox priority;

    @FXML
    private void initialize() {
        if (currentExercise.getId() == null) {
            currentExerciseDto = new ExerciseWriteDto();
        } else {
            currentExerciseDto = exerciseService.findById(currentExercise.getId());
        }

        renderForeignList();

        renderNativeList();

        renderPriority();
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

    private void renderPriority() {
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

        priority.getChildren().addAll(priorityButtons);
    }

    public void handleSave(ActionEvent event) {
        // Unsetting of an empty phrase for addition new one.
        currentExerciseDto.foreignPhraseProperty().removeLast();
        currentExerciseDto.nativePhraseProperty().removeLast();

        Map<SettingName, String> settings = settingService.getMap();

        Exercise exercise = currentExerciseDto.getExercise();
        exercise.setReadingCount(Integer.parseInt(settings.get(SettingName.READING_COUNT)));
        exercise.setMemoryCount(Integer.parseInt(settings.get(SettingName.MEMORY_COUNT)));
        exercise.setUpdatedAt(System.currentTimeMillis());
        exerciseService.save(exercise);

        main.getScene().getWindow().hide();
    }
}
