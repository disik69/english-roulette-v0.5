package ua.pp.disik.englishroulette.desktop.fx.entity;

import ua.pp.disik.englishroulette.desktop.entity.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ExerciseReadDto {
    private final Exercise exercise;

    public ExerciseReadDto(Exercise exercise) {
        this.exercise = exercise;
    }

    public Integer getId() {
        return exercise.getId();
    }

    public int getReadingCount() {
        return exercise.getReadingCount();
    }

    public int getMemoryCount() {
        return exercise.getMemoryCount();
    }

    public String getPriority() {
        return Priority.getByIndex(exercise.getPriority()).name();
    }

    public String getCheckedAt() {
        if (exercise.getCheckedAt() == null) {
            return "";
        } else {
            return DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd"
            ).withZone(
                    ZoneId.systemDefault()
            ).format(
                    Instant.ofEpochMilli(exercise.getCheckedAt())
            );
        }
    }

    public String getForeignPhrases() {
        return exercise.getForeignPhrases().stream()
                .map(phrase -> phrase.getBody())
                .reduce((first, second) -> first + "\n" + second)
                .orElse("");
    }

    public String getNativePhrases() {
        return exercise.getNativePhrases().stream()
                .map(phrase -> phrase.getBody())
                .reduce((first, second) -> first + "\n" + second)
                .orElse("");
    }
}
