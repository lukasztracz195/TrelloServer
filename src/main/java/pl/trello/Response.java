package pl.trello;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response extends ResponseEntity {

    private Response(Object body, HttpStatus status) {
        super(body, status);
    }

    public static Response of(Object body, HttpStatus httpStatus) {
        return new Response(body, httpStatus);
    }

    public static Response of(HttpStatus httpStatus) {
        return new Response(null, httpStatus);
    }
}
