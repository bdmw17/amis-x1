package de.amis.backend.exception;

import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * Zentraler Fehler-Handler für alle REST-Controller.
 * Nutzt RFC 9457 ProblemDetail (Spring 6+).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Optimistic Locking: zwei Nutzer:innen haben denselben Datensatz gleichzeitig geändert.
     * → HTTP 409 Conflict mit Anweisung, die Seite neu zu laden.
     */
    @ExceptionHandler({OptimisticLockException.class, OptimisticLockingFailureException.class})
    public ResponseEntity<ProblemDetail> handleOptimisticLock(Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setType(URI.create("https://amis.de/errors/optimistic-lock"));
        pd.setTitle("Datensatz wurde zwischenzeitlich geändert");
        pd.setDetail("Ein anderer Nutzer hat diesen Datensatz verändert. Bitte Seite neu laden und Eingabe wiederholen.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd);
    }

    /** Validierungsfehler aus @Valid an Request-Bodies */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {
        String fields = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pd.setType(URI.create("https://amis.de/errors/validation"));
        pd.setTitle("Eingabevalidierung fehlgeschlagen");
        pd.setDetail(fields);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(pd);
    }

    /** Validierungsfehler aus @Validated an Methodenparametern */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pd.setType(URI.create("https://amis.de/errors/validation"));
        pd.setTitle("Eingabevalidierung fehlgeschlagen");
        pd.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(pd);
    }

    /** Ressource nicht gefunden */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setType(URI.create("https://amis.de/errors/not-found"));
        pd.setTitle("Ressource nicht gefunden");
        pd.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    /** Fehlende Berechtigung */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(AccessDeniedException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        pd.setType(URI.create("https://amis.de/errors/forbidden"));
        pd.setTitle("Zugriff verweigert");
        pd.setDetail("Sie haben keine Berechtigung für diese Aktion.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(pd);
    }

    /** Catch-All für unerwartete Fehler */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setType(URI.create("https://amis.de/errors/internal"));
        pd.setTitle("Interner Serverfehler");
        pd.setDetail("Ein unerwarteter Fehler ist aufgetreten.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }
}
