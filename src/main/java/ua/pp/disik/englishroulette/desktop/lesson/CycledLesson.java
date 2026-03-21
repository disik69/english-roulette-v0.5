package ua.pp.disik.englishroulette.desktop.lesson;

import ua.pp.disik.englishroulette.desktop.lesson.exercise.ExerciseCard;
import ua.pp.disik.englishroulette.desktop.lesson.exercise.ExerciseSet;
import ua.pp.disik.englishroulette.desktop.lesson.exercise.ExerciseSide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CycledLesson implements Lesson {
    private final ExerciseSet exerciseSet;

    private List<ExerciseCard> currentCards = new ArrayList<>();
    private List<ExerciseCard> unknownCards = new ArrayList<>();
    private ExerciseCard current;
    private int successNumber = 0;
    private int allNumber;
    private boolean rewound = false;

    public CycledLesson(
            ExerciseSet exerciseSet
    ) {
        this.exerciseSet = exerciseSet;

        current = exerciseSet.getCurrent();
        allNumber = exerciseSet.getAllNumber();
    }

    @Override
    public int getAmount() {
        if (! rewound) {
            return exerciseSet.getAmount();
        } else {
            return currentCards.size();
        }
    }

    @Override
    public int getCurrentCount() {
        return current.getCount();
    }

    @Override
    public ExerciseSide getCurrentAvers() {
        return current.getAvers();
    }

    @Override
    public ExerciseSide getCurrentRevers() {
        return current.getRevers();
    }

    @Override
    public void rememberCurrent() {
        if (current != null) {
            successNumber++;

            if (! rewound) {
                exerciseSet.rememberCurrent();
            } else {
                currentCards.remove(current);
            }
        }
    }

    @Override
    public void dontRememberCurrent() {
        if (current != null) {
            unknownCards.add(current);

            if (! rewound) {
                exerciseSet.dontRememberCurrent();
            } else {
                currentCards.remove(current);
            }
        }
    }

    public void next() {
        if (! rewound) {
            exerciseSet.next();
            current = exerciseSet.getCurrent();
        } else {
            if (currentCards.size() > 0) {
                current = currentCards.getFirst();
            } else {
                current = null;
            }
        }
    }

    @Override
    public int getSuccessNumber() {
        return successNumber;
    }

    @Override
    public int getAllNumber() {
        return allNumber;
    }

    @Override
    public boolean rewind() {
        if (! rewound) {
            rewound = true;
        }
        if (unknownCards.size() > 0) {
            currentCards = new ArrayList<>(unknownCards);
            unknownCards.clear();

            Collections.shuffle(currentCards);

            current = currentCards.getFirst();
            successNumber = 0;
            allNumber = currentCards.size();

            return true;
        } else {
            return false;
        }
    }
}
