package ua.pp.disik.englishroulette.desktop.lesson.exercise;

import ua.pp.disik.englishroulette.desktop.entity.ExerciseDto;
import ua.pp.disik.englishroulette.desktop.entity.SettingName;
import ua.pp.disik.englishroulette.desktop.service.ExerciseService;
import ua.pp.disik.englishroulette.desktop.service.SettingService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RepeatingExerciseSet implements ExerciseSet {
    private final ExerciseService exerciseService;
    private final SettingService settingService;

    private List<ExerciseDto> exercises;
    private ExerciseDto current;
    private int allNumber;

    public RepeatingExerciseSet(
            ExerciseService exerciseService,
            SettingService settingService
    ) {
        this.exerciseService = exerciseService;
        this.settingService = settingService;

        exercises = exerciseService.getRepeating(
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
                            current.getNativePhrases().stream()
                                    .map(phrase -> phrase.getBody().trim())
                                    .toList(),
                            false
                    ),
                    new ExerciseSide(
                            current.getForeignPhrases().stream()
                                   .map(phrase -> phrase.getBody().trim())
                                   .toList(),
                            true
                    ),
                    0
            );
        } else {
            return null;
        }
    }

    @Override
    public void rememberCurrent() {
        if (current != null) {
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
