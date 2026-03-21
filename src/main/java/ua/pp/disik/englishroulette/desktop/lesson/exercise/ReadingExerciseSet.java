package ua.pp.disik.englishroulette.desktop.lesson.exercise;

import ua.pp.disik.englishroulette.desktop.entity.ExerciseDto;
import ua.pp.disik.englishroulette.desktop.entity.SettingName;
import ua.pp.disik.englishroulette.desktop.service.ExerciseService;
import ua.pp.disik.englishroulette.desktop.service.SettingService;

import java.util.Collections;
import java.util.List;

public class ReadingExerciseSet implements ExerciseSet {
    private final ExerciseService exerciseService;
    private final SettingService settingService;

    private List<ExerciseDto> exercises;
    private ExerciseDto current;
    private int allNumber;

    public ReadingExerciseSet(
            ExerciseService exerciseService,
            SettingService settingService
    ) {
        this.exerciseService = exerciseService;
        this.settingService = settingService;

        exercises = exerciseService.getReading(
                0,
                Integer.parseInt(settingService.getMap().get(SettingName.LESSON_SIZE))
        );
        allNumber = exercises.size();
        Collections.shuffle(exercises);
        if (exercises.size() > 0) {
            current = exercises.getFirst();
        }
    }

    @Override
    public int getAmount() {
        return exercises.size();
    }

    @Override
    public ExerciseCard getCurrent() {
        if (current != null) {
            return new ExerciseCard(
                    new ExerciseSide(
                            current.getForeignPhrases().stream()
                                   .map(phrase -> phrase.getBody().trim())
                                   .toList(),
                            true
                    ),
                    new ExerciseSide(
                            current.getNativePhrases().stream()
                                    .map(phrase -> phrase.getBody().trim())
                                    .toList(),
                            false
                    ),
                    current.getReadingCount()
            );
        } else {
            return null;
        }
    }

    @Override
    public void rememberCurrent() {
        if (current != null) {
            int readingCount = current.getReadingCount() - 1;
            current.setReadingCount(readingCount);
            exerciseService.save(current);
        }
    }

    @Override
    public void dontRememberCurrent() {}

    public void next() {
        if (exercises.size() > 0) {
            exercises.remove(current);
            if (exercises.size() > 0) {
                current = exercises.getFirst();
            } else {
                current = null;
            }
        }
    }

    @Override
    public int getAllNumber() {
        return allNumber;
    }
}
