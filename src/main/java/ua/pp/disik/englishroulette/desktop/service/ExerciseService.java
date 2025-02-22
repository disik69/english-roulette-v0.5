package ua.pp.disik.englishroulette.desktop.service;

import org.springframework.stereotype.Service;
import org.apache.commons.collections4.IterableUtils;
import ua.pp.disik.englishroulette.desktop.fx.entity.ExerciseReadDto;
import ua.pp.disik.englishroulette.desktop.repository.ExerciseRepository;

import java.util.List;

@Service
public class ExerciseService implements RepositoryService {
    private final ExerciseRepository exerciseRepository;

    public ExerciseService(
            ExerciseRepository exerciseRepository
    ) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public Object repository() {
        return exerciseRepository;
    }

    public List<ExerciseReadDto> findAll() {
        return IterableUtils.toList(exerciseRepository.findAll()).stream()
                .map(exercise -> new ExerciseReadDto(exercise))
                .toList();
    }
}
