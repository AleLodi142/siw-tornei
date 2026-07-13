package siw_tornei.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw_tornei.model.Partita;
import siw_tornei.model.Torneo;
import siw_tornei.repository.CommentoRepository;
import siw_tornei.repository.PartitaRepository;
import siw_tornei.repository.TorneoRepository;

@Service
@Transactional(readOnly = true)
public class PartitaService {

    private PartitaRepository partitaRepository;
    private TorneoRepository torneoRepository;
    private CommentoRepository commentoRepository;

    public PartitaService(PartitaRepository partitaRepository, TorneoRepository torneoRepository, CommentoRepository commentoRepository) {

        this.commentoRepository = commentoRepository;
        this.partitaRepository = partitaRepository;
        this.torneoRepository = torneoRepository;
    }

    public Iterable<Partita> findAll() {
        return partitaRepository.findAll();
    }

    public Partita findById(Long id) {
        return partitaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Partita non trovata"));
    }

    public List<Partita> findByTorneoIdOrderByDataOra(Long torneoId) {
        return partitaRepository
                .findByTorneoIdOrderByDataOra(torneoId);
    }

    @Transactional
    public Partita save(Partita partita) {


        if (partita.getTorneo() == null ||
                partita.getTorneo().getId() == null) {

            throw new IllegalArgumentException(
                    "Devi selezionare un torneo"
            );
        }

       

        Torneo torneo = torneoRepository
                .findById(partita.getTorneo().getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Il torneo selezionato non esiste"
                        ));

        

        if (partita.getSquadraCasa() == null ||
                partita.getSquadraCasa().getId() == null ||
                partita.getSquadraTrasferta() == null ||
                partita.getSquadraTrasferta().getId() == null) {

            throw new IllegalArgumentException(
                    "Devi selezionare entrambe le squadre"
            );
        }

        Long squadraCasaId =
                partita.getSquadraCasa().getId();

        Long squadraTrasfertaId =
                partita.getSquadraTrasferta().getId();

        if (squadraCasaId.equals(squadraTrasfertaId)) {
            throw new IllegalArgumentException(
                    "La squadra di casa e quella in trasferta devono essere diverse"
            );
        }


        boolean squadraCasaPresente =
                torneo.getSquadre()
                        .stream()
                        .anyMatch(squadra ->
                                squadra.getId()
                                        .equals(squadraCasaId));

        boolean squadraTrasfertaPresente =
                torneo.getSquadre()
                        .stream()
                        .anyMatch(squadra ->
                                squadra.getId()
                                        .equals(squadraTrasfertaId));

        if (!squadraCasaPresente ||
                !squadraTrasfertaPresente) {

            throw new IllegalArgumentException(
                    "Selezionare due squadre appartenenti al torneo scelto"
            );
        }


        if (partita.getArbitro() == null ||
                partita.getArbitro().getId() == null) {

            throw new IllegalArgumentException(
                    "Devi selezionare un arbitro"
            );
        }


        if (partita.getStato() == null ||
                partita.getStato().isBlank()) {

            throw new IllegalArgumentException(
                    "Devi selezionare lo stato della partita"
            );
        }

        if ("SCHEDULED".equals(partita.getStato())) {

            partita.setGoalsHome(null);
            partita.setGoalsAway(null);

        } else if ("PLAYED".equals(partita.getStato())) {

            if (partita.getGoalsHome() == null ||
                    partita.getGoalsAway() == null) {

                throw new IllegalArgumentException(
                        "Per una partita giocata devi inserire entrambi i risultati"
                );
            }

            if (partita.getGoalsHome() < 0 ||
                    partita.getGoalsAway() < 0) {

                throw new IllegalArgumentException(
                        "Il numero di gol non può essere negativo"
                );
            }

        } else {

            throw new IllegalArgumentException(
                    "Stato della partita non valido"
            );
        }

        partita.setTorneo(torneo);

        return partitaRepository.save(partita);
    }

    @Transactional
    public void deleteById(Long id) {

        Partita partita = findById(id);

        boolean contieneCommenti = commentoRepository.existsByPartitaId(id);

        if (contieneCommenti) {

            throw new IllegalStateException(
                    "La partita tra "
                    + partita.getSquadraCasa().getNome()
                    + " e "
                    + partita.getSquadraTrasferta().getNome()
                    + " non può essere eliminata perché "
                    + "contiene uno o più commenti."
                );
        }

        partitaRepository.deleteById(id);
    }
}