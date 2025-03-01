package ua.pp.disik.englishroulette.desktop.service;

import org.springframework.stereotype.Service;
import ua.pp.disik.englishroulette.desktop.repository.PhraseRepository;

@Service
public class PhraseService implements RepositoryService<PhraseRepository> {
    public final PhraseRepository phraseRepository;

    public PhraseService(
            PhraseRepository phraseRepository
    ) {
        this.phraseRepository = phraseRepository;
    }

    @Override
    public PhraseRepository repository() {
        return phraseRepository;
    }
}
