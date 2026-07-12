package siw_tornei.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import siw_tornei.model.Giocatore;

public interface GiocatoreRepository
        extends JpaRepository<Giocatore, Long> {

    @Query("""
        SELECT g
        FROM Giocatore g
        WHERE (
            LOWER(g.nome) LIKE LOWER(CONCAT('%', :testo, '%'))
            OR LOWER(g.cognome) LIKE LOWER(CONCAT('%', :testo, '%'))
        )
        AND (
            :ruolo = ''
            OR g.ruolo = :ruolo
        )
    """)
    Page<Giocatore> cercaConFiltri(
            @Param("testo") String testo,
            @Param("ruolo") String ruolo,
            Pageable pageable
    );

    @Query("""
        SELECT DISTINCT g.ruolo
        FROM Giocatore g
        WHERE g.ruolo IS NOT NULL
        AND g.ruolo <> ''
        ORDER BY g.ruolo
    """)
    List<String> findRuoliDistinti();
}