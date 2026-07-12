package siw_tornei.controller.rest;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import siw_tornei.dto.ErroreDto;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroreDto> gestisciAccessoNegato(
            AccessDeniedException exception) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErroreDto(
                        exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroreDto> gestisciValidazione(
            MethodArgumentNotValidException exception) {

        String messaggio =
                exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(errore ->
                                errore.getDefaultMessage())
                        .distinct()
                        .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroreDto(messaggio));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroreDto> gestisciArgomentoNonValido(
            IllegalArgumentException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroreDto(
                        exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroreDto> gestisciErroreGenerico(
            Exception exception) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroreDto(
                        "Si è verificato un errore durante l'operazione"));
    }
}