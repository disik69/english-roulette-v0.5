package ua.pp.disik.englishroulette.desktop.service;

import org.springframework.stereotype.Service;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.transaction.PlatformTransactionManager;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;
import ua.pp.disik.englishroulette.desktop.fx.entity.ExerciseReadDto;
import ua.pp.disik.englishroulette.desktop.fx.entity.ExerciseWriteDto;
import ua.pp.disik.englishroulette.desktop.repository.ExerciseRepository;

import java.util.List;

@Service
public class ExerciseService implements RepositoryService<ExerciseRepository> {
    private final ExerciseRepository exerciseRepository;
    private final PlatformTransactionManager platformTransactionManager;

    public ExerciseService(
            ExerciseRepository exerciseRepository,
            PlatformTransactionManager platformTransactionManager
    ) {
        this.exerciseRepository = exerciseRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Override
    public ExerciseRepository repository() {
        return exerciseRepository;
    }

    public List<ExerciseReadDto> findAll() {
        return IterableUtils.toList(exerciseRepository.findAll()).stream()
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
        platformTransactionManager.getTransaction(null);

        return exerciseRepository.save(exercise);
    }
}
