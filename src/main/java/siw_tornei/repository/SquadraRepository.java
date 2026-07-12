package siw_tornei.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import siw_tornei.model.Squadra;

public interface SquadraRepository extends JpaRepository<Squadra, Long> {
    
}
