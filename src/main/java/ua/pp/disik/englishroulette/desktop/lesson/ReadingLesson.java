package ua.pp.disik.englishroulette.desktop.lesson;

import ua.pp.disik.englishroulette.desktop.entity.ExerciseDto;
import ua.pp.disik.englishroulette.desktop.entity.SettingName;
import ua.pp.disik.englishroulette.desktop.service.ExerciseService;
import ua.pp.disik.englishroulette.desktop.service.SettingService;

import java.util.Collections;
import java.util.List;

public class ReadingLesson implements Lesson {
    private final ExerciseService exerciseService;
    private final SettingService settingService;

    private List<ExerciseDto> exercises;
    private ExerciseDto current;
    private int successNumber = 0;
    private int allNumber;

    public ReadingLesson(
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
    public int getAmmount() {
        return exercises.size();
    }

    @Override
    public int getCurrentCount() {
        return current.getReadingCount();
    }

    @Override
    public Side getCurrentAvers() {
        return new Side(
                current.getForeignPhrases().stream()
                        .map(phrase -> phrase.getBody().trim())
                        .toList(),
                true
        );
    }

    @Override
    public Side getCurrentRevers() {
        return new Side(
                current.getNativePhrases().stream()
                        .map(phrase -> phrase.getBody().trim())
                        .toList(),
                false
        );
    }

    @Override
    public void rememberCurrent() {
        if (current != null) {
            successNumber++;

            int readingCount = current.getReadingCount() - 1;
            current.setReadingCount(readingCount);
            exerciseService.save(current);
        }
    }

    @Override
    public void dontRememberCurrent() {}

    public void next() {
        if (exercises.size() > 0) {
            exercises.removeFirst();
            if (exercises.size() > 0) {
                current = exercises.getFirst();
            } else {
                current = null;
            }
        }
    }

    @Override
    public int getSuccessNumber() {
        return successNumber;
    }

    @Override
    public int getAllNumber() {
        return allNumber;
    }
}
