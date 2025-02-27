package ua.pp.disik.englishroulette.desktop.fx;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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

        renderForeignList();

        renderNativeList();

        renderPriority();
    }

    private void renderForeignList() {
        foreignList.setEditable(true);
        foreignList.setCellFactory(TextFieldListCell.forListView(phraseStringConverter()));
        foreignList.setItems(currentExerciseDto.foreignPhraseProperty());

        currentExerciseDto.foreignPhraseProperty().add(new Phrase(""));
        foreignList.setOnEditCommit(this::handlePhraseListCommit);
    }

    private void renderNativeList() {
        nativeList.setEditable(true);
        nativeList.setCellFactory(TextFieldListCell.forListView(phraseStringConverter()));
        nativeList.setItems(currentExerciseDto.nativePhraseProperty());

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
                list.remove(event.getIndex());
            }
        } else {
            list.set(event.getIndex(), event.getNewValue());
            if (event.getIndex() == lastIndex) {
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

    }
}
