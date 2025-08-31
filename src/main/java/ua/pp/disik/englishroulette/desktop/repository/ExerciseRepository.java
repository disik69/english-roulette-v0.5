package ua.pp.disik.englishroulette.desktop.repository;

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
    int countBy();

    List<Exercise> findDistinctByForeignPhrases_BodyContainingOrNativePhrases_BodyContainingOrderByUpdatedAtDesc(
            String foreignBody,
            String nativeBody,
            Pageable pageable
    );
    int countDistinctByForeignPhrases_BodyContainingOrNativePhrases_BodyContaining(
            String foreignBody,
            String nativeBody
    );

    List<Exercise> findByReadingCountNotOrderByPriorityAscReadingCountAscUpdatedAtAsc(
            int readingCount,
            Pageable pageable
    );
    int countByReadingCountNot(
            int readingCount
    );

    List<Exercise> findByReadingCountAndMemoryCountNotOrderByPriorityAscMemoryCountAscUpdatedAtAsc(
            int readingCount,
            int memoryCount,
            Pageable pageable
    );
    int countByReadingCountAndMemoryCountNot(
            int readingCount,
            int memoryCount
    );

    List<Exercise> findByReadingCountAndMemoryCountAndCheckedAtLessThanEqualOrderByPriorityAscCheckedAtAsc(
            int readingCount,
            int memoryCount,
            Long checkedAt,
            Pageable pageable
    );
    int countByReadingCountAndMemoryCountAndCheckedAtLessThanEqual(
            int readingCount,
            int memoryCount,
            Long checkedAt
    );
}
