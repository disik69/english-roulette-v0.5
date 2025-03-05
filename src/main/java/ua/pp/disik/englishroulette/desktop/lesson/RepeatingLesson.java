package ua.pp.disik.englishroulette.desktop.lesson;

import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.SettingName;
import ua.pp.disik.englishroulette.desktop.service.ExerciseService;
import ua.pp.disik.englishroulette.desktop.service.SettingService;

import java.time.temporal.ChronoUnit;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RepeatingLesson implements Lesson {
    private final ExerciseService exerciseService;
    private final SettingService settingService;

    private List<Exercise> exercises;
    private Exercise current;
    private int successNumber = 0;
    private int allNumber;

    public RepeatingLesson(
            ExerciseService exerciseService,
            SettingService settingService
    ) {
        this.exerciseService = exerciseService;
        this.settingService = settingService;

        exercises = exerciseService.getRepeating();
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
        return 0;
    }

    @Override
    public String getCurrentAvers() {
        return current.getNativePhrases().stream()
                .map(phrase -> phrase.getBody())
                .reduce((first, second) -> first + "\n" + second)
                .orElse("");
    }

    @Override
    public String getCurrentRevers() {
        return current.getForeignPhrases().stream()
                .map(phrase -> phrase.getBody())
                .reduce((first, second) -> first + "\n" + second)
                .orElse("");
    }

    @Override
    public void rememberCurrentAndNext() {
        if (current != null) {
            successNumber++;

            Map<SettingName, String> settings = settingService.getMap();

            long checkedAt = Instant.now().plus(
                    Long.parseLong(settings.get(SettingName.REPEAT_TERM)),
                    ChronoUnit.DAYS
            ).toEpochMilli();
            current.setCheckedAt(checkedAt);
            current.setUpdatedAt(System.currentTimeMillis());
            exerciseService.save(current);
        }

        next();
    }

    @Override
    public void dontRememberCurrentAndNext() {
        if (current != null) {
            Map<SettingName, String> settings = settingService.getMap();

            current.setReadingCount(Integer.parseInt(settings.get(SettingName.READING_COUNT)));
            current.setMemoryCount(Integer.parseInt(settings.get(SettingName.MEMORY_COUNT)));
            current.setCheckedAt(null);
            current.setUpdatedAt(System.currentTimeMillis());
            exerciseService.save(current);
        }

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
