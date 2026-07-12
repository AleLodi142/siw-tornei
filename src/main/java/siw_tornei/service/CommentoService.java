package siw_tornei.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw_tornei.model.Commento;
import siw_tornei.model.Partita;
import siw_tornei.model.Utente;
import siw_tornei.repository.CommentoRepository;

@Service
@Transactional(readOnly = true)
public class CommentoService {

    private CommentoRepository commentoRepository;
    private PartitaService partitaService;
    private UtenteService utenteService;

    public CommentoService(
            CommentoRepository commentoRepository,
            PartitaService partitaService,
            UtenteService utenteService) {

        this.commentoRepository = commentoRepository;
        this.partitaService = partitaService;
        this.utenteService = utenteService;
    }

    public Iterable<Commento> findAll() {
        return commentoRepository.findAll();
    }

    public Commento findById(Long id) {
        return commentoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Commento non trovato"));
    }

    public List<Commento> findByPartitaId(Long partitaId) {

        partitaService.findById(partitaId);

        return commentoRepository
                .findByPartitaIdOrderByIdAsc(partitaId);
    }

    @Transactional
    public Commento creaCommento(
            Long partitaId,
            String testo,
            String username) {

        validaTesto(testo);

        Partita partita =
                partitaService.findById(partitaId);

        Utente utente =
                utenteService.findByUsername(username);

        Commento commento = new Commento();

        commento.setTesto(testo.trim());
        commento.setPartita(partita);
        commento.setUtente(utente);

        return commentoRepository.save(commento);
    }

    @Transactional
    public Commento modificaCommento(
            Long commentoId,
            String nuovoTesto,
            String username) {

        validaTesto(nuovoTesto);

        Commento commento = findById(commentoId);

        verificaProprietario(commento, username);

        commento.setTesto(nuovoTesto.trim());

        return commentoRepository.save(commento);
    }

    @Transactional
    public void eliminaCommento(
            Long commentoId,
            String username) {

        Commento commento = findById(commentoId);

        verificaProprietario(commento, username);

        commentoRepository.delete(commento);
    }

    private void verificaProprietario(
            Commento commento,
            String username) {

        String autoreUsername =
                commento.getUtente().getUsername();

        if (!autoreUsername.equals(username)) {

            throw new AccessDeniedException(
                    "Non puoi modificare o eliminare un commento di un altro utente");
        }
    }

    private void validaTesto(String testo) {

        if (testo == null || testo.isBlank()) {

            throw new IllegalArgumentException(
                    "Il testo del commento è obbligatorio");
        }

        if (testo.trim().length() > 1000) {

            throw new IllegalArgumentException(
                    "Il commento non può superare 1000 caratteri");
        }
    }
}