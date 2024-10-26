package ua.pp.disik.englishroulette.desktop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.pp.disik.englishroulette.desktop.entity.Word;

import java.util.Optional;

public interface WordRepository extends
        CrudRepository<Word, Integer>,
        PagingAndSortingRepository<Word, Integer> {
    Optional<Word> findByBody(String body);
    Page<Word> findByBodyContaining(String body, Pageable pageable);
}
