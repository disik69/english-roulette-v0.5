package ua.pp.disik.englishroulette.desktop.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.fx.entity.ExerciseReadDto;
import ua.pp.disik.englishroulette.desktop.fx.entity.ExerciseWriteDto;
import ua.pp.disik.englishroulette.desktop.repository.ExerciseRepository;

import java.util.List;

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

    public Exercise save(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }
}
