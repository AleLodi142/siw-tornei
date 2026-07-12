package siw_tornei.controller.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import siw_tornei.dto.CommentoDto;
import siw_tornei.dto.CreaCommentoRequest;
import siw_tornei.dto.ModificaCommentoRequest;
import siw_tornei.model.Commento;
import siw_tornei.service.CommentoService;

@RestController
@RequestMapping("/api")
public class CommentoRestController {

    private final CommentoService commentoService;

    public CommentoRestController(
            CommentoService commentoService) {

        this.commentoService = commentoService;
    }

    @GetMapping("/partite/{partitaId}/commenti")
    public ResponseEntity<List<CommentoDto>> getCommentiPartita(
            @PathVariable Long partitaId,
            Authentication authentication) {

        String usernameAutenticato =
                getUsernameAutenticato(authentication);

        List<CommentoDto> commenti =
                commentoService
                        .findByPartitaId(partitaId)
                        .stream()
                        .map(commento ->
                                convertiInDto(
                                        commento,
                                        usernameAutenticato))
                        .toList();

        return ResponseEntity.ok(commenti);
    }

    @PostMapping("/partite/{partitaId}/commenti")
    public ResponseEntity<CommentoDto> creaCommento(
            @PathVariable Long partitaId,
            @Valid @RequestBody
            CreaCommentoRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        Commento commento =
                commentoService.creaCommento(
                        partitaId,
                        request.getTesto(),
                        username);

        CommentoDto dto =
                convertiInDto(commento, username);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dto);
    }

    @PutMapping("/commenti/{commentoId}")
    public ResponseEntity<CommentoDto> modificaCommento(
            @PathVariable Long commentoId,
            @Valid @RequestBody
            ModificaCommentoRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        Commento commento =
                commentoService.modificaCommento(
                        commentoId,
                        request.getTesto(),
                        username);

        CommentoDto dto =
                convertiInDto(commento, username);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/commenti/{commentoId}")
    public ResponseEntity<Void> eliminaCommento(
            @PathVariable Long commentoId,
            Authentication authentication) {

        String username = authentication.getName();

        commentoService.eliminaCommento(
                commentoId,
                username);

        return ResponseEntity.noContent().build();
    }

    private CommentoDto convertiInDto(
            Commento commento,
            String usernameAutenticato) {

        boolean proprietario =
                usernameAutenticato != null
                && commento.getUtente()
                        .getUsername()
                        .equals(usernameAutenticato);

        return new CommentoDto(
                commento.getId(),
                commento.getTesto(),
                commento.getUtente().getUsername(),
                commento.getPartita().getId(),
                proprietario);
    }

    private String getUsernameAutenticato(
            Authentication authentication) {

        if (authentication == null) {
            return null;
        }

        if (!authentication.isAuthenticated()) {
            return null;
        }

        if (authentication
                instanceof AnonymousAuthenticationToken) {

            return null;
        }

        return authentication.getName();
    }
}