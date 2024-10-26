package ua.pp.disik.englishroulette.desktop.repository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.Word;

import java.util.List;

@Component
public class JpaApplicationRunner implements ApplicationRunner {
    private final WordRepository wordRepository;
    private final ExerciseRepository exerciseRepository;

    public JpaApplicationRunner(
            WordRepository wordRepository,
            ExerciseRepository exerciseRepository
    ) {
        this.wordRepository = wordRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Word word1 = new Word("instead");
        Word word2 = new Word("вместо");
        wordRepository.saveAll(List.of(word1, word2));

        Exercise exercise = new Exercise();
        exercise.setForeignWords(List.of(
                new Word("match"),
                new Word("feet"),
                new Word("suit")
        ));
        exercise.setNativeWords(List.of(
                new Word("совпадать"),
                new Word("подходить по размеру"),
                new Word("быть к лицу")
        ));
        exerciseRepository.save(exercise);
    }
}
