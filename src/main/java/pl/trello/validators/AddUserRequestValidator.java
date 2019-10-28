package pl.trello.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.trello.ResponseAdapter;
import pl.trello.dto.request.AddUserRequest;
import pl.trello.repository.UserRepository;

@Component
public class AddUserRequestValidator implements RequestValidator<AddUserRequest> {

    private final UserRepository userRepository;

    public AddUserRequestValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ValidatedRequest validate(AddUserRequest request) {
        if (!(StringUtils.isNotBlank(request.getLogin()) || StringUtils.isNotBlank(request.getPassword()))) {
            return ValidatedRequest.invalid(ResponseAdapter.badRequest("Username or password is blank"));
        }

        return userRepository.findByLogin(request.getLogin())
                .map(user -> ValidatedRequest.invalid(ResponseAdapter.badRequest("User is taken")))
                .orElseGet(ValidatedRequest::valid);
    }


}