package ua.pp.disik.englishroulette.desktop.fx.entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.Phrase;
import ua.pp.disik.englishroulette.desktop.entity.Priority;

public class ExerciseWriteDto {
    private Integer id;

    @Getter
    @Setter
    private int priority;

    private ObservableList<Phrase> foreignPhraseProperty = FXCollections.observableArrayList();
    private ObservableList<Phrase> nativePhraseProperty = FXCollections.observableArrayList();

    public ExerciseWriteDto() {
        priority = Priority.HIGH.getIndex();
    }

    public ExerciseWriteDto(Exercise exercise) {
        id = exercise.getId();
        priority = exercise.getPriority();
        foreignPhraseProperty.setAll(exercise.getForeignPhrases());
        nativePhraseProperty.setAll(exercise.getNativePhrases());
    }
    
    public ObservableList<Phrase> foreignPhraseProperty() {
        return foreignPhraseProperty;
    }
    
    public ObservableList<Phrase> nativePhraseProperty() {
        return nativePhraseProperty;
    }

    public Exercise getExercise() {
        Exercise exercise = new Exercise();
        exercise.setId(id);
        exercise.setPriority(priority);
        exercise.setForeignPhrases(foreignPhraseProperty);
        exercise.setNativePhrases(nativePhraseProperty);
        return exercise;
    }
}
