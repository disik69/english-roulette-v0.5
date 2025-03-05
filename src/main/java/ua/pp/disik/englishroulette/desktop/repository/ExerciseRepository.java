package ua.pp.disik.englishroulette.desktop.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.pp.disik.englishroulette.desktop.entity.Exercise;

import java.util.List;

@Repository
public interface ExerciseRepository extends
        CrudRepository<Exercise, Integer>,
        PagingAndSortingRepository<Exercise, Integer> {
    List<Exercise> findByOrderByUpdatedAtDesc(Pageable pageable);
    List<Exercise> findByForeignPhrases_BodyContainingOrNativePhrases_BodyContainingOrderByUpdatedAtDesc(
            String foreignBody,
            String nativeBody,
            Pageable pageable
    );
    List<Exercise> findByReadingCountNotOrderByPriorityAscReadingCountAscUpdatedAtAsc(
            int readingCount,
            Limit limit
    );
    List<Exercise> findByReadingCountAndMemoryCountNotOrderByPriorityAscMemoryCountAscUpdatedAtAsc(
            int readingCount,
            int memoryCount,
            Limit limit
    );
    List<Exercise> findByReadingCountAndMemoryCountAndCheckedAtLessThanEqualOrderByPriorityAscCheckedAtAsc(
            int readingCount,
            int memoryCount,
            Long checkedAt,
            Limit limit
    );
}
