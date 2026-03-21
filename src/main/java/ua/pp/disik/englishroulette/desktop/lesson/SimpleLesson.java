package ua.pp.disik.englishroulette.desktop.lesson;

import ua.pp.disik.englishroulette.desktop.lesson.exercise.ExerciseSet;
import ua.pp.disik.englishroulette.desktop.lesson.exercise.ExerciseSide;

public class SimpleLesson implements Lesson {
    private final ExerciseSet exerciseSet;

    private int successNumber = 0;

    public SimpleLesson(
            ExerciseSet exerciseSet
    ) {
        this.exerciseSet = exerciseSet;
    }

    @Override
    public int getAmount() {
        return exerciseSet.getAmount();
    }

    @Override
    public int getCurrentCount() {
        return exerciseSet.getCurrent().getCount();
    }

    @Override
    public ExerciseSide getCurrentAvers() {
        return exerciseSet.getCurrent().getAvers();
    }

    @Override
    public ExerciseSide getCurrentRevers() {
        return exerciseSet.getCurrent().getRevers();
    }

    @Override
    public void rememberCurrent() {
        if (exerciseSet.getCurrent() != null) {
            successNumber++;
            exerciseSet.rememberCurrent();
        }
    }

    @Override
    public void dontRememberCurrent() {
        if (exerciseSet.getCurrent() != null) {
            exerciseSet.dontRememberCurrent();
        }
    }

    public void next() {
        exerciseSet.next();
    }

    @Override
    public int getSuccessNumber() {
        return successNumber;
    }

    @Override
    public int getAllNumber() {
        return exerciseSet.getAllNumber();
    }

    public boolean rewind() {
        return false;
    }
}
