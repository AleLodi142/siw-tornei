package siw_tornei.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw_tornei.model.Giocatore;
import siw_tornei.repository.GiocatoreRepository;

@Service
@Transactional(readOnly = true)
public class GiocatoreService {
    
    private GiocatoreRepository giocatoreRepository;

    public GiocatoreService (GiocatoreRepository giocatoreRepository) {
        this.giocatoreRepository = giocatoreRepository;
    }

    public Iterable<Giocatore> findAll() {
        return giocatoreRepository.findAll();
    }

    public Giocatore findById(Long id)  {
        return giocatoreRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Giocatore non trovato"));
    }
    
    @Transactional
    public Giocatore save (Giocatore giocatore) {
        return giocatoreRepository.save(giocatore);
    }

    @Transactional
    public void deleteById(Long id) {
        giocatoreRepository.deleteById(id);
    }
}
