package ua.pp.disik.englishroulette.desktop.service;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.SettingName;
import ua.pp.disik.englishroulette.desktop.entity.ExerciseDto;
import ua.pp.disik.englishroulette.desktop.repository.ExerciseRepository;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public List<ExerciseDto> findAll(int page, int size) {
        return exerciseRepository.findByOrderByUpdatedAtDesc(
                        PageRequest.of(page, size)
                ).stream()
                .map(exercise -> new ExerciseDto(exercise))
                .toList();
    }

    @Transactional
    public List<ExerciseDto> findAllByFilter(String filter, int page, int size) {
        return exerciseRepository.findByForeignPhrases_BodyContainingOrNativePhrases_BodyContainingOrderByUpdatedAtDesc(
                        filter,
                        filter,
                        PageRequest.of(page, size)
                ).stream()
                .map(exercise -> new ExerciseDto(exercise))
                .toList();
    }

    @Transactional
    public ExerciseDto findById(Integer id) {
        Exercise exercise = exerciseRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise doesn't exist."));
        return new ExerciseDto(exercise);
    }

    @Transactional
    public List<ExerciseDto> getReading() {
        return exerciseRepository.findByReadingCountNotOrderByPriorityAscReadingCountAscUpdatedAtAsc(
                        0,
                        Limit.of(Integer.parseInt(settingService.getMap().get(SettingName.LESSON_SIZE)))
                ).stream()
                .map(exercise -> new ExerciseDto(exercise))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ExerciseDto> getMemory() {
        return exerciseRepository.findByReadingCountAndMemoryCountNotOrderByPriorityAscMemoryCountAscUpdatedAtAsc(
                        0,
                        0,
                        Limit.of(Integer.parseInt(settingService.getMap().get(SettingName.LESSON_SIZE)))
                ).stream()
                .map(exercise -> new ExerciseDto(exercise))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ExerciseDto> getRepeating() {
        return exerciseRepository.findByReadingCountAndMemoryCountAndCheckedAtLessThanEqualOrderByPriorityAscCheckedAtAsc(
                        0,
                        0,
                        System.currentTimeMillis(),
                        Limit.of(Integer.parseInt(settingService.getMap().get(SettingName.LESSON_SIZE)))
                ).stream()
                .map(exercise -> new ExerciseDto(exercise))
                .collect(Collectors.toList());
    }

    @Transactional
    public void save(ExerciseDto exerciseDto) {
        Exercise exercise = new Exercise();
        exercise.setId(exerciseDto.getId());
        exercise.setReadingCount(exerciseDto.getReadingCount());
        exercise.setMemoryCount(exerciseDto.getMemoryCount());
        exercise.setPriority(exerciseDto.getPriority());
        exercise.setCheckedAt(exerciseDto.getCheckedAt());
        exercise.setUpdatedAt(exerciseDto.getUpdatedAt());
        exercise.getForeignPhrases().addAll(exerciseDto.getForeignPhrases());
        exercise.getNativePhrases().addAll(exerciseDto.getNativePhrases());

        exerciseRepository.save(exercise);
    }
}
