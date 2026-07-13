package siw_tornei.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw_tornei.model.Squadra;
import siw_tornei.repository.PartitaRepository;
import siw_tornei.repository.SquadraRepository;
import siw_tornei.repository.TorneoRepository;

@Service
@Transactional(readOnly = true)
public class SquadraService {

    private SquadraRepository squadraRepository;
    private TorneoRepository torneoRepository;
    private PartitaRepository partitaRepository;

    public SquadraService (SquadraRepository squadraRepository, TorneoRepository torneoRepository, PartitaRepository partitaRepository) {
        this.squadraRepository = squadraRepository;
        this.torneoRepository = torneoRepository;
        this.partitaRepository = partitaRepository;
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
        Squadra squadra = findById(id);

        boolean partecipaATorneo =torneoRepository.existsBySquadreId(id);

        boolean presenteInPartita =partitaRepository.existsBySquadraCasaIdOrSquadraTrasfertaId(id, id);
        if (partecipaATorneo || presenteInPartita) {

            throw new IllegalStateException(
                    "La squadra "
                    + squadra.getNome()
                    + " non può essere eliminata perché "
                    + "partecipa a un torneo oppure è presente "
                    + "in una partita."
            );
        }

        squadraRepository.deleteById(id);
    }
}
