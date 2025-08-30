package ua.pp.disik.englishroulette.desktop.lesson;

import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.ExerciseDto;
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

    private List<ExerciseDto> exercises;
    private ExerciseDto current;
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
    public Side getCurrentAvers() {
        return new Side(
                current.getNativePhrases().stream()
                        .map(phrase -> phrase.getBody().trim())
                        .toList(),
                false
        );
    }

    @Override
    public Side getCurrentRevers() {
        return new Side(
                current.getForeignPhrases().stream()
                        .map(phrase -> phrase.getBody().trim())
                        .toList(),
                true
        );
    }

    @Override
    public void rememberCurrent() {
        if (current != null) {
            successNumber++;

            Map<SettingName, String> settings = settingService.getMap();

            long checkedAt = Instant.now().truncatedTo(ChronoUnit.DAYS).plus(
                    Long.parseLong(settings.get(SettingName.REPEAT_TERM)),
                    ChronoUnit.DAYS
            ).toEpochMilli();
            current.setCheckedAt(checkedAt);
            current.setUpdatedAt(System.currentTimeMillis());
            exerciseService.save(current);
        }
    }

    @Override
    public void dontRememberCurrent() {
        if (current != null) {
            Map<SettingName, String> settings = settingService.getMap();

            current.setReadingCount(Integer.parseInt(settings.get(SettingName.READING_COUNT)));
            current.setMemoryCount(Integer.parseInt(settings.get(SettingName.MEMORY_COUNT)));
            current.setCheckedAt(null);
            current.setUpdatedAt(System.currentTimeMillis());
            exerciseService.save(current);
        }
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
