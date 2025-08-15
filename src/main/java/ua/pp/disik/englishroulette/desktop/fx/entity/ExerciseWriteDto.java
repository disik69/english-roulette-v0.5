package ua.pp.disik.englishroulette.desktop.fx.entity;

import lombok.Getter;
import lombok.Setter;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.Phrase;
import ua.pp.disik.englishroulette.desktop.entity.Priority;

import java.util.ArrayList;
import java.util.List;

public class ExerciseWriteDto {
    private Integer id;

    @Getter
    @Setter
    private int priority;

    private Long checkedAt;

    @Getter
    private List<Phrase> foreignPhrases = new ArrayList<>();

    @Getter
    private List<Phrase> nativePhrases = new ArrayList<>();

    public ExerciseWriteDto() {
        priority = Priority.MIDDLE.getIndex();
    }

    public ExerciseWriteDto(Exercise exercise) {
        id = exercise.getId();
        priority = exercise.getPriority();
        checkedAt = exercise.getCheckedAt();
        foreignPhrases.addAll(exercise.getForeignPhrases());
        nativePhrases.addAll(exercise.getNativePhrases());
    }

    public void fillExercise(Exercise exercise) {
        exercise.setId(id);
        exercise.setPriority(priority);
        exercise.setCheckedAt(checkedAt);
    }

    public void fillForeignNative(Exercise exercise) {
        exercise.setForeignPhrases(foreignPhrases);
        exercise.setNativePhrases(nativePhrases);
    }
}
