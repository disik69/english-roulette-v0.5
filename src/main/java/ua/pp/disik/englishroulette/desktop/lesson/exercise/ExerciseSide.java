package ua.pp.disik.englishroulette.desktop.lesson.exercise;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ExerciseSide {
    private List<String> phrases;
    private boolean spoken;
}
