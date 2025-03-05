package ua.pp.disik.englishroulette.desktop.init;

import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.Phrase;
import ua.pp.disik.englishroulette.desktop.entity.Priority;
import ua.pp.disik.englishroulette.desktop.entity.SettingName;
import ua.pp.disik.englishroulette.desktop.service.ExerciseService;
import ua.pp.disik.englishroulette.desktop.service.PhraseService;
import ua.pp.disik.englishroulette.desktop.service.SettingService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

//@Component
public class SeedCreator extends Creator {
    private final PhraseService phraseService;
    private final ExerciseService exerciseService;
    private final SettingService settingService;

    public SeedCreator(
            PhraseService phraseService,
            ExerciseService exerciseService,
            SettingService settingService
    ) {
        this.phraseService = phraseService;
        this.exerciseService = exerciseService;
        this.settingService = settingService;
    }

    @Override
    public void create() {
        Phrase phrase1 = new Phrase("instead");
        Phrase phrase2 = new Phrase("вместо");
        phraseService.repository().saveAll(List.of(phrase1, phrase2));

        Map<SettingName, String> settings = settingService.getMap();

        Exercise exercise1 = new Exercise(
                Integer.parseInt(settings.get(SettingName.READING_COUNT)),
                Integer.parseInt(settings.get(SettingName.MEMORY_COUNT)),
                Priority.MIDDLE.getIndex()
        );
        exercise1.setForeignPhrases(List.of(
                new Phrase("match"),
                new Phrase("feet"),
                new Phrase("suit")
        ));
        exercise1.setNativePhrases(List.of(
                new Phrase("совпадать"),
                new Phrase("подходить по размеру"),
                new Phrase("быть к лицу")
        ));
        exerciseService.repository().save(exercise1);

        Exercise exercise2 = new Exercise(
                0,
                0,
                Priority.LOW.getIndex()
        );
        exercise2.setCheckedAt(Instant.now().minus(5, ChronoUnit.DAYS).toEpochMilli());
        exercise2.setForeignPhrases(List.of(
                new Phrase("I'm fed up.")
        ));
        exercise2.setNativePhrases(List.of(
                new Phrase("Мне это надоело.")
        ));
        exerciseService.repository().save(exercise2);

        Exercise exercise3 = new Exercise(
                Integer.parseInt(settings.get(SettingName.READING_COUNT)),
                Integer.parseInt(settings.get(SettingName.MEMORY_COUNT)),
                Priority.HIGH.getIndex()
        );
        exercise3.setForeignPhrases(List.of(
                new Phrase("to drop out")
        ));
        exercise3.setNativePhrases(List.of(
                new Phrase("отсеятся"),
                new Phrase("вылететь"),
                new Phrase("выпасть")
        ));
        exerciseService.repository().save(exercise3);
    }
}
