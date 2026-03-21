package ua.pp.disik.englishroulette.desktop.lesson.exercise;

public interface ExerciseSet {
    int getAmount();
    ExerciseCard getCurrent();
    void rememberCurrent();
    void dontRememberCurrent();
    void next();
    int getAllNumber();
}
