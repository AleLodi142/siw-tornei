package siw_tornei.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import siw_tornei.dto.AuthDto;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @GetMapping("/me")
    public ResponseEntity<AuthDto> getUtenteCorrente(
            Authentication authentication,
            CsrfToken csrfToken) {

        /*
         * Accedendo al valore del token costringiamo Spring
         * a generarlo e a inviarlo nel cookie XSRF-TOKEN.
         */
        csrfToken.getToken();

        boolean autenticato =
                authentication != null
                && authentication.isAuthenticated()
                && !(authentication
                        instanceof AnonymousAuthenticationToken);

        if (!autenticato) {

            return ResponseEntity.ok(
                    new AuthDto(false, null));
        }

        return ResponseEntity.ok(
                new AuthDto(
                        true,
                        authentication.getName()));
    }
}