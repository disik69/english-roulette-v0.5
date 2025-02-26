package ua.pp.disik.englishroulette.desktop.fx;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.entity.Phrase;
import ua.pp.disik.englishroulette.desktop.entity.Priority;
import ua.pp.disik.englishroulette.desktop.fx.entity.CurrentExercise;
import ua.pp.disik.englishroulette.desktop.fx.entity.ExerciseWriteDto;
import ua.pp.disik.englishroulette.desktop.service.ExerciseService;

import java.util.Arrays;
import java.util.List;

@Component
public class ExercisePresenter {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private CurrentExercise currentExercise;

    private ExerciseWriteDto currentExerciseDto;

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

        foreignList.setEditable(true);
        foreignList.setCellFactory(TextFieldListCell.forListView(listStringConverter()));
        foreignList.setItems(currentExerciseDto.foreignPhraseProperty());

        nativeList.setEditable(true);
        nativeList.setItems(currentExerciseDto.nativePhraseProperty());
        nativeList.setCellFactory(TextFieldListCell.forListView(listStringConverter()));

        renderPriority();
    }

    private StringConverter<Phrase> listStringConverter() {
        return new StringConverter<Phrase>() {
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

    }
}
