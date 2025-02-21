package ua.pp.disik.englishroulette.desktop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.pp.disik.englishroulette.desktop.entity.Phrase;

@Repository
public interface PhraseRepository extends
        CrudRepository<Phrase, Integer>,
        PagingAndSortingRepository<Phrase, Integer> {
}
