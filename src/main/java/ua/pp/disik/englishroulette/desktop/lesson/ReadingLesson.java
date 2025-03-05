package ua.pp.disik.englishroulette.desktop.lesson;

import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.service.ExerciseService;

import java.util.Collections;
import java.util.List;

public class ReadingLesson implements Lesson {
    private final ExerciseService exerciseService;

    private List<Exercise> exercises;
    private Exercise current;
    private int successNumber = 0;
    private int allNumber;

    public ReadingLesson(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;

        exercises = exerciseService.getReading();
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
    public String getCurrentAvers() {
        return current.getForeignPhrases().stream()
                .map(phrase -> phrase.getBody())
                .reduce((first, second) -> first + ",\n" + second)
                .orElse("");
    }

    @Override
    public String getCurrentRevers() {
        return current.getNativePhrases().stream()
                .map(phrase -> phrase.getBody())
                .reduce((first, second) -> first + ",\n" + second)
                .orElse("");
    }

    @Override
    public void rememberCurrentAndNext() {
        if (current != null) {
            successNumber++;

            int readingCount = current.getReadingCount();
            current.setReadingCount(readingCount - 1);
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
