package ua.pp.disik.englishroulette.desktop.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExerciseDto {
    private Integer id;
    private int readingCount;
    private int memoryCount;
    private int priority;
    private Long checkedAt;
    private long updatedAt;
    private List<Phrase> foreignPhrases = new ArrayList<>();
    private List<Phrase> nativePhrases = new ArrayList<>();

    public ExerciseDto() {
        priority = Priority.MIDDLE.getIndex();
        updatedAt = System.currentTimeMillis();
    }

    public ExerciseDto(Exercise exercise) {
        id = exercise.getId();
        readingCount = exercise.getReadingCount();
        memoryCount = exercise.getMemoryCount();
        priority = exercise.getPriority();
        checkedAt = exercise.getCheckedAt();
        updatedAt = exercise.getUpdatedAt();
        foreignPhrases.addAll(exercise.getForeignPhrases());
        nativePhrases.addAll(exercise.getNativePhrases());
    }
}
