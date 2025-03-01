package ua.pp.disik.englishroulette.desktop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Priority {
    HIGH(1), MIDDLE(2), LOW(3);

    private int index;

    public static Priority getByIndex(int index) {
        switch (index) {
            case 1:
                return HIGH;
            case 2:
                return MIDDLE;
            case 3:
                return LOW;
            default:
                throw new RuntimeException("It's wrong index of Priority.");
        }
    }
}
