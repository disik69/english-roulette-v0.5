package ua.pp.disik.englishroulette.desktop.lesson.exercise;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExerciseCard {
    private ExerciseSide avers;
    private ExerciseSide revers;
    private int count;
}
