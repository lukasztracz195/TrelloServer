package pl.trello.validated;

import org.springframework.http.ResponseEntity;
import pl.trello.dto.request.AddUserRequestDTO;

public class ValidatedAddUserRequest extends ValidatedRequest {

    private final AddUserRequestDTO addUserRequestDTO;
    protected ValidatedAddUserRequest(ResponseEntity responseEntity, boolean valid, AddUserRequestDTO addUserRequestDTO) {
        super(responseEntity, valid);
        this.addUserRequestDTO = addUserRequestDTO;
    }

    public static ValidatedAddUserRequest invalid(ResponseEntity responseEntity) {
        return new ValidatedAddUserRequest(responseEntity, false, null);
    }

    public static ValidatedAddUserRequest valid(AddUserRequestDTO deliveryPoint) {
        return new ValidatedAddUserRequest(null, true, deliveryPoint);
    }
}
