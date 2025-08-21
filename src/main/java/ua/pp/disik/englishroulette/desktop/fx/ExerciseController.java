package ua.pp.disik.englishroulette.desktop.fx;

import io.micrometer.common.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
import ua.pp.disik.englishroulette.desktop.fx.entity.CurrentPhrase;
import ua.pp.disik.englishroulette.desktop.fx.entity.ExerciseReadDto;
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
    private static final int PAGE_SIZE = 30;
    private static final int MIN_FILTER_LENGTH = 3;

    private ExerciseWriteDto currentExerciseDto;
    private Stage phraseStage;

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

    @Autowired
    private CurrentPhrase currentPhrase;

    @FXML
    private GridPane main;

    @FXML
    private TableView<ExerciseReadDto> exerciseTable;

    @FXML
    private TableColumn<ExerciseReadDto, String> exerciseTableColumnForeign;

    @FXML
    private TableColumn<ExerciseReadDto, String> exerciseTableColumnNative;

    @FXML
    private VBox priorityBox;

    @FXML
    private void initialize() {
        if (currentExercise.getId() == null) {
            currentExerciseDto = new ExerciseWriteDto();

            // addition of initial phrases for new exercise
            currentExerciseDto.getForeignPhrases().add(new Phrase(""));
            currentExerciseDto.getNativePhrases().add(new Phrase(""));
        } else {
            currentExerciseDto = exerciseService.findById(currentExercise.getId());
        }

        PhraseVBox foreignVBox = new PhraseVBox(
                currentExerciseDto.getForeignPhrases(),
                this::convertPhrase,
                this::handlePhraseUpdate,
                this::handlePhraseFocus,
                this::handlePhraseUnfocus
        );
        main.add(foreignVBox, 0, 1);

        PhraseVBox nativeVBox = new PhraseVBox(
                currentExerciseDto.getNativePhrases(),
                this::convertPhrase,
                this::handlePhraseUpdate,
                this::handlePhraseFocus,
                this::handlePhraseUnfocus
        );
        main.add(nativeVBox, 1, 1);

        exerciseTableColumnForeign.setCellValueFactory(new PropertyValueFactory<>("foreignPhrases"));
        exerciseTableColumnNative.setCellValueFactory(new PropertyValueFactory<>("nativePhrases"));

        renderPriorityBox();

        phraseStage = new Stage();
        phraseStage.setOnShown(phraseWindowEvent -> {
            main.getScene().getWindow().setOnHiding(mainWindowEvent -> phraseStage.hide());
        });
    }

    private Phrase convertPhrase(String body) {
            return phraseService.repository()
                    .findByBody(body)
                    .orElse(new Phrase(body));
    }

    private void handlePhraseUpdate(String body) {
        if (body.length() >= MIN_FILTER_LENGTH) {
            List<ExerciseReadDto> exerciseReadDtos = exerciseService.findAllByFilter(body, 0, PAGE_SIZE);
            exerciseTable.setItems(FXCollections.observableArrayList(exerciseReadDtos));
        }
    }

    private void handlePhraseFocus(String body) {
        currentPhrase.setBody(body);
    }

    private void handlePhraseUnfocus() {
        exerciseTable.setItems(FXCollections.emptyObservableList());
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
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            currentExerciseDto.setPriority(
                    Priority.valueOf(
                            ((Labeled) newValue).getText()
                    ).getIndex()
            );
        });

        priorityBox.getChildren().addAll(priorityButtons);
    }

    public void handleSave(ActionEvent event) {
        if (
                findNotBlankPhrase(currentExerciseDto.getForeignPhrases()) &&
                findNotBlankPhrase(currentExerciseDto.getNativePhrases())
        ) {
            currentExerciseDto.getForeignPhrases().removeIf(phrase -> StringUtils.isBlank(phrase.getBody()));
            currentExerciseDto.getNativePhrases().removeIf(phrase -> StringUtils.isBlank(phrase.getBody()));

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
        } else {
            MessageStage error = new MessageStage("Error", "You have unfilled side(s).", main);
            error.showAndWait();
        }
    }

    private boolean findNotBlankPhrase(List<Phrase> phrases) {
        for (Phrase phrase : phrases) {
            if (StringUtils.isNotBlank(phrase.getBody())) {
                return true;
            }
        }
        return false;
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
