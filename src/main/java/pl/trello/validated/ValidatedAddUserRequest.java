package pl.trello.validated;

import org.springframework.http.ResponseEntity;
import pl.trello.dto.request.AddUserRequest;

public class ValidatedAddUserRequest extends ValidatedRequest {

    private final AddUserRequest addUserRequest;
    protected ValidatedAddUserRequest(ResponseEntity responseEntity, boolean valid, AddUserRequest addUserRequest) {
        super(responseEntity, valid);
        this.addUserRequest = addUserRequest;
    }

    public static ValidatedAddUserRequest invalid(ResponseEntity responseEntity) {
        return new ValidatedAddUserRequest(responseEntity, false, null);
    }

    public static ValidatedAddUserRequest valid(AddUserRequest deliveryPoint) {
        return new ValidatedAddUserRequest(null, true, deliveryPoint);
    }
}
