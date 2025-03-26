package ua.pp.disik.englishroulette.desktop.lesson;

import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.SettingName;
import ua.pp.disik.englishroulette.desktop.service.ExerciseService;
import ua.pp.disik.englishroulette.desktop.service.SettingService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MemoryLesson implements Lesson {
    private final ExerciseService exerciseService;
    private final SettingService settingService;

    private List<Exercise> exercises;
    private Exercise current;
    private int successNumber = 0;
    private int allNumber;

    public MemoryLesson(
            ExerciseService exerciseService,
            SettingService settingService
    ) {
        this.exerciseService = exerciseService;
        this.settingService = settingService;

        exercises = exerciseService.getMemory();
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
        return current.getMemoryCount();
    }

    @Override
    public Side getCurrentAvers() {
        return new Side(
                current.getNativePhrases().stream()
                        .map(phrase -> phrase.getBody())
                        .reduce((first, second) -> first + ",\n" + second)
                        .orElse(""),
                false
        );
    }

    @Override
    public Side getCurrentRevers() {
        return new Side(
                current.getForeignPhrases().stream()
                        .map(phrase -> phrase.getBody())
                        .reduce((first, second) -> first + ",\n" + second)
                        .orElse(""),
                true
        );
    }

    @Override
    public void rememberCurrentAndNext() {
        if (current != null) {
            successNumber++;

            int memoryCount = current.getMemoryCount() - 1;
            current.setMemoryCount(memoryCount);
            if (memoryCount < 1) {
                Map<SettingName, String> settings = settingService.getMap();

                long checkedAt = Instant.now().truncatedTo(ChronoUnit.DAYS).plus(
                        Long.parseLong(settings.get(SettingName.REPEAT_TERM)),
                        ChronoUnit.DAYS
                ).toEpochMilli();
                current.setCheckedAt(checkedAt);
                current.setUpdatedAt(System.currentTimeMillis());
            }
            exerciseService.save(current);
        }

        next();
    }

    @Override
    public void dontRememberCurrentAndNext() {
        next();
    }

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
