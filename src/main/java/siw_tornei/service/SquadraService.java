package siw_tornei.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw_tornei.model.Squadra;
import siw_tornei.repository.SquadraRepository;

@Service
@Transactional(readOnly = true)
public class SquadraService {

    private SquadraRepository squadraRepository;

    public SquadraService (SquadraRepository squadraRepository) {
        this.squadraRepository = squadraRepository;
    }

    public Iterable<Squadra> findAll() {
        return squadraRepository.findAll();
    }

    public Squadra findById(Long id) {
        return squadraRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Squadra non trovata"));            
    }

    @Transactional
    public Squadra save(Squadra squadra){
        return squadraRepository.save(squadra);
    }

    @Transactional
    public void deleteById(Long id) {
        squadraRepository.deleteById(id);
    }
}
