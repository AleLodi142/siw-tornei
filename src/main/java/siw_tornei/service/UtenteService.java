package siw_tornei.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;

import siw_tornei.model.Utente;
import siw_tornei.repository.UtenteRepository;

@Service
@Transactional(readOnly = true)
public class UtenteService {

    private UtenteRepository utenteRepository;
    private PasswordEncoder passwordEncoder;


    public UtenteService(UtenteRepository utenteRepository,  PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Iterable<Utente> findAll() {
        return utenteRepository.findAll();
    }

    public Utente findById(Long id) {
        return utenteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
    }

     public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Utente non trovato"));
    }

    
     public boolean existsByUsername(String username) {
        return utenteRepository.existsByUsername(username);
    }

    @Transactional
    public Utente registraUtente(Utente utente) {

        if (utenteRepository.existsByUsername(utente.getUsername())) {
            throw new IllegalArgumentException("Username già utilizzato");
        }

        utente.setRuolo(Utente.USER_ROLE);

        String passwordCodificata = passwordEncoder.encode(utente.getPassword());

        utente.setPassword(passwordCodificata);

        return utenteRepository.save(utente);
    }

}