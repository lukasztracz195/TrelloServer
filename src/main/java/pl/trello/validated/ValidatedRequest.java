package pl.trello.validated;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ValidatedRequest {
    private final ResponseEntity responseEntity;

    private final boolean valid;

    protected ValidatedRequest(ResponseEntity responseEntity, boolean valid) {
        this.responseEntity = responseEntity;
        this.valid = valid;
    }

    public static ValidatedRequest invalid(ResponseEntity responseEntity) {
        return new ValidatedRequest(responseEntity, false);
    }

    public static ValidatedRequest valid() {
        return new ValidatedRequest(null, true);
    }
}
