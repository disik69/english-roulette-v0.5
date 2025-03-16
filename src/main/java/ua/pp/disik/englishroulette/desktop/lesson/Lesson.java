package ua.pp.disik.englishroulette.desktop.lesson;

public interface Lesson {
    int getAmmount();
    int getCurrentCount();
    String getCurrentAvers();
    String getCurrentRevers();
    void rememberCurrentAndNext();
    void dontRememberCurrentAndNext();
    int getSuccessNumber();
    int getAllNumber();
}
