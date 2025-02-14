package ua.pp.disik.englishroulette.desktop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Priority {
    HIGH(1), MIDDLE(2), LOW(3);

    private int index;
}
