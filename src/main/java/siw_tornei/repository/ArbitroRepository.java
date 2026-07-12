package siw_tornei.repository;

import org.springframework.data.repository.CrudRepository;

import siw_tornei.model.Arbitro;

public interface ArbitroRepository extends CrudRepository<Arbitro, Long> {

    boolean existsByCodiceArbitrale(String codiceArbitrale);

    boolean existsByCodiceArbitraleAndIdNot(
            String codiceArbitrale,
            Long id
    );
}