package ua.pp.disik.englishroulette.desktop.fx.entity;

import ua.pp.disik.englishroulette.desktop.entity.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ExerciseTableItem {
    private final ExerciseDto exerciseDto;

    public ExerciseTableItem(ExerciseDto exerciseDto) {
        this.exerciseDto = exerciseDto;
    }

    public Integer getId() {
        return exerciseDto.getId();
    }

    public int getReadingCount() {
        return exerciseDto.getReadingCount();
    }

    public int getMemoryCount() {
        return exerciseDto.getMemoryCount();
    }

    public String getPriority() {
        return Priority.getByIndex(exerciseDto.getPriority()).name();
    }

    public String getCheckedAt() {
        if (exerciseDto.getCheckedAt() == null) {
            return "";
        } else {
            return DateTimeFormatter.ofPattern(
                    "dd-MM-yyyy"
            ).withZone(
                    ZoneId.systemDefault()
            ).format(
                    Instant.ofEpochMilli(exerciseDto.getCheckedAt())
            );
        }
    }

    public String getForeignPhrases() {
        return exerciseDto.getForeignPhrases().stream()
                .map(phrase -> phrase.getBody())
                .reduce((first, second) -> first + "\n" + second)
                .orElse("");
    }

    public String getNativePhrases() {
        return exerciseDto.getNativePhrases().stream()
                .map(phrase -> phrase.getBody())
                .reduce((first, second) -> first + "\n" + second)
                .orElse("");
    }
}
