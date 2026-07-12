package siw_tornei.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import siw_tornei.model.Torneo;

public interface TorneoRepository extends CrudRepository<Torneo, Long> {

    @Query("""
           SELECT DISTINCT t
           FROM Torneo t
           LEFT JOIN FETCH t.squadre
           WHERE t.id = :id
           """)          
    Optional<Torneo> findByIdWithSquadre(Long id);
}