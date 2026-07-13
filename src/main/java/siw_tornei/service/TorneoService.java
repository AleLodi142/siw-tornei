package siw_tornei.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw_tornei.model.Torneo;
import siw_tornei.repository.PartitaRepository;
import siw_tornei.repository.TorneoRepository;

@Service
@Transactional(readOnly = true)
public class TorneoService {

    private TorneoRepository torneoRepository;
    private PartitaRepository partitaRepository;

    public TorneoService(TorneoRepository torneoRepository, PartitaRepository partitaRepository) {
        this.torneoRepository = torneoRepository;
        this.partitaRepository = partitaRepository;
    }

    public Iterable<Torneo> findAll() {
        return torneoRepository.findAll();
    }

    public Torneo findById(Long id) {
        return torneoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Torneo non trovato"));
    }

    public Torneo findByIdWithSquadre(Long id) {
        return torneoRepository.findByIdWithSquadre(id).orElseThrow(() ->
            new IllegalArgumentException("Torneo non trovato"));
    }

    @Transactional
    public Torneo save(Torneo torneo) {
        return torneoRepository.save(torneo);
    }

    @Transactional
    public void deleteById(Long id) {

        Torneo torneo = findById(id);

        boolean contienePartite = partitaRepository.existsByTorneoId(id);

        if (contienePartite) {

            throw new IllegalStateException(
                    "Il torneo "
                    + torneo.getNome()
                    + " non può essere eliminato perché "
                    + "contiene delle partite."
            );
        }
        torneoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public void testLazy(Long id) {

        Torneo torneo = this.findById(id);

        torneo.getSquadre().size();
    }

    @Transactional(readOnly = true)
    public void testFetch(Long id) {

        Torneo torneo = this.findByIdWithSquadre(id);

        torneo.getSquadre().size();
    }

}