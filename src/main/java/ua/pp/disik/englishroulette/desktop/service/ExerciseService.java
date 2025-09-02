package ua.pp.disik.englishroulette.desktop.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.entity.ExerciseDto;
import ua.pp.disik.englishroulette.desktop.repository.ExerciseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseService implements RepositoryService<ExerciseRepository> {
    private final ExerciseRepository exerciseRepository;

    public ExerciseService(
            ExerciseRepository exerciseRepository
    ) {
        this.exerciseRepository = exerciseRepository;
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

    public int countAll() {
        return exerciseRepository.countBy();
    }

    @Transactional
    public List<ExerciseDto> findAllByFilter(String filter, int page, int size) {
        return exerciseRepository.findDistinctByForeignPhrases_BodyContainingOrNativePhrases_BodyContainingOrderByUpdatedAtDesc(
                        filter,
                        filter,
                        PageRequest.of(page, size)
                ).stream()
                .map(exercise -> new ExerciseDto(exercise))
                .toList();
    }

    public int countAllByFilter(String filter) {
        return exerciseRepository.countDistinctByForeignPhrases_BodyContainingOrNativePhrases_BodyContaining(
                filter,
                filter
        );
    }

    @Transactional
    public ExerciseDto findById(Integer id) {
        Exercise exercise = exerciseRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise doesn't exist."));
        return new ExerciseDto(exercise);
    }

    @Transactional
    public List<ExerciseDto> getReading(int page, int size) {
        return exerciseRepository.findByReadingCountNotOrderByPriorityAscReadingCountAscUpdatedAtAsc(
                        0,
                        PageRequest.of(page, size)
                ).stream()
                .map(exercise -> new ExerciseDto(exercise))
                .collect(Collectors.toList());
    }

    public int countReading() {
        return exerciseRepository.countByReadingCountNot(
                0
        );
    }

    @Transactional
    public List<ExerciseDto> getMemory(int page, int size) {
        return exerciseRepository.findByReadingCountAndMemoryCountNotOrderByPriorityAscMemoryCountAscUpdatedAtAsc(
                        0,
                        0,
                        PageRequest.of(page, size)
                ).stream()
                .map(exercise -> new ExerciseDto(exercise))
                .collect(Collectors.toList());
    }

    public int countMemory() {
        return exerciseRepository.countByReadingCountAndMemoryCountNot(
                0,
                0
        );
    }

    @Transactional
    public List<ExerciseDto> getRepeating(int page, int size) {
        return exerciseRepository.findByReadingCountAndMemoryCountAndCheckedAtLessThanEqualOrderByPriorityAscCheckedAtAsc(
                        0,
                        0,
                        System.currentTimeMillis(),
                        PageRequest.of(page, size)
                ).stream()
                .map(exercise -> new ExerciseDto(exercise))
                .collect(Collectors.toList());
    }

    public int countRepeating() {
        return exerciseRepository.countByReadingCountAndMemoryCountAndCheckedAtLessThanEqual(
                0,
                0,
                System.currentTimeMillis()
        );
    }

    public void save(ExerciseDto exerciseDto) {
        Exercise exercise = new Exercise();
        exercise.setId(exerciseDto.getId());
        exercise.setReadingCount(exerciseDto.getReadingCount());
        exercise.setMemoryCount(exerciseDto.getMemoryCount());
        exercise.setPriority(exerciseDto.getPriority());
        exercise.setCheckedAt(exerciseDto.getCheckedAt());
        exercise.setUpdatedAt(exerciseDto.getUpdatedAt());
        if (exercise.getId() == null) {
            // Phrase cascading
            // PERSIST -> PERSIST
            // MERGE -> PERSIST, MERGE
            exerciseRepository.save(exercise);
        }
        exercise.getForeignPhrases().addAll(exerciseDto.getForeignPhrases());
        exercise.getNativePhrases().addAll(exerciseDto.getNativePhrases());
        exerciseRepository.save(exercise);
    }
}
