package pl.trello;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class ResponseAdapter {

    public static Response ok(Object body) {
        return Response.of(body, OK);
    }

    public static Response ok() {
        return ok(null);
    }

    public static Response badRequest(Object body) {
        return Response.of(body, BAD_REQUEST);
    }

    public static Response badRequest() {
        return badRequest(null);
    }

    public static Response notFound(Object body) {
        return Response.of(body, NOT_FOUND);
    }

    public static Response notFound() {
        return badRequest(null);
    }

    public static Response forbidden(Object body) {
        return Response.of(body, FORBIDDEN);
    }

    public static Response forbidden() {
        return forbidden(null);
    }
}
