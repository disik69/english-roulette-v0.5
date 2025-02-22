package ua.pp.disik.englishroulette.desktop.fx.entity;

import javafx.beans.property.*;
import lombok.Getter;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.Phrase;

import java.util.List;

@Getter
public class ExerciseWriteDto {
    private IntegerProperty readingCountProperty = new SimpleIntegerProperty();
    private IntegerProperty memoryCountProperty = new SimpleIntegerProperty();
    private IntegerProperty priorityProperty = new SimpleIntegerProperty();
    private LongProperty checkedAtProperty = new SimpleLongProperty();
    private ObjectProperty<List<Phrase>> foreignPhrasesProperty = new SimpleObjectProperty<>();
    private ObjectProperty<List<Phrase>> nativePhrasesProperty = new SimpleObjectProperty<>();

    public ExerciseWriteDto(Exercise exercise) {
        readingCountProperty.set(exercise.getReadingCount());
        memoryCountProperty.set(exercise.getMemoryCount());
        priorityProperty.set(exercise.getPriority());
        checkedAtProperty.set(exercise.getCheckedAt());
        foreignPhrasesProperty.set(exercise.getForeignPhrases());
        nativePhrasesProperty.set(exercise.getNativePhrases());
    }
}
