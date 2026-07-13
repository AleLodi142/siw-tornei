package siw_tornei.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw_tornei.model.Arbitro;
import siw_tornei.repository.ArbitroRepository;
import siw_tornei.repository.PartitaRepository;

@Service
@Transactional(readOnly = true)
public class ArbitroService {

    private ArbitroRepository arbitroRepository;
    private PartitaRepository partitaRepository;

    public ArbitroService(ArbitroRepository arbitroRepository, PartitaRepository partitaRepository) {
        this.arbitroRepository = arbitroRepository;
        this.partitaRepository = partitaRepository;
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

        Arbitro arbitro = findById(id);

        boolean assegnatoAPartita = partitaRepository.existsByArbitroId(id);

        if (assegnatoAPartita) {

            throw new IllegalStateException(
                    "L'arbitro "
                    + arbitro.getNome()
                    + " "
                    + arbitro.getCognome()
                    + " non può essere eliminato perché "
                    + "è assegnato a una o più partite."
                );
        }

        arbitroRepository.deleteById(id);
    }
}