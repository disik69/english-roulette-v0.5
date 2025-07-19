package ua.pp.disik.englishroulette.desktop.lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public interface Lesson {
    @AllArgsConstructor
    @Getter
    class Side {
        private List<String> phrases;
        private boolean spoken;
    }

    int getAmmount();
    int getCurrentCount();
    Side getCurrentAvers();
    Side getCurrentRevers();
    void rememberCurrent();
    void dontRememberCurrent();
    void next();
    int getSuccessNumber();
    int getAllNumber();
}
