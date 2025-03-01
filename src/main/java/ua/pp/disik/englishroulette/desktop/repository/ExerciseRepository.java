package ua.pp.disik.englishroulette.desktop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;

@Repository
public interface ExerciseRepository extends
        CrudRepository<Exercise, Integer>,
        PagingAndSortingRepository<Exercise, Integer> {
}
