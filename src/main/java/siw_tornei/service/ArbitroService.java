package siw_tornei.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw_tornei.model.Arbitro;
import siw_tornei.repository.ArbitroRepository;

@Service
@Transactional(readOnly = true)
public class ArbitroService {

    private ArbitroRepository arbitroRepository;

    public ArbitroService(ArbitroRepository arbitroRepository) {
        this.arbitroRepository = arbitroRepository;
    }

    public Iterable<Arbitro> findAll() {
        return arbitroRepository.findAll();
    }

    public Arbitro findById(Long id) {
        return arbitroRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Arbitro non trovato"));
    }

    @Transactional
    public Arbitro save(Arbitro arbitro) {

        boolean codiceGiaUsato;

        if (arbitro.getId() == null) {
            codiceGiaUsato =
                    arbitroRepository.existsByCodiceArbitrale(
                            arbitro.getCodiceArbitrale()
                    );
        } else {
            codiceGiaUsato =
                    arbitroRepository.existsByCodiceArbitraleAndIdNot(
                            arbitro.getCodiceArbitrale(),
                            arbitro.getId()
                    );
        }

        if (codiceGiaUsato) {
            throw new IllegalArgumentException(
                    "Esiste già un arbitro con questo codice arbitrale"
            );
        }

        return arbitroRepository.save(arbitro);
    }

    @Transactional
    public void deleteById(Long id) {
        arbitroRepository.deleteById(id);
    }
}