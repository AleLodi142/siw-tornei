package siw_tornei.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import siw_tornei.model.Giocatore;

public interface GiocatoreRepository extends JpaRepository<Giocatore, Long> {
    
}
