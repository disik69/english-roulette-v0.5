package ua.pp.disik.englishroulette.desktop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;

public interface ExerciseRepository extends
        CrudRepository<Exercise, Integer>,
        PagingAndSortingRepository<Exercise, Integer> {
}
