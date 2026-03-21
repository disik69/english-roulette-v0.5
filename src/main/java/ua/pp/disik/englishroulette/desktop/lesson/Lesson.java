package ua.pp.disik.englishroulette.desktop.lesson;

import ua.pp.disik.englishroulette.desktop.lesson.exercise.ExerciseSide;

public interface Lesson {
    int getAmount();
    int getCurrentCount();
    ExerciseSide getCurrentAvers();
    ExerciseSide getCurrentRevers();
    void rememberCurrent();
    void dontRememberCurrent();
    void next();
    int getSuccessNumber();
    int getAllNumber();
    boolean rewind();
}