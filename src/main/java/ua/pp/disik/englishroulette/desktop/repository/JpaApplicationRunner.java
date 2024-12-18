package ua.pp.disik.englishroulette.desktop.repository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.Phrase;
import ua.pp.disik.englishroulette.desktop.entity.SettingName;
import ua.pp.disik.englishroulette.desktop.service.SettingService;

import java.util.List;
import java.util.Map;

//@Component
public class JpaApplicationRunner implements ApplicationRunner {
    private final WordRepository wordRepository;
    private final ExerciseRepository exerciseRepository;
    private final SettingService settingService;

    public JpaApplicationRunner(
            WordRepository wordRepository,
            ExerciseRepository exerciseRepository,
            SettingService settingService
    ) {
        this.wordRepository = wordRepository;
        this.exerciseRepository = exerciseRepository;
        this.settingService = settingService;
    }

    @Override
    public void run(ApplicationArguments args) {
        Phrase phrase1 = new Phrase("instead");
        Phrase phrase2 = new Phrase("вместо");
        wordRepository.saveAll(List.of(phrase1, phrase2));

        Map<SettingName, String> settings = settingService.getMap();

        Exercise exercise = new Exercise(
                Integer.parseInt(settings.get(SettingName.READING_COUNT)),
                Integer.parseInt(settings.get(SettingName.MEMORY_COUNT))
        );
        exercise.setForeignPhrases(List.of(
                new Phrase("match"),
                new Phrase("feet"),
                new Phrase("suit")
        ));
        exercise.setNativePhrases(List.of(
                new Phrase("совпадать"),
                new Phrase("подходить по размеру"),
                new Phrase("быть к лицу")
        ));
        exerciseRepository.save(exercise);
    }
}
