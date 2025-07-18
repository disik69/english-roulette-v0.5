package ua.pp.disik.englishroulette.desktop.lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface Lesson {
    @AllArgsConstructor
    @Getter
    class Side {
        private String text;
        private boolean spoken;
    }

    int getAmmount();
    int getCurrentCount();
    Side getCurrentAvers();
    Side getCurrentRevers();
    void rememberCurrentAndNext();
    void dontRememberCurrentAndNext();
    int getSuccessNumber();
    int getAllNumber();
}
