package ua.pp.disik.englishroulette.desktop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.pp.disik.englishroulette.desktop.entity.Phrase;

public interface PhraseRepository extends
        CrudRepository<Phrase, Integer>,
        PagingAndSortingRepository<Phrase, Integer> {
}
