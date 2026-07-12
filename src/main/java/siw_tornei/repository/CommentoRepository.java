package siw_tornei.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import siw_tornei.model.Commento;

public interface CommentoRepository extends JpaRepository<Commento, Long> {

    List<Commento> findByPartitaIdOrderByIdAsc(Long partitaId);
}