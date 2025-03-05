package ua.pp.disik.englishroulette.desktop.service;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.SettingName;
import ua.pp.disik.englishroulette.desktop.fx.entity.ExerciseReadDto;
import ua.pp.disik.englishroulette.desktop.fx.entity.ExerciseWriteDto;
import ua.pp.disik.englishroulette.desktop.repository.ExerciseRepository;

import java.util.List;

@Service
public class ExerciseService implements RepositoryService<ExerciseRepository> {
    private final ExerciseRepository exerciseRepository;
    private final SettingService settingService;

    public ExerciseService(
            ExerciseRepository exerciseRepository,
            SettingService settingService
    ) {
        this.exerciseRepository = exerciseRepository;
        this.settingService = settingService;
    }

    @Override
    public ExerciseRepository repository() {
        return exerciseRepository;
    }

    public List<ExerciseReadDto> findAll(int page, int size) {
        return exerciseRepository.findByOrderByUpdatedAtDesc(
                        PageRequest.of(page, size)
                ).stream()
                .map(exercise -> new ExerciseReadDto(exercise))
                .toList();
    }

    public List<ExerciseReadDto> findAllByFilter(String filter, int page, int size) {
        return exerciseRepository.findByForeignPhrases_BodyContainingOrNativePhrases_BodyContainingOrderByUpdatedAtDesc(
                        filter,
                        filter,
                        PageRequest.of(page, size)
                ).stream()
                .map(exercise -> new ExerciseReadDto(exercise))
                .toList();
    }

    public ExerciseWriteDto findById(Integer id) {
        Exercise exercise = exerciseRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise doesn't exist."));
        return new ExerciseWriteDto(exercise);
    }

    public List<Exercise> getReading() {
        return exerciseRepository.findByReadingCountNotOrderByPriorityAscReadingCountAscUpdatedAtAsc(
                0,
                Limit.of(Integer.parseInt(settingService.getMap().get(SettingName.LESSON_SIZE)))
        );
    }

    public List<Exercise> getMemory() {
        return exerciseRepository.findByReadingCountAndMemoryCountNotOrderByPriorityAscMemoryCountAscUpdatedAtAsc(
                0,
                0,
                Limit.of(Integer.parseInt(settingService.getMap().get(SettingName.LESSON_SIZE)))
        );
    }

    public List<Exercise> getRepeating() {
        return exerciseRepository.findByReadingCountAndMemoryCountAndCheckedAtLessThanEqualOrderByPriorityAscCheckedAtAsc(
                0,
                0,
                System.currentTimeMillis(),
                Limit.of(Integer.parseInt(settingService.getMap().get(SettingName.LESSON_SIZE)))
        );
    }

    public Exercise save(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }
}
