package siw_tornei.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import siw_tornei.model.Partita;

public interface PartitaRepository extends JpaRepository<Partita, Long> {
    
    List<Partita> findByTorneoIdOrderByDataOra(Long torneoId);

    boolean existsBySquadraCasaIdOrSquadraTrasfertaId(Long squadraCasaId, Long squadraTrasfertaId);

    boolean existsByTorneoId(Long torneoId);

    boolean existsByArbitroId(Long arbitroId);
}
